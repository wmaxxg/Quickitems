package me.wmaxxg.quickitems;
import me.wmaxxg.quickitems.items.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public final class Quickitems extends JavaPlugin {

    @Override
    public void onEnable() {
        File quickitemsFolder = new File(System.getProperty("user.dir") + "\\plugins\\quickitems");
        if (!(quickitemsFolder.exists())) {
            try {
                quickitemsFolder.mkdir();
            } catch (SecurityException e) {
                System.out.println("ERROR, COULDN'T CREATE PLUGIN FOLDER, PLUGIN WON'T WORK PROPERLY WITHOUT IT");
            }
        }
        getCommand("createitem").setTabCompleter(new AutoTab());
        getCommand("modifyitem").setTabCompleter(new AutoTab());
        getCommand("summonitem").setTabCompleter(new AutoTab());
        getCommand("deleteitem").setTabCompleter(new AutoTab());
        getCommand("quickitems").setTabCompleter(new AutoTab());
    }

    @Override
    public void onDisable() {
        //shutdown
        //reload
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("summonitem")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.DARK_RED + "You cannot execute this command from console");
                return true;
            }
            Player player = (Player) sender;
            ItemManager.getItem(player, args);
            return true;
        }
        if (label.equalsIgnoreCase("modifyitem")) {
            ItemManager.modifyItem(sender, args);
            return true;
        }
        if (label.equalsIgnoreCase("createitem")) {
            ItemManager.createItem(sender, args);
            return true;
        }
        if (label.equalsIgnoreCase("deleteitem")) {
            ItemManager.deleteItem(sender, args[0]);
            return true;
        }
        if (label.equalsIgnoreCase("quickitems"))
        {
            if(args.length!=1)
                sender.sendMessage(ChatColor.RED+"Syntax error: usage: /quickitems help");
            if (args[0].equals("help"))
                sender.sendMessage(ChatColor.GOLD+"Quickitems provides 5 commands including the help command:\n" +
                                                  "/quickitems help | shows help message (you're reading it now btw)\n" +
                                                  "/createitem <item-name> <minecraft-item-id> | Creates item of given id\n" +
                                                  "/modifyitem <item-name> [set/unset] <attribute> <attribute-parameter> <value> | modifies given attribute of chosen item\n" +
                                                  "/summonitem <item-name> | Gives item to specified player");
        }
        return true;
    }

}
