package me.wmaxxg.quickitems;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;

public class FileManager {

    public static boolean checkFilePresence(String name){
        File file = new File(System.getProperty("user.dir")+"\\plugins\\quickitems\\"+name+".json");
        return file.exists();
    }
    public static boolean createFile(CommandSender sender, String name){
        try {
            File myObj = new File(System.getProperty("user.dir")+"\\plugins\\quickitems\\"+name+".json");
            if (myObj.createNewFile()) {
                return true;
            } else {
                sender.sendMessage(ChatColor.RED+"Item with given name already exist! delete the existing one or check for a typo.");
                return false;
            }
        } catch (IOException e) {
            System.out.println("An error occurred. file path: "+System.getProperty("user.dir")+"\\plugins\\quickitems\\"+name+".json");
            sender.sendMessage(ChatColor.DARK_RED+"Error has occured");
            e.printStackTrace();
        }
        return false;
    };
    public static boolean writeToJSON(JsonObject item, String name){
        FileWriter file = null;
        try {
            file = new FileWriter(System.getProperty("user.dir") + "\\plugins\\quickitems\\" + name + ".json");
            file.write(item.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                assert file != null;
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    public static JsonObject readFromJSON(String name) {
        JsonObject item;
        File file;
        try {
            InputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\plugins\\quickitems\\" + name + ".json");
            try (JsonReader reader = Json.createReader(fis)) {
                item = reader.readObject();
                return item;
            } catch (JsonException e) {
                e.printStackTrace();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean deleteFile(String name)
    {
        File file = new File(System.getProperty("user.dir")+"\\plugins\\quickitems\\"+name+".json");
        return file.delete();
    }
}















