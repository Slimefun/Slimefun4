package me.mrCookieSlime.Slimefun.MySQL;

import me.mrCookieSlime.Slimefun.MySQL.Components.*;
import me.mrCookieSlime.Slimefun.Setup.Files;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MySQLMain {

    //private Connection connection;
    private HashMap<String, Database> connections;
    public static MySQLMain instance;
    public String titan_mysql_host;
    public String titan_mysql_port;
    private String titan_mysql_database;
    public String titan_mysql_username;
    public String titan_mysql_password;
    private Boolean MySQL_enabled;
    public HashMap<Integer, BukkitRunnable> tasksSaver = new HashMap<Integer, BukkitRunnable>();
    private FileConfiguration config;
    private HashMap<String, Boolean> loaded = new HashMap<String, Boolean>();
    private Table block_storage;
    private HashMap<String, List<HashMap<String, ResultData>>> load_storage;
    public MySQLMain()
    {
        instance = this;
        load_storage = new HashMap<String, List<HashMap<String, ResultData>>>();
        this.config = YamlConfiguration.loadConfiguration(Files.MYSQL);

        if (!this.config.contains("mysql.enable"))
        {
            this.config.set("mysql.enable", false);
        }
        if (!this.config.contains("mysql.host"))
        {
            this.config.set("mysql.host", "Host_Adrress_Here");
        }
        if (!this.config.contains("mysql.port"))
        {
            this.config.set("mysql.port", "3306");
        }
        if (!this.config.contains("mysql.database"))
        {
            this.config.set("mysql.database", "Database_Name_Here");
        }
        if (!this.config.contains("mysql.username"))
        {
            this.config.set("mysql.username", "Username_Name_Here");
        }
        if (!this.config.contains("mysql.password"))
        {
            this.config.set("mysql.password", "Password_Name_Here");
        }
        try {
            this.config.save(Files.MYSQL);
        } catch (IOException e) {
            e.printStackTrace();
        }


        titan_mysql_host = this.config.getString("mysql.host");
        titan_mysql_port = this.config.getString("mysql.port");
        titan_mysql_database = this.config.getString("mysql.database");
        titan_mysql_username = this.config.getString("mysql.username");
        titan_mysql_password = this.config.getString("mysql.password");
        MySQL_enabled = this.config.getBoolean("mysql.enable");
        connections = new HashMap<String, Database>();
        if (!MySQL_enabled) return;

        Database myDefault = new Database(titan_mysql_database,true);
        connections.put("defualt", myDefault);
        connections.put(titan_mysql_database, myDefault);


        Bukkit.getScheduler().runTaskTimer(SlimefunStartup.instance, new Runnable() {
            @Override
            public void run() {
                for (String key: connections.keySet())
                {
                    if (!key.equals("defualt"))
                    {
                        connections.get(key).ping();
                    }
                }
            }
        }, 3600 * 20L, 3600 * 20L);


        this.setupTables();
        System.out.println("[Slimefun, MySQL]: Initialized and Enabled.");


    }

    public boolean isLoaded(String world) {
        if (!loaded.containsKey(world)) return false;
        return loaded.get(world);
    }

    public void setLoaded(String world, boolean loaded) {
        this.loaded.put(world, loaded);
    }

    public void setLoad_storage(String world, List<HashMap<String, ResultData>> load_storage) {
        this.load_storage.put(world, load_storage);
    }

    public List<HashMap<String, ResultData>> getLoad_storage(String world) {
        return load_storage.get(world);
    }

    public Table getBlock_storage() {
        return block_storage;
    }

    public boolean isEnabled()
    {
        return this.MySQL_enabled;
    }
    public void setupTables()
    {
        block_storage = new Table("slimefun_block_storage");
        //Creating table if there isn't one
        //id must be used for the primary key
        block_storage.addDataType("id", DataTypeEnum.CHARARRAY, true, true, true);
        block_storage.addDataType("slimefun_id", DataTypeEnum.CHARARRAY, true, false, false);
        block_storage.addDataType("world", DataTypeEnum.CHARARRAY, true, false, false);
        block_storage.addDataType("json", DataTypeEnum.STRING, true, false, false);
        block_storage.createTable();
    }
    public static String getPlugin() {
        StackTraceElement[] AllCalls = Thread.currentThread().getStackTrace();
        Plugin[] AllPlugins  = Bukkit.getPluginManager().getPlugins();
        for (int i = 3; i < AllCalls.length; i++)
        {
            StackTraceElement callingFrame = AllCalls[i];
            String splitter[] = callingFrame.toString().split("\\.");
            for(String name: splitter)
            {
                for (Plugin p: AllPlugins)
                {
                    if (p.getName().equalsIgnoreCase(name))
                    {
                        return p.getName();
                    }
                }
            }
        }
        return "Unknown";
    }
    public static String getSimpleTrace() {
        StackTraceElement[] AllCalls = Thread.currentThread().getStackTrace();
        String calls = "";
        for (int i = 3; i < AllCalls.length; i++)
        {
            StackTraceElement callingFrame = AllCalls[i];
            return  callingFrame + "-->\n";
        }
        return "No Trace???";
    }
    public static String getTrace() {
        StackTraceElement[] AllCalls = Thread.currentThread().getStackTrace();
        String calls = "";
        for (int i = 2; i < AllCalls.length; i++)
        {
            StackTraceElement callingFrame = AllCalls[i];
            calls  = calls + callingFrame + "-->\n";
        }
        return calls;
    }
    public void disable()
    {
        long timepass = System.currentTimeMillis();
        int sizeleft = printThreadCounts();
        long killTimer = System.currentTimeMillis() + 300000;
        while (sizeleft > 0)
        {
            if (System.currentTimeMillis() >= killTimer)
            {
                System.out.println("Killing Threads...");
                break;
            }
            if (System.currentTimeMillis() - timepass > 15000)
            {

                timepass = System.currentTimeMillis();
                sizeleft = printThreadCounts();
                int SecondsLeft = (int) ((killTimer - System.currentTimeMillis()) / 1000);
                System.out.println("Killing Threads in: " +SecondsLeft + " Seconds");
            }
        }
        killThreadCounts();
    }

    private void killThreadCounts() {
        List<BukkitTask> tmpTask = Bukkit.getScheduler().getPendingTasks();
        for(BukkitTask BT: tmpTask)
        {
            if (BT.getOwner().getName().equals(SlimefunStartup.instance.getName())) {
                if (!BT.isCancelled()) {
                    BT.cancel();
                }
            }
        }
    }

    private int printThreadCounts() {
        List<BukkitTask> tmpTask = Bukkit.getScheduler().getPendingTasks();
        List<BukkitRunnable> waitingCount = new ArrayList<BukkitRunnable>();
        List<BukkitTask> waitingCountNoPlugin = new ArrayList<BukkitTask>();
        for(BukkitTask BT: tmpTask) {
            if (!BT.isCancelled()) {
                if (BT.getOwner().getName().equals(SlimefunStartup.instance.getName())) {

                    if (tasksSaver.containsKey(BT.getTaskId())) {
                        waitingCount.add(tasksSaver.get(BT.getTaskId()));
                    }
                } else {
                    waitingCountNoPlugin.add(BT);
                }
            }
        }
        System.out.println("[Slimefun, MySQL]: There are threads being saved: " + waitingCount.size() + " (+" + waitingCountNoPlugin.size() + ")");
        for (BukkitRunnable key: waitingCount)
        {
            if (key instanceof MySQLRunnable) {
                MySQLRunnable keyT = (MySQLRunnable)key;
                String running = Bukkit.getScheduler().isCurrentlyRunning(keyT.getTaskId()) +"";
                String qued = Bukkit.getScheduler().isQueued(keyT.getTaskId()) +"";
                System.out.println("----------------------- [Running:" + running + ", Queued:" + qued + "] "  + keyT.plugin + " (" + keyT.time + ") -----------------------\n" + keyT.trace);
                if (!Bukkit.getScheduler().isCurrentlyRunning(keyT.getTaskId()))
                {
                    System.out.println("[Slimefun, MySQL]: Running...");
                    keyT.run();
                    keyT.cancel();
                }
            }
        }
/*
        for (BukkitTask key: waitingCountNoPlugin)
        {
            ((CraftTask)key).run();
            System.out.println(key.getOwner().getName());
        }*/
        System.out.println("[Slimefun, MySQL]: End List");

        return  waitingCount.size();
    }

    public void onEnable(){
        instance = this;

    }


    public Database getDatebase() {

        return connections.get("defualt");
    }
    public Database getDatebase(String mysql_database) {

        if (connections.containsKey(mysql_database)) {
            return connections.get(mysql_database);
        }
        else {
            return  null;
        }
    }
    public void addDatabase(String mysql_database, boolean KeepAlive)
    {
        Database mydatabase = new Database(mysql_database,KeepAlive);
        connections.put(mysql_database, mydatabase);
    }

    /**
     * Encodes an {@link ItemStack} in a Base64 String
     * @param itemStack {@link ItemStack} to encode
     * @return Base64 encoded String
     */
    public static String encode(ItemStack itemStack) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("i", itemStack);
        return DatatypeConverter.printBase64Binary(config.saveToString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Encodes an {@link Location} in a Base64 String
     * @param location {@link Location} to encode
     * @return Base64 encoded String
     */
    public static String encode(Location location) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("i.x", location.getX());
        config.set("i.y", location.getY());
        config.set("i.z", location.getZ());
        config.set("i.pitch", location.getPitch() + "");
        config.set("i.yaw", location.getYaw() + "");
        if (location.getWorld() == null)
        {
            config.set("i.world", "worldmain");
        }
        else {
            config.set("i.world", location.getWorld().getName());
        }
        return DatatypeConverter.printBase64Binary(config.saveToString().getBytes(StandardCharsets.UTF_8));
    }
    /**
     * Encodes an {@link List<Integer>} in a Base64 String
     * Encodes an {@link List<String>} in a Base64 String
     * Encodes an {@link List<ItemStack>} in a Base64 String
     * @param list {@link List} to encode
     * @return Base64 encoded String
     */
    public static String encode(List list) {
        YamlConfiguration config = new YamlConfiguration();
        if (list.size() > 0)
        {
            if (list.get(0) instanceof  ItemStack)
            {
                int i = 0;
                for (ItemStack is: (List<ItemStack>)list)
                {
                    config.set("i" + i, is);
                    i++;
                }
                return DatatypeConverter.printBase64Binary(config.saveToString().getBytes(StandardCharsets.UTF_8));
            }
        }
        config.set("i", list);
        return DatatypeConverter.printBase64Binary(config.saveToString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decodes an {@link ItemStack} from a Base64 String
     * @param string Base64 encoded String to decode
     * @return Decoded {@link ItemStack}
     */
    public static ItemStack decodeItemStack(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(DatatypeConverter.parseBase64Binary(string), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        return config.getItemStack("i", null);
    }


    /**
     * Decodes an {@link ItemStack} from a Base64 String
     * @param string Base64 encoded String to decode
     * @return Decoded {@link ItemStack}
     */
    public static List<ItemStack> decodeItemList(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(DatatypeConverter.parseBase64Binary(string), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        List<ItemStack> tmp = new ArrayList<ItemStack>();
        for (String key: config.getKeys(false))
        {
            ItemStack itsub = config.getItemStack(key);
            tmp.add(itsub);
        }

        return tmp;
    }

    /**
     * Decodes an {@link ItemStack} from a Base64 String
     * @param string Base64 encoded String to decode
     * @return Decoded {@link ItemStack}
     */
    public static List<String> decodeStringList(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(DatatypeConverter.parseBase64Binary(string), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        List<String> tmp = (List<String>) config.getList("i", new ArrayList<String>());
        return tmp;
    }
    /**
     * Decodes an {@link ItemStack} from a Base64 String
     * @param string Base64 encoded String to decode
     * @return Decoded {@link ItemStack}
     */
    public static List<Integer> decodeIntList(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(DatatypeConverter.parseBase64Binary(string), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        List<Integer> tmp = (List<Integer>) config.getList("i", new ArrayList<Integer>());
        return tmp;
    }

    /**
     * Decodes an {@link Location} from a Base64 String
     * @param string Base64 encoded String to decode
     * @return Decoded {@link Location}
     */
    public static Location decodeLocation(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(DatatypeConverter.parseBase64Binary(string), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        double x = config.getDouble("i.x");
        double y = config.getDouble("i.y");
        double z = config.getDouble("i.z");
        float pitch =  Float.valueOf(config.getString("i.pitch"));
        float yaw = Float.valueOf(config.getString("i.yaw"));
        String worldname = config.getString("i.world");
        World world = Bukkit.getWorld(worldname);
        Location location = new Location(world, x, y, z, yaw, pitch);
        return  location.clone();
    }
}
