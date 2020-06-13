package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;

public final class Script {

    private final Config config;
    private final String name;
    private final String author;
    private final String code;

    protected Script(Config config) {
        this.config = config;
        this.name = config.getString("name");
        this.code = config.getString("code");

        OfflinePlayer player = Bukkit.getOfflinePlayer(config.getUUID("author"));
        this.author = player.getName() != null ? player.getName() : config.getString("author_name");
    }

    /**
     * This returns the name of this {@link Script}.
     * 
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * This returns the author of this {@link Script}.
     * The author is the person who initially created and uploaded this {@link Script}.
     * 
     * @return The author of this {@link Script}
     */
    public String getAuthor() {
        return author;
    }

    /**
     * This method returns the actual code of this {@link Script}.
     * It is basically a {@link String} describing the order of {@link Instruction Instructions} that
     * shall be executed.
     * 
     * @return The code for this {@link Script}
     */
    public String getSourceCode() {
        return code;
    }

    /**
     * This method determines whether the given {@link OfflinePlayer} is the author of
     * this {@link Script}.
     * 
     * @param p
     *            The {@link OfflinePlayer} to check for
     * 
     * @return Whether the given {@link OfflinePlayer} is the author of this {@link Script}.
     */
    public boolean isAuthor(OfflinePlayer p) {
        return p.getUniqueId().equals(config.getUUID("author"));
    }

    /**
     * This method checks whether a given {@link Player} is able to leave a rating for this {@link Script}.
     * 
     * 
     * @param p
     * @return
     */
    public boolean canRate(Player p) {
        if (isAuthor(p)) {
            return false;
        }

        List<String> upvoters = config.getStringList("rating.positive");
        List<String> downvoters = config.getStringList("rating.negative");
        return !upvoters.contains(p.getUniqueId().toString()) && !downvoters.contains(p.getUniqueId().toString());
    }

    /**
     * This method returns the amount of upvotes this {@link Script} has received.
     * 
     * @return The amount of upvotes
     */
    public int getUpvotes() {
        return config.getStringList("rating.positive").size();
    }

    /**
     * This method returns the amount of downvotes this {@link Script} has received.
     * 
     * @return The amount of downvotes
     */
    public int getDownvotes() {
        return config.getStringList("rating.negative").size();
    }

    /**
     * This returns how often this {@link Script} has been downloaded.
     * 
     * @return The amount of downloads for this {@link Script}.
     */
    public int getDownloads() {
        return config.getInt("downloads");
    }

    /**
     * This returns the "rating" of this {@link Script}.
     * This value is calculated from the up- and downvotes this {@link Script} received.
     * 
     * @return The rating for this {@link Script}
     */
    public float getRating() {
        int positive = getUpvotes() + 1;
        int negative = getDownvotes();
        return Math.round((positive / (float) (positive + negative)) * 100.0F) / 100.0F;
    }

    /**
     * This method increases the amount of downloads by one.
     */
    public void download() {
        config.reload();
        config.setValue("downloads", getDownloads() + 1);
        config.save();
    }

    public void rate(Player p, boolean positive) {
        config.reload();

        String path = "rating." + (positive ? "positive" : "negative");
        List<String> list = config.getStringList(path);
        list.add(p.getUniqueId().toString());

        config.setValue(path, list);
        config.save();
    }

    public static List<Script> getUploadedScripts(AndroidType androidType) {
        List<Script> scripts = new LinkedList<>();

        File directory = new File("plugins/Slimefun/scripts/" + androidType.name());
        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (File script : directory.listFiles()) {
            if (script.getName().endsWith("sfs")) {
                scripts.add(new Script(new Config(script)));
            }
        }

        if (androidType != AndroidType.NONE) {
            File mainDirectory = new File("plugins/Slimefun/scripts/NONE");
            if (!mainDirectory.exists()) {
                mainDirectory.mkdirs();
            }

            for (File script : mainDirectory.listFiles()) {
                if (script.getName().endsWith(".sfs")) {
                    scripts.add(new Script(new Config(script)));
                }
            }
        }

        Collections.sort(scripts, Comparator.comparingInt(script -> -script.getUpvotes() + 1 - script.getDownvotes()));
        return scripts;
    }

    public static void upload(Player p, AndroidType androidType, int id, String name, String code) {
        Config script = new Config("plugins/Slimefun/scripts/" + androidType.name() + '/' + p.getName() + ' ' + String.valueOf(id) + ".sfs");

        script.setValue("author", p.getUniqueId().toString());
        script.setValue("author_name", p.getName());
        script.setValue("name", ChatUtils.removeColorCodes(name));
        script.setValue("code", code);
        script.setValue("downloads", 0);
        script.setValue("android", androidType.name());
        script.setValue("rating.positive", new ArrayList<String>());
        script.setValue("rating.negative", new ArrayList<String>());
        script.save();
    }

}
