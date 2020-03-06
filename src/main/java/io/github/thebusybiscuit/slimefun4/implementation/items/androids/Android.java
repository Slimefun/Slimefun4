package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.chat.ChatInput;
import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

abstract class Android extends SlimefunItem {

    public Android(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    public abstract AndroidType getAndroidType();

    protected abstract void tick(Block b);

    @Override
    protected boolean areItemHandlersPrivate() {
        return true;
    }

    @Override
    public void preRegister() {
        super.preRegister();

        addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem sf, me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config data) {
                if (b != null) {
                    Android.this.tick(b);
                }
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        });
    }

    protected void killEntities(Block b, double damage, Predicate<Entity> predicate) {
        throw new UnsupportedOperationException("Non-butcher Android tried to butcher!");
    }

    protected void fish(Block b, BlockMenu menu) {
        throw new UnsupportedOperationException("Non-fishing Android tried to fish!");
    }

    protected void mine(Block b, BlockMenu menu, Block block) {
        throw new UnsupportedOperationException("Non-mining Android tried to mine!");
    }

    protected void movedig(Block b, BlockMenu menu, BlockFace face, Block block) {
        throw new UnsupportedOperationException("Non-mining Android tried to mine!");
    }

    protected boolean chopTree(Block b, BlockMenu menu, BlockFace face) {
        throw new UnsupportedOperationException("Non-woodcutter Android tried to chop a Tree!");
    }

    protected void farm(BlockMenu menu, Block block) {
        throw new UnsupportedOperationException("Non-farming Android tried to farm!");
    }

    protected void exoticFarm(BlockMenu menu, Block block) {
        throw new UnsupportedOperationException("Non-farming Android tried to farm!");
    }

    public void openScript(Player p, Block b, String script) {
        ChestMenu menu = new ChestMenu(ChatColor.DARK_AQUA + SlimefunPlugin.getLocal().getMessage(p, "android.scripts.editor"));
        String[] commands = PatternUtils.DASH.split(script);

        menu.addItem(0, new CustomItem(ScriptPart.START.getItem(), SlimefunPlugin.getLocal().getMessage(p, "android.scripts.instructions.START"), "", "&7\u21E8 &eLeft Click &7to return to the Android's interface"));
        menu.addMenuClickHandler(0, (pl, slot, item, action) -> {
            BlockStorage.getInventory(b).open(pl);
            return false;
        });

        for (int i = 1; i < commands.length; i++) {
            int index = i;

            if (i == commands.length - 1) {
                int additional = commands.length == 54 ? 0 : 1;

                if (additional == 1) {
                    menu.addItem(i, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTcxZDg5NzljMTg3OGEwNTk4N2E3ZmFmMjFiNTZkMWI3NDRmOWQwNjhjNzRjZmZjZGUxZWExZWRhZDU4NTIifX19"), "&7> Add new Command"));
                    menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
                        openScriptComponentEditor(pl, b, script, index);
                        return false;
                    });
                }

                menu.addItem(i + additional, new CustomItem(ScriptPart.REPEAT.getItem(), SlimefunPlugin.getLocal().getMessage(p, "android.scripts.instructions.REPEAT"), "", "&7\u21E8 &eLeft Click &7to return to the Android's interface"));
                menu.addMenuClickHandler(i + additional, (pl, slot, item, action) -> {
                    BlockStorage.getInventory(b).open(pl);
                    return false;
                });
            }
            else {
                ItemStack stack = ScriptPart.valueOf(commands[i]).getItem();
                menu.addItem(i, new CustomItem(stack, SlimefunPlugin.getLocal().getMessage(p, "android.scripts.instructions." + ScriptPart.valueOf(commands[i]).name()), "", "&7\u21E8 &eLeft Click &7to edit", "&7\u21E8 &eRight Click &7to delete", "&7\u21E8 &eShift + Right Click &7to duplicate"));
                menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
                    if (action.isRightClicked() && action.isShiftClicked()) {
                        if (commands.length == 54) return false;

                        int j = 0;
                        StringBuilder builder = new StringBuilder(ScriptPart.START + "-");

                        for (String command : commands) {
                            if (j > 0) {
                                if (j == index) {
                                    builder.append(commands[j]).append('-').append(commands[j]).append('-');
                                }
                                else if (j < commands.length - 1) builder.append(command).append('-');
                            }
                            j++;
                        }
                        builder.append(ScriptPart.REPEAT);
                        BlockStorage.addBlockInfo(b, "script", builder.toString());

                        openScript(pl, b, builder.toString());
                    }
                    else if (action.isRightClicked()) {
                        int j = 0;
                        StringBuilder builder = new StringBuilder(ScriptPart.START + "-");

                        for (String command : commands) {
                            if (j != index && j > 0 && j < commands.length - 1) builder.append(command).append('-');
                            j++;
                        }

                        builder.append(ScriptPart.REPEAT);
                        BlockStorage.addBlockInfo(b, "script", builder.toString());

                        openScript(pl, b, builder.toString());
                    }
                    else {
                        openScriptComponentEditor(pl, b, script, index);
                    }
                    return false;
                });
            }
        }

        menu.open(p);
    }

    protected void openScriptDownloader(Player p, Block b, int page) {
        ChestMenu menu = new ChestMenu("Android Scripts");
        menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.7F, 0.7F));

        List<Config> scripts = getUploadedScripts();

        int index = 0;
        int pages = (scripts.size() / 45) + 1;

        for (int i = 45; i < 54; i++) {
            menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
            menu.addMenuClickHandler(i, (pl, slot, item, action) -> false);
        }

        menu.addItem(46, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&r\u21E6 Previous Page", "", "&7(" + page + " / " + pages + ")"));
        menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
            int next = page - 1;
            if (next < 1) next = pages;
            if (next != page) {
                openScriptDownloader(pl, b, next);
            }
            return false;
        });

        menu.addItem(48, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTA1YTJjYWI4YjY4ZWE1N2UzYWY5OTJhMzZlNDdjOGZmOWFhODdjYzg3NzYyODE5NjZmOGMzY2YzMWEzOCJ9fX0="), "&eUpload a Script", "", "&6Click &7to upload your Android's Script", "&7to the Database"));
        menu.addMenuClickHandler(48, (pl, slot, item, action) -> {
            uploadScript(pl, b, page);
            return false;
        });

        menu.addItem(50, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&rNext Page \u21E8", "", "&7(" + page + " / " + pages + ")"));
        menu.addMenuClickHandler(50, (pl, slot, item, action) -> {
            int next = page + 1;
            if (next > pages) next = 1;
            if (next != page) {
                openScriptDownloader(pl, b, next);
            }
            return false;
        });

        menu.addItem(53, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE4NWM5N2RiYjgzNTNkZTY1MjY5OGQyNGI2NDMyN2I3OTNhM2YzMmE5OGJlNjdiNzE5ZmJlZGFiMzVlIn19fQ=="), "&6> Back", "", "&7Return to the Android's interface"));
        menu.addMenuClickHandler(53, (pl, slot, item, action) -> {
            openScriptEditor(pl, b);
            return false;
        });

        int categoryIndex = 45 * (page - 1);

        for (int i = 0; i < 45; i++) {
            int target = categoryIndex + i;

            if (target >= scripts.size()) {
                break;
            }
            else {
                Config script = scripts.get(target);

                OfflinePlayer op = Bukkit.getOfflinePlayer(script.getUUID("author"));
                String author = (op != null && op.getName() != null) ? op.getName() : script.getString("author_name");

                if (script.getString("author").equals(p.getUniqueId().toString())) {
                    menu.addItem(index, new CustomItem(this.getItem(), "&b" + script.getString("name"), "&7by &r" + author, "", "&7Downloads: &r" + script.getInt("downloads"), "&7Rating: " + getScriptRatingPercentage(script), "&a" + getScriptRating(script, true) + " \u263A &7- &4\u2639 " + getScriptRating(script, false), "", "&eLeft Click &rto download this Script", "&4(This will override your current Script)"));
                }
                else {
                    menu.addItem(index, new CustomItem(this.getItem(), "&b" + script.getString("name"), "&7by &r" + author, "", "&7Downloads: &r" + script.getInt("downloads"), "&7Rating: " + getScriptRatingPercentage(script), "&a" + getScriptRating(script, true) + " \u263A &7- &4\u2639 " + getScriptRating(script, false), "", "&eLeft Click &rto download this Script", "&4(This will override your current Script)", "&eShift + Left Click &rto leave a positive Rating", "&eShift + Right Click &rto leave a negative Rating"));
                }

                menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
                    Config script2 = new Config(script.getFile());

                    if (action.isShiftClicked()) {
                        if (script2.getString("author").equals(pl.getUniqueId().toString())) {
                            SlimefunPlugin.getLocal().sendMessage(pl, "android.scripts.rating.own", true);
                        }
                        else if (action.isRightClicked()) {
                            if (!script2.getStringList("rating.negative").contains(pl.getUniqueId().toString()) && !script2.getStringList("rating.positive").contains(pl.getUniqueId().toString())) {
                                List<String> list = script2.getStringList("rating.negative");
                                list.add(p.getUniqueId().toString());

                                script2.setValue("rating.negative", list);
                                script2.save();

                                openScriptDownloader(pl, b, page);
                            }
                            else {
                                SlimefunPlugin.getLocal().sendMessage(pl, "android.scripts.rating.already", true);
                            }
                        }
                        else {
                            if (!script2.getStringList("rating.negative").contains(pl.getUniqueId().toString()) && !script2.getStringList("rating.positive").contains(pl.getUniqueId().toString())) {
                                List<String> list = script2.getStringList("rating.positive");
                                list.add(pl.getUniqueId().toString());

                                script2.setValue("rating.positive", list);
                                script2.save();

                                openScriptDownloader(pl, b, page);
                            }
                            else {
                                SlimefunPlugin.getLocal().sendMessage(pl, "android.scripts.rating.already", true);
                            }
                        }
                    }
                    else if (!action.isRightClicked()) {
                        script2.setValue("downloads", script2.getInt("downloads") + 1);
                        script2.save();

                        BlockStorage.addBlockInfo(b, "script", script2.getString("code"));
                        openScriptEditor(pl, b);
                    }
                    return false;
                });

                index++;
            }
        }

        menu.open(p);
    }

    private void uploadScript(Player p, Block b, int page) {
        String code = BlockStorage.getLocationInfo(b.getLocation(), "script");
        int num = 1;

        for (Config script : getUploadedScripts()) {
            if (script.getString("author").equals(p.getUniqueId().toString())) {
                num++;
            }

            if (script.getString("code").equals(code)) {
                SlimefunPlugin.getLocal().sendMessage(p, "android.scripts.already-uploaded", true);
                return;
            }
        }

        int id = num;

        p.closeInventory();
        SlimefunPlugin.getLocal().sendMessages(p, "android.scripts.enter-name");

        ChatInput.waitForPlayer(SlimefunPlugin.instance, p, msg -> {
            Config script = new Config("plugins/Slimefun/scripts/" + getAndroidType().toString() + '/' + p.getName() + ' ' + id + ".sfs");

            script.setValue("author", p.getUniqueId().toString());
            script.setValue("author_name", p.getName());
            script.setValue("name", ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', msg)));
            script.setValue("code", code);
            script.setValue("downloads", 0);
            script.setValue("android", getAndroidType().toString());
            script.setValue("rating.positive", new ArrayList<String>());
            script.setValue("rating.negative", new ArrayList<String>());
            script.save();

            SlimefunPlugin.getLocal().sendMessages(p, "android.scripts.uploaded");
            openScriptDownloader(p, b, page);
        });
    }

    public void openScriptEditor(Player p, Block b) {
        ChestMenu menu = new ChestMenu(ChatColor.DARK_AQUA + SlimefunPlugin.getLocal().getMessage(p, "android.scripts.editor"));

        menu.addItem(1, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDliZjZkYjRhZWRhOWQ4ODIyYjlmNzM2NTM4ZThjMThiOWE0ODQ0Zjg0ZWI0NTUwNGFkZmJmZWU4N2ViIn19fQ=="), "&2> Edit Script", "", "&aEdits your current Script"));
        menu.addMenuClickHandler(1, (pl, slot, item, action) -> {
            openScript(pl, b, BlockStorage.getLocationInfo(b.getLocation(), "script"));
            return false;
        });

        menu.addItem(3, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTcxZDg5NzljMTg3OGEwNTk4N2E3ZmFmMjFiNTZkMWI3NDRmOWQwNjhjNzRjZmZjZGUxZWExZWRhZDU4NTIifX19"), "&4> Create new Script", "", "&cDeletes your current Script", "&cand creates a blank one"));
        menu.addMenuClickHandler(3, (pl, slot, item, action) -> {
            openScript(pl, b, "START-TURN_LEFT-REPEAT");
            return false;
        });

        menu.addItem(5, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzAxNTg2ZTM5ZjZmZmE2M2I0ZmIzMDFiNjVjYTdkYThhOTJmNzM1M2FhYWI4OWQzODg2NTc5MTI1ZGZiYWY5In19fQ=="), "&6> Download a Script", "", "&eDownload a Script from the Server", "&eYou can edit or simply use it"));
        menu.addMenuClickHandler(5, (pl, slot, item, action) -> {
            openScriptDownloader(pl, b, 1);
            return false;
        });

        menu.addItem(8, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE4NWM5N2RiYjgzNTNkZTY1MjY5OGQyNGI2NDMyN2I3OTNhM2YzMmE5OGJlNjdiNzE5ZmJlZGFiMzVlIn19fQ=="), "&6> Back", "", "&7Return to the Android's interface"));
        menu.addMenuClickHandler(8, (pl, slot, item, action) -> {
            BlockStorage.getInventory(b).open(p);
            return false;
        });

        menu.open(p);
    }

    protected List<Config> getUploadedScripts() {
        List<Config> scripts = new ArrayList<>();

        File directory = new File("plugins/Slimefun/scripts/" + getAndroidType().toString());
        if (!directory.exists()) directory.mkdirs();

        for (File script : directory.listFiles()) {
            if (script.getName().endsWith("sfs")) {
                scripts.add(new Config(script));
            }
        }

        if (getAndroidType() != AndroidType.NONE) {
            File mainDirectory = new File("plugins/Slimefun/scripts/NONE");
            if (!mainDirectory.exists()) mainDirectory.mkdirs();

            for (File script : mainDirectory.listFiles()) {
                if (script.getName().endsWith("sfs")) {
                    scripts.add(new Config(script));
                }
            }
        }

        Collections.sort(scripts, Comparator.comparingInt(script -> -(getScriptRating(script, true) + 1 - getScriptRating(script, false))));

        return scripts;
    }

    protected List<ScriptPart> getAccessibleScriptParts() {
        List<ScriptPart> list = new ArrayList<>();

        for (ScriptPart part : ScriptPart.values()) {
            if (part != ScriptPart.START && part != ScriptPart.REPEAT && getAndroidType().isType(part.getRequiredType())) {
                list.add(part);
            }
        }

        return list;
    }

    protected float getScriptRating(Config script) {
        int positive = getScriptRating(script, true) + 1;
        int negative = getScriptRating(script, false);
        return Math.round((positive / (double) (positive + negative)) * 100.0F) / 100.0F;
    }

    protected int getScriptRating(Config script, boolean positive) {
        if (positive) return script.getStringList("rating.positive").size();
        else return script.getStringList("rating.negative").size();
    }

    protected String getScriptRatingPercentage(Config script) {
        String progress = String.valueOf(getScriptRating(script));
        if (Float.parseFloat(progress) < 16.0F) progress = "&4" + progress + "&r% ";
        else if (Float.parseFloat(progress) < 32.0F) progress = "&c" + progress + "&r% ";
        else if (Float.parseFloat(progress) < 48.0F) progress = "&6" + progress + "&r% ";
        else if (Float.parseFloat(progress) < 64.0F) progress = "&e" + progress + "&r% ";
        else if (Float.parseFloat(progress) < 80.0F) progress = "&2" + progress + "&r% ";
        else progress = "&a" + progress + "&r% ";

        return progress;
    }

    protected void openScriptComponentEditor(Player p, Block b, String script, int index) {
        ChestMenu menu = new ChestMenu(ChatColor.DARK_AQUA + SlimefunPlugin.getLocal().getMessage(p, "android.scripts.editor"));
        String[] commands = PatternUtils.DASH.split(script);

        ChestMenuUtils.drawBackground(menu, 0, 1, 2, 3, 4, 5, 6, 7, 8);

        menu.addItem(9, new CustomItem(SkullItem.fromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTYxMzlmZDFjNTY1NGU1NmU5ZTRlMmM4YmU3ZWIyYmQ1YjQ5OWQ2MzM2MTY2NjNmZWVlOTliNzQzNTJhZDY0In19fQ=="), "&rDo nothing"), (pl, slot, item, action) -> {
            int i = 0;
            StringBuilder builder = new StringBuilder("START-");

            for (String command : commands) {
                if (i != index && i > 0 && i < commands.length - 1) builder.append(command).append('-');
                i++;
            }

            builder.append("REPEAT");
            BlockStorage.addBlockInfo(b, "script", builder.toString());

            openScript(p, b, builder.toString());
            return false;
        });

        int i = 10;
        for (ScriptPart part : getAccessibleScriptParts()) {
            menu.addItem(i, new CustomItem(part.getItem(), SlimefunPlugin.getLocal().getMessage(p, "android.scripts.instructions." + part.name())), (pl, slot, item, action) -> {
                addInstruction(pl, b, index, part, commands);
                return false;
            });
            i++;
        }

        menu.open(p);
    }

    private void addInstruction(Player p, Block b, int index, ScriptPart part, String[] commands) {
        int j = 0;
        StringBuilder builder = new StringBuilder("START-");

        for (String command : commands) {
            if (j > 0) {
                if (j == index) builder.append(part).append('-');
                else if (j < commands.length - 1) builder.append(command).append('-');
            }
            j++;
        }

        builder.append("REPEAT");
        BlockStorage.addBlockInfo(b, "script", builder.toString());

        openScript(p, b, builder.toString());
    }
}
