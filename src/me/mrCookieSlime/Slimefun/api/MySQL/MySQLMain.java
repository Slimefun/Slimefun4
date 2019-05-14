package me.mrCookieSlime.Slimefun.api.MySQL;

import com.firesoftitan.play.titansql.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MySQLMain {


    private TitanSQL titanSQL;
    private Table block_storage;
    private HashMap<String, List<HashMap<String, ResultData>>> load_storage;
    private HashMap<String, String> IDtoKeys = new HashMap<String, String>();
    private boolean enabled = false;
    public static MySQLMain instance;
    public MySQLMain()
    {
        instance = this;
        titanSQL = TitanSQL.instance;
        if (!SlimefunStartup.getCfg().contains("options.mySQL_Enabled"))
        {
            SlimefunStartup.getCfg().setValue("options.mySQL_Enabled", false);
        }
        this.enabled = SlimefunStartup.getCfg().getBoolean("options.mySQL_Enabled");
        if (!this.enabled) return;
        setupTables();
        load_storage = new HashMap<String, List<HashMap<String, ResultData>>>();
        for (World world: Bukkit.getWorlds()) {
            block_storage.search("world", world.getName(), new CallbackResults() {
                @Override
                public void onResult(List<HashMap<String, ResultData>> results) {
                    load_storage.put(world.getName(), results);
                    System.out.println("[Slimefun] MySQL, world: " + world.getName() + " data received for " + results.size() + " blocks.");
                }
            });
        }
        this.enabled = true;

    }
    public void setupTables()
    {
        block_storage = new Table("slimefun_block_storage");
        //Creating table if there isn't one
        //id must be used for the primary key
        block_storage.addDataType("id", DataTypeEnum.CHARARRAY, true, true, true);
        block_storage.addDataType("location", DataTypeEnum.CHARARRAY, true, false, false);
        block_storage.addDataType("slimefun_id", DataTypeEnum.CHARARRAY, true, false, false);
        block_storage.addDataType("world", DataTypeEnum.CHARARRAY, true, false, false);
        block_storage.addDataType("json", DataTypeEnum.STRING, true, false, false);
        block_storage.createTable();
    }
    public String getNewIDString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random(System.currentTimeMillis());
        while (salt.length() < 36) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        if (IDtoKeys.containsValue(saltStr))
        {
            return getNewIDString();
        }
        return saltStr;

    }
    public void deleteSQLID(Location location)
    {
        IDtoKeys.remove(serializeLocation(location));
    }
    public String getSQLID(Location location)
    {
        return IDtoKeys.get(serializeLocation(location));
    }
    public void addSQLID(Location location, String ID)
    {
        IDtoKeys.put(serializeLocation(location), ID);
    }
    private String serializeLocation(Location l) {
        return l.getWorld().getName() + ";" + l.getBlockX() + ";" + l.getBlockY() + ";" + l.getBlockZ();
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void disable()
    {
        enabled = false;
    }
    public Table getBlock_storage() {
        return block_storage;
    }
    public boolean isLoaded(String worldname)
    {
        return load_storage.containsKey(worldname);
    }
    public List<HashMap<String, ResultData>> getLoad_storage(String worldname)
    {
        return load_storage.get(worldname);
    }
    private static Map<String, String> parseJSON(String json) {
        Map<String, String> map = new HashMap<String, String>();

        if (json != null && json.length() > 2) {
            try {
                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(json);
                for (Object entry: obj.keySet()) {
                    String key = entry.toString();
                    String value = obj.get(entry).toString();
                    map.put(key, value);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
