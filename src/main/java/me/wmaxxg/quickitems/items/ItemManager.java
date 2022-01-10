package me.wmaxxg.quickitems.items;
import me.wmaxxg.quickitems.FileManager;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    public static void createItem(CommandSender sender, String[] args) {
        if(args.length!=2) {
            sender.sendMessage(ChatColor.RED + "Syntax error: usage: /createitem <item name> <item id>");
        }
        else {
            if (FileManager.createFile(sender, args[0])) {
                JsonObject item = Json.createObjectBuilder()
                        .add("itemName", Json.createObjectBuilder()
                            .add("name", args[0])
                            .add("prefix", ""))
                        .add("itemTag", args[1])
                        .add("itemDescription", Json.createObjectBuilder()
                            .add("prefix","")
                            .add("description","")
                            .build())
                        .add("itemEnchant", Json.createObjectBuilder().build())
                        .add("itemAttribute",Json.createObjectBuilder().build())
                        .add("itemFlag", Json.createObjectBuilder().build())
                        .build();

                if(FileManager.writeToJSON(item, args[0]))
                    sender.sendMessage(ChatColor.GREEN+"Succesfully created item!");
            }
        }
    }

    public static boolean getItem(Player player, String[] args){
        ItemStack item = buildItem(player, args);
        if(player.getInventory().firstEmpty()==-1)
        {
            Location loc = player.getLocation();
            World world = player.getWorld();
            world.dropItemNaturally(loc, item);
            return true;
        }
        player.getInventory().addItem(item);
        return true;
    }
    // /modifyitem <nazwa> <atrybut> [set/unset] <wartość>
    public static boolean modifyItem(CommandSender sender, String[] args){
        if(!(FileManager.checkFilePresence(args[0])))
        {
            sender.sendMessage(ChatColor.RED+"Item with given name does not exist! check for a typo.");
            return true;
        }
        JsonObject item = FileManager.readFromJSON(args[0]);

        assert item != null;
        switch(args[2])
        {
            case "attribute":
                if(ItemManager.modifyJSON("itemAttribute",item, args)) sender.sendMessage(ChatColor.GREEN+"Succesfully modified item!");
                break;
            case "enchant":
                if(ItemManager.modifyJSON("itemEnchant",item, args)) sender.sendMessage(ChatColor.GREEN+"Succesfully modified item!");
                break;
            case "name":
                if(ItemManager.modifyJSON("itemName",item, args)) sender.sendMessage(ChatColor.GREEN+"Succesfully modified item!");
                break;
            case "description":
                if(ItemManager.modifyJSON("itemDescription",item, args)) sender.sendMessage(ChatColor.GREEN+"Succesfully modified item!");
                break;
            case "flag":
                if(ItemManager.modifyJSON("itemFlag",item, args)) sender.sendMessage(ChatColor.GREEN+"Succesfully modified item!");
                break;
            case "tag":
                sender.sendMessage(ChatColor.RED+"You cannot change item tag, create new item or change it in json file");
                break;
            default:
                sender.sendMessage(ChatColor.RED+"Syntax error: usage: /modifyitem <item-name> [set/unset] [attribute/enchant] <attribute/enchant-name> <value>");
                break;
        }
        return true;
    }
    public static void deleteItem(CommandSender sender, String name)
    {
        if(!(FileManager.checkFilePresence(name)))
        {
            sender.sendMessage(ChatColor.RED+"Cannot remove item: item does not exist! check for a typo.");
        }
        if(FileManager.deleteFile(name))
        {
            sender.sendMessage(ChatColor.GREEN+"Item called "+name+" has been removed");
        }

    }
    private static boolean modifyJSON(String type,JsonObject item,String[] args)
    {
        JsonObjectBuilder mod = Json.createObjectBuilder();
        for(String key : item.keySet())
        {
            if(!(key.equals(type)))
            {
                mod.add(key, item.get(key));
            }
            else {
                JsonObjectBuilder modSub = Json.createObjectBuilder();
                JsonObject attr = item.getJsonObject(key);
                for(String subkey : attr.keySet())
                {
                    if(!(subkey.equals(args[3])))
                    {
                        modSub.add(subkey, attr.get(subkey));
                    }
                    else
                    {
                        if(args[1].equals("set"))
                            modSub.add(subkey, args[4]);
                        else if(args[1].equals("unset"))
                            modSub.add(subkey, "");
                    }

                }
                if(!(modSub.build().containsKey(args[3])))
                {
                    if(args[1].equals("set"))
                        modSub.add(args[3], args[4]);
                    else if(args[1].equals("unset"))
                        modSub.add(args[3], "");
                }
                mod.add(key, modSub.build());
            }
        }
        return FileManager.writeToJSON(mod.build(), args[0]);
    }



    private static ItemStack buildItem(Player player, String[] args) {
        if(!(FileManager.checkFilePresence(args[0])))
        {
            player.sendMessage(ChatColor.RED+"Item with given name does not exist! check for a typo.");
            return null;
        }
        JsonObject itemData = FileManager.readFromJSON(args[0]);
        JsonObject itemName = itemData.getJsonObject("itemName");
        String pre = itemName.getString("prefix");
        String name = itemName.getString("name");
        if (pre == null)
            pre = "&r&f";
        String displayname = pre+name;
        String material = itemData.getString("itemTag");
        ItemStack item = new ItemStack(Material.matchMaterial(material));
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8&l[&7======&r " + displayname + " &7======&8]&r"));
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', " &e&m==========&r&a Opis &e&m==========&r "));
        JsonObject itemDesc = itemData.getJsonObject("itemDescription");
        String desc = itemDesc.getString("description");
        String preDesc = itemDesc.getString("prefix");
        if(preDesc==null)
            preDesc="&r";
        if (desc!=null)
            lore.add(ChatColor.translateAlternateColorCodes('&', preDesc+desc));
        lore.add(null);
        lore.add(ChatColor.translateAlternateColorCodes('&', " &e&m========&r&a Statystyki &e&m=======&r "));
        meta.setLore(lore);
        JsonObject itemEnchants = itemData.getJsonObject("itemEnchant");
        for (String enchant: itemEnchants.keySet()) {
            meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(enchant.substring(10))), Integer.parseInt(itemEnchants.getString(enchant)), true);
        }
        JsonObject itemAttributes = itemData.getJsonObject("itemAttribute");
        for (String attr: itemAttributes.keySet()) {
            AttributeModifier modifier = new AttributeModifier("itemModifier",Float.parseFloat(itemAttributes.getString(attr)),AttributeModifier.Operation.ADD_NUMBER);
            meta.addAttributeModifier(Attribute.valueOf(attr.toUpperCase().substring(10)),modifier);
        }
        item.setItemMeta(meta);
        return item;
    }
}
