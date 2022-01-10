package me.wmaxxg.quickitems;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class AutoTab implements TabCompleter {

    List<String> autoCompletes = new ArrayList<>();
    List<String> options = new ArrayList<>();
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        autoCompletes.clear();
        options.clear();
        if (label.equalsIgnoreCase("createitem"))
        {
            if (args.length == 1) {
                autoCompletes.add("<name>");
                return autoCompletes;
            }
            if (args.length == 2) {
                for (Material material: Material.values())
                {
                    if(material.toString().toLowerCase().startsWith(args[1]))
                    {
                        autoCompletes.add("minecraft:"+ material.toString().toLowerCase());
                    }
                }
                return autoCompletes;
            }

        }
        if (label.equalsIgnoreCase("modifyitem"))
        {

            if (args.length == 1) {
                autoCompletes.add("<name>");
                return autoCompletes;
            }
            if (args.length == 2) {

                options.add("set");
                options.add("unset");
                for (String option: options)
                {
                    if(option.startsWith(args[1]))
                    {
                        autoCompletes.add(option);
                    }
                }
                return autoCompletes;
            }
            if (args.length == 3) {
                options.add("name");
                options.add("description");
                options.add("enchant");
                options.add("attribute");
                options.add("flag");
                for (String option: options)
                {
                    if(option.startsWith(args[2]))
                    {
                        autoCompletes.add(option);
                    }
                }
                return autoCompletes;
            }
            if (args.length ==4)
            {
                switch(args[2])
                {
                    case "enchant":
                        for (Enchantment enchant : Enchantment.values()) {
                            if(enchant.toString().toLowerCase().startsWith(args[3]))
                            {
                                autoCompletes.add("minecraft:"+ enchant.getKey().getKey());
                            }
                        }
                        break;
                    case "attribute":
                        for (Attribute attr: Attribute.values())
                        {
                            if(attr.toString().toLowerCase().startsWith(args[3]))
                            {
                                autoCompletes.add("minecraft:"+attr.toString().toLowerCase());
                            }
                        }
                        break;
                    case "name":
                        options.add("name");
                        options.add("prefix");
                        for (String option: options)
                        {
                            if(option.startsWith(args[3]))
                            {
                                autoCompletes.add(option);
                            }
                        }
                        break;
                    case "description":
                        options.add("description");
                        options.add("prefix");
                        for (String option: options)
                        {
                            if(option.startsWith(args[3]))
                            {
                                autoCompletes.add(option);
                            }
                        }
                        break;
                    case "flag":
                        sender.sendMessage(ChatColor.RED+"item's flag is not supported yet");
                        break;
                }


            }
            if(args.length == 5)
            {
                autoCompletes.add("<value/level>");
            }
                return autoCompletes;

        }
        if (label.equalsIgnoreCase("summonitem")) {
            if (args.length == 1)
            {
                autoCompletes.add("<name>");
                return autoCompletes;
            }
        }
        if (label.equalsIgnoreCase("deleteitem")) {
            if (args.length == 1)
            {
                autoCompletes.add("<name>");
                return autoCompletes;
            }
        }
        if (label.equalsIgnoreCase("quickitems")) {
            if (args.length == 1)
            {
                autoCompletes.add("help");
                return autoCompletes;
            }
        }
        return null;
    }
}
