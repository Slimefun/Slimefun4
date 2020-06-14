package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import io.github.starwishsama.extra.ProtectionChecker;
import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.chat.ChatInput;
import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.skull.SkullBlock;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;

public abstract class ProgrammableAndroid extends SlimefunItem implements InventoryBlock, RecipeDisplayItem {

    private static final List<BlockFace> POSSIBLE_ROTATIONS = Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
    private static final int[] BORDER = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 18, 24, 25, 26, 27, 33, 35, 36, 42, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
    private static final int[] OUTPUT_BORDER = {10, 11, 12, 13, 14, 19, 23, 28, 32, 37, 38, 39, 40, 41};

    protected final Set<MachineFuel> fuelTypes = new HashSet<>();
    protected final String texture;

    public ProgrammableAndroid(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        texture = item.getSkullTexture().orElse(null);
        registerDefaultFuelTypes();

        new BlockMenuPreset(getID(), "可编程式机器人") {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                boolean open = BlockStorage.getLocationInfo(b.getLocation(), "owner").equals(p.getUniqueId().toString()) || p.hasPermission("slimefun.android.bypass");

                if (!open) {
                    SlimefunPlugin.getLocal().sendMessage(p, "inventory.no-access", true);
                }

                return open;
            }

            @Override
            public void newInstance(BlockMenu menu, Block b) {
                menu.replaceExistingItem(15, new CustomItem(SlimefunUtils.getCustomHead("e01c7b5726178974b3b3a01b42a590e54366026fd43808f2a787648843a7f5a"), "&a启动/继续运行"));
                menu.addMenuClickHandler(15, (p, slot, item, action) -> {
                    SlimefunPlugin.getLocal().sendMessage(p, "android.started", true);
                    BlockStorage.addBlockInfo(b, "paused", "false");
                    p.closeInventory();
                    return false;
                });

                menu.replaceExistingItem(17, new CustomItem(SlimefunUtils.getCustomHead("16139fd1c5654e56e9e4e2c8be7eb2bd5b499d633616663feee99b74352ad64"), "&4暂停运行"));
                menu.addMenuClickHandler(17, (p, slot, item, action) -> {
                    BlockStorage.addBlockInfo(b, "paused", "true");
                    SlimefunPlugin.getLocal().sendMessage(p, "android.stopped", true);
                    return false;
                });

                menu.replaceExistingItem(16, new CustomItem(SlimefunUtils.getCustomHead("d78f2b7e5e75639ea7fb796c35d364c4df28b4243e66b76277aadcd6261337"), "&b内存核心", "", "&8\u21E8 &7单击打开脚本编辑器"));
                menu.addMenuClickHandler(16, (p, slot, item, action) -> {
                    BlockStorage.addBlockInfo(b, "paused", "true");
                    SlimefunPlugin.getLocal().sendMessage(p, "android.stopped", true);
                    openScriptEditor(p, b);
                    return false;
                });
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }
        };

        registerBlockHandler(getID(), new SlimefunBlockHandler() {

            @Override
            public void onPlace(Player p, Block b, SlimefunItem item) {
                BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
                BlockStorage.addBlockInfo(b, "script", "START-TURN_LEFT-REPEAT");
                BlockStorage.addBlockInfo(b, "index", "0");
                BlockStorage.addBlockInfo(b, "fuel", "0");
                BlockStorage.addBlockInfo(b, "rotation", p.getFacing().getOppositeFace().toString());
                BlockStorage.addBlockInfo(b, "paused", "true");
                b.setType(Material.PLAYER_HEAD);

                Rotatable blockData = (Rotatable) b.getBlockData();
                blockData.setRotation(p.getFacing());
                b.setBlockData(blockData);
            }

            @Override
            public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
                boolean allow = reason == UnregisterReason.PLAYER_BREAK && (BlockStorage.getLocationInfo(b.getLocation(), "owner").equals(p.getUniqueId().toString()) || p.hasPermission("slimefun.android.bypass"));

                if (allow) {
                    BlockMenu inv = BlockStorage.getInventory(b);

                    if (inv != null) {
                        if (inv.getItemInSlot(43) != null) {
                            b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(43));
                            inv.replaceExistingItem(43, null);
                        }

                        for (int slot : getOutputSlots()) {
                            if (inv.getItemInSlot(slot) != null) {
                                b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                                inv.replaceExistingItem(slot, null);
                            }
                        }
                    }
                }

                return allow;
            }
        });
    }

    /**
     * This returns the {@link AndroidType} that is associated with this {@link ProgrammableAndroid}.
     *
     * @return The type of this {@link ProgrammableAndroid}
     */
    public abstract AndroidType getAndroidType();

    @Override
    public void preRegister() {
        super.preRegister();

        addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem item, me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config data) {
                if (b != null) {
                    ProgrammableAndroid.this.tick(b);
                }
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        });
    }

    public void openScript(Player p, Block b, String script) {
        ChestMenu menu = new ChestMenu(ChatColor.DARK_AQUA + SlimefunPlugin.getLocal().getMessage(p, "android.scripts.editor"));

        menu.addItem(0, new CustomItem(ScriptAction.START.getItem(), SlimefunPlugin.getLocal().getMessage(p, "android.scripts.instructions.START"), "", "&7\u21E8 &e左键 &7返回机器人的控制面板"));
        menu.addMenuClickHandler(0, (pl, slot, item, action) -> {
            BlockStorage.getInventory(b).open(pl);
            return false;
        });

        String[] commands = PatternUtils.DASH.split(script);

        for (int i = 1; i < commands.length; i++) {
            int index = i;

            if (i == commands.length - 1) {
                int additional = commands.length == 54 ? 0 : 1;

                if (additional == 1) {
                    menu.addItem(i, new CustomItem(SlimefunUtils.getCustomHead("171d8979c1878a05987a7faf21b56d1b744f9d068c74cffcde1ea1edad5852"), "&7> 添加新命令"));
                    menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
                        openScriptComponentEditor(pl, b, script, index);
                        return false;
                    });
                }

                menu.addItem(i + additional, new CustomItem(ScriptAction.REPEAT.getItem(), SlimefunPlugin.getLocal().getMessage(p, "android.scripts.instructions.REPEAT"), "", "&7\u21E8 &e左键 &7返回机器人的控制面板"));
                menu.addMenuClickHandler(i + additional, (pl, slot, item, action) -> {
                    BlockStorage.getInventory(b).open(pl);
                    return false;
                });
            } else {
                ItemStack stack = ScriptAction.valueOf(commands[i]).getItem();
                menu.addItem(i, new CustomItem(stack, SlimefunPlugin.getLocal().getMessage(p, "android.scripts.instructions." + ScriptAction.valueOf(commands[i]).name()), "", "&7\u21E8 &e左键 &7编辑", "&7\u21E8 &e右键 &7删除", "&7\u21E8 &eShift + 右键 &7复制"));
                menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
                    if (action.isRightClicked() && action.isShiftClicked()) {
                        if (commands.length == 54) return false;

                        int j = 0;
                        StringBuilder builder = new StringBuilder(ScriptAction.START + "-");

                        for (String command : commands) {
                            if (j > 0) {
                                if (j == index) {
                                    builder.append(commands[j]).append('-').append(commands[j]).append('-');
                                } else if (j < commands.length - 1) builder.append(command).append('-');
                            }
                            j++;
                        }
                        builder.append(ScriptAction.REPEAT);
                        setScript(b.getLocation(), builder.toString());
                        openScript(pl, b, builder.toString());
                    } else if (action.isRightClicked()) {
                        int j = 0;
                        StringBuilder builder = new StringBuilder(ScriptAction.START + "-");

                        for (String command : commands) {
                            if (j != index && j > 0 && j < commands.length - 1) builder.append(command).append('-');
                            j++;
                        }

                        builder.append(ScriptAction.REPEAT);
                        setScript(b.getLocation(), builder.toString());

                        openScript(pl, b, builder.toString());
                    } else {
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

        int pages = (scripts.size() / 45) + 1;

        for (int i = 45; i < 54; i++) {
            menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
            menu.addMenuClickHandler(i, (pl, slot, item, action) -> false);
        }

        menu.addItem(46, new CustomItem(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&r\u21E6 上一页", "", "&7(" + page + " / " + pages + ")"));
        menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
            int next = page - 1;
            if (next < 1) next = pages;
            if (next != page) {
                openScriptDownloader(pl, b, next);
            }
            return false;
        });

        menu.addItem(48, new CustomItem(SlimefunUtils.getCustomHead("105a2cab8b68ea57e3af992a36e47c8ff9aa87cc8776281966f8c3cf31a38"), "&e上传脚本", "", "&6单击 &7将你正在用的脚本", "&7上传到服务器"));
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

        menu.addItem(53, new CustomItem(SlimefunUtils.getCustomHead("185c97dbb8353de652698d24b64327b793a3f32a98be67b719fbedab35e"), "&6> 返回", "", "&7返回机器人控制面板"));
        menu.addMenuClickHandler(53, (pl, slot, item, action) -> {
            openScriptEditor(pl, b);
            return false;
        });

        int index = 0;
        int categoryIndex = 45 * (page - 1);

        for (int i = 0; i < 45; i++) {
            int target = categoryIndex + i;

            if (target >= scripts.size()) {
                break;
            } else {
                Config script = scripts.get(target);

                OfflinePlayer op = Bukkit.getOfflinePlayer(script.getUUID("author"));
                String author = (op != null && op.getName() != null) ? op.getName() : script.getString("author_name");

                if (script.getString("author").equals(p.getUniqueId().toString())) {
                    menu.addItem(index, new CustomItem(this.getItem(), "&b" + script.getString("name"), "&7by &r" + author, "", "&7下载数: &r" + script.getInt("downloads"), "&7评分: " + getScriptRatingPercentage(script), "&a" + getScriptRating(script, true) + " \u263A &7| &4\u2639 " + getScriptRating(script, false), "", "&e左键 &r下载", "&4(这将会覆盖你正在使用的脚本)"));
                } else {
                    menu.addItem(index, new CustomItem(this.getItem(), "&b" + script.getString("name"), "&7by &r" + author, "", "&7下载数: &r" + script.getInt("downloads"), "&7评分: " + getScriptRatingPercentage(script), "&a" + getScriptRating(script, true) + " \u263A &7| &4\u2639 " + getScriptRating(script, false), "", "&e左键 &r下载", "&4(这将会覆盖你正在使用的脚本)", "&eShift + 右键 &r好评", "&eShift + 右键 &r差评"));
                }

                menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
                    Config script2 = new Config(script.getFile());

                    if (action.isShiftClicked()) {
                        if (script2.getString("author").equals(pl.getUniqueId().toString())) {
                            SlimefunPlugin.getLocal().sendMessage(pl, "android.scripts.rating.own", true);
                        } else if (action.isRightClicked()) {
                            if (!script2.getStringList("rating.negative").contains(pl.getUniqueId().toString()) && !script2.getStringList("rating.positive").contains(pl.getUniqueId().toString())) {
                                List<String> list = script2.getStringList("rating.negative");
                                list.add(p.getUniqueId().toString());

                                script2.setValue("rating.negative", list);
                                script2.save();

                                openScriptDownloader(pl, b, page);
                            } else {
                                SlimefunPlugin.getLocal().sendMessage(pl, "android.scripts.rating.already", true);
                            }
                        } else {
                            if (!script2.getStringList("rating.negative").contains(pl.getUniqueId().toString()) && !script2.getStringList("rating.positive").contains(pl.getUniqueId().toString())) {
                                List<String> list = script2.getStringList("rating.positive");
                                list.add(pl.getUniqueId().toString());

                                script2.setValue("rating.positive", list);
                                script2.save();

                                openScriptDownloader(pl, b, page);
                            } else {
                                SlimefunPlugin.getLocal().sendMessage(pl, "android.scripts.rating.already", true);
                            }
                        }
                    } else if (!action.isRightClicked()) {
                        script2.setValue("downloads", script2.getInt("downloads") + 1);
                        script2.save();

                        setScript(b.getLocation(), script2.getString("code"));
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
        String code = getScript(b.getLocation());
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

        menu.addItem(1, new CustomItem(SlimefunUtils.getCustomHead("d9bf6db4aeda9d8822b9f736538e8c18b9a4844f84eb45504adfbfee87eb"), "&2> 编辑脚本", "", "&a修改你现有的脚本"));
        menu.addMenuClickHandler(1, (pl, slot, item, action) -> {
            openScript(pl, b, getScript(b.getLocation()));
            return false;
        });

        menu.addItem(3, new CustomItem(SlimefunUtils.getCustomHead("171d8979c1878a05987a7faf21b56d1b744f9d068c74cffcde1ea1edad5852"), "&4> 创建新脚本", "", "&c删除你正在使用的脚本", "&c并创建一个全新的空白脚本"));
        menu.addMenuClickHandler(3, (pl, slot, item, action) -> {
            openScript(pl, b, "START-TURN_LEFT-REPEAT");
            return false;
        });

        menu.addItem(5, new CustomItem(SlimefunUtils.getCustomHead("c01586e39f6ffa63b4fb301b65ca7da8a92f7353aaab89d3886579125dfbaf9"), "&6> 下载脚本", "", "&e从服务器中下载其他玩家上传的脚本", "&e可以即下即用, 或者修改之后再使用"));
        menu.addMenuClickHandler(5, (pl, slot, item, action) -> {
            openScriptDownloader(pl, b, 1);
            return false;
        });

        menu.addItem(8, new CustomItem(SlimefunUtils.getCustomHead("a185c97dbb8353de652698d24b64327b793a3f32a98be67b719fbedab35e"), "&6> 返回", "", "&7返回机器人控制面板"));
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

    protected List<ScriptAction> getAccessibleScriptParts() {
        List<ScriptAction> list = new ArrayList<>();

        for (ScriptAction part : ScriptAction.values()) {
            if (part != ScriptAction.START && part != ScriptAction.REPEAT && getAndroidType().isType(part.getRequiredType())) {
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

        menu.addItem(9, new CustomItem(SlimefunUtils.getCustomHead("16139fd1c5654e56e9e4e2c8be7eb2bd5b499d633616663feee99b74352ad64"), "&rDo nothing"), (pl, slot, item, action) -> {
            int i = 0;
            StringBuilder builder = new StringBuilder("START-");

            for (String command : commands) {
                if (i != index && i > 0 && i < commands.length - 1) builder.append(command).append('-');
                i++;
            }

            builder.append("REPEAT");
            setScript(b.getLocation(), builder.toString());

            openScript(p, b, builder.toString());
            return false;
        });

        int i = 10;
        for (ScriptAction part : getAccessibleScriptParts()) {
            menu.addItem(i, new CustomItem(part.getItem(), SlimefunPlugin.getLocal().getMessage(p, "android.scripts.instructions." + part.name())), (pl, slot, item, action) -> {
                addInstruction(pl, b, index, part, commands);
                return false;
            });
            i++;
        }

        menu.open(p);
    }

    private void addInstruction(Player p, Block b, int index, ScriptAction part, String[] commands) {
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
        setScript(b.getLocation(), builder.toString());

        openScript(p, b, builder.toString());
    }

    protected String getScript(Location l) {
        return BlockStorage.getLocationInfo(l, "script");
    }

    protected void setScript(Location l, String script) {
        BlockStorage.addBlockInfo(l, "script", script);
    }

    private void registerDefaultFuelTypes() {
        if (getTier() == 1) {
            registerFuelType(new MachineFuel(800, new ItemStack(Material.COAL_BLOCK)));
            registerFuelType(new MachineFuel(45, new ItemStack(Material.BLAZE_ROD)));

            // Coal & Charcoal
            registerFuelType(new MachineFuel(8, new ItemStack(Material.COAL)));
            registerFuelType(new MachineFuel(8, new ItemStack(Material.CHARCOAL)));

            // Logs
            for (Material mat : Tag.LOGS.getValues()) {
                registerFuelType(new MachineFuel(2, new ItemStack(mat)));
            }

            // Wooden Planks
            for (Material mat : Tag.PLANKS.getValues()) {
                registerFuelType(new MachineFuel(1, new ItemStack(mat)));
            }
        }
        else if (getTier() == 2) {
            registerFuelType(new MachineFuel(100, new ItemStack(Material.LAVA_BUCKET)));
            registerFuelType(new MachineFuel(200, SlimefunItems.BUCKET_OF_OIL));
            registerFuelType(new MachineFuel(500, SlimefunItems.BUCKET_OF_FUEL));
        }
        else {
            registerFuelType(new MachineFuel(2500, SlimefunItems.URANIUM));
            registerFuelType(new MachineFuel(1200, SlimefunItems.NEPTUNIUM));
            registerFuelType(new MachineFuel(3000, SlimefunItems.BOOSTED_URANIUM));
        }
    }

    @Override
    public String getLabelLocalPath() {
        return "guide.tooltips.recipes.generator";
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> list = new ArrayList<>();

        for (MachineFuel fuel : fuelTypes) {
            ItemStack item = fuel.getInput().clone();
            ItemMeta im = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColors.color("&8\u21E8 &7剩余 " + NumberUtils.getTimeLeft(fuel.getTicks() / 2)));
            im.setLore(lore);
            item.setItemMeta(im);
            list.add(item);
        }

        return list;
    }

    @Override
    public int[] getInputSlots() {
        return new int[0];
    }

    @Override
    public int[] getOutputSlots() {
        return new int[]{20, 21, 22, 29, 30, 31};
    }

    public abstract float getFuelEfficiency();

    public abstract int getTier();

    protected void tick(Block b) {
        if (b.getType() != Material.PLAYER_HEAD) {
            // The Android was destroyed or moved.
            return;
        }

        if ("false".equals(BlockStorage.getLocationInfo(b.getLocation(), "paused"))) {
            BlockMenu menu = BlockStorage.getInventory(b);
            float fuel = Float.parseFloat(BlockStorage.getLocationInfo(b.getLocation(), "fuel"));

            if (fuel < 0.001) {
                consumeFuel(b, menu);
            } else {
                String[] script = PatternUtils.DASH.split(BlockStorage.getLocationInfo(b.getLocation(), "script"));

                int index = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "index")) + 1;
                if (index >= script.length) index = 0;

                boolean refresh = true;
                BlockStorage.addBlockInfo(b, "fuel", String.valueOf(fuel - 1));
                ScriptAction part = ScriptAction.valueOf(script[index]);

                if (getAndroidType().isType(part.getRequiredType())) {
                    BlockFace face = BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"));
                    double damage = getTier() >= 3 ? 20D : 4D * getTier();

                    switch (part) {
                        case GO_DOWN:
                            move(b, face, b.getRelative(BlockFace.DOWN));
                            break;
                        case GO_FORWARD:
                            move(b, face, b.getRelative(face));
                            break;
                        case GO_UP:
                            move(b, face, b.getRelative(BlockFace.UP));
                            break;
                        case REPEAT:
                            BlockStorage.addBlockInfo(b, "index", String.valueOf(0));
                            break;
                        case TURN_LEFT:
                            int indexLeft = POSSIBLE_ROTATIONS.indexOf(BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"))) - 1;
                            if (indexLeft < 0) indexLeft = POSSIBLE_ROTATIONS.size() - 1;

                            Rotatable rotatableLeft = (Rotatable) b.getBlockData();
                            rotatableLeft.setRotation(POSSIBLE_ROTATIONS.get(indexLeft));
                            b.setBlockData(rotatableLeft);
                            BlockStorage.addBlockInfo(b, "rotation", POSSIBLE_ROTATIONS.get(indexLeft).toString());

                            break;
                        case TURN_RIGHT:
                            int indexRight = POSSIBLE_ROTATIONS.indexOf(BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"))) + 1;
                            if (indexRight == POSSIBLE_ROTATIONS.size()) indexRight = 0;

                            Rotatable rotatableRight = (Rotatable) b.getBlockData();
                            rotatableRight.setRotation(POSSIBLE_ROTATIONS.get(indexRight));
                            b.setBlockData(rotatableRight);
                            BlockStorage.addBlockInfo(b, "rotation", POSSIBLE_ROTATIONS.get(indexRight).toString());

                            break;
                        case DIG_FORWARD:
                            mine(b, menu, b.getRelative(face));
                            break;
                        case DIG_UP:
                            mine(b, menu, b.getRelative(BlockFace.UP));
                            break;
                        case DIG_DOWN:
                            mine(b, menu, b.getRelative(BlockFace.DOWN));
                            break;
                        case CATCH_FISH:
                            fish(b, menu);
                            break;
                        case MOVE_AND_DIG_FORWARD:
                            movedig(b, menu, face, b.getRelative(face));
                            break;
                        case MOVE_AND_DIG_UP:
                            movedig(b, menu, face, b.getRelative(BlockFace.UP));
                            break;
                        case MOVE_AND_DIG_DOWN:
                            movedig(b, menu, face, b.getRelative(BlockFace.DOWN));
                            break;
                        case INTERFACE_ITEMS:
                            depositItems(menu, b.getRelative(face));
                            break;
                        case INTERFACE_FUEL:
                            refuel(menu, b.getRelative(face));
                            break;
                        case FARM_FORWARD:
                            farm(menu, b.getRelative(face));
                            break;
                        case FARM_DOWN:
                            farm(menu, b.getRelative(BlockFace.DOWN));
                            break;
                        case FARM_EXOTIC_FORWARD:
                            exoticFarm(menu, b.getRelative(face));
                            break;
                        case FARM_EXOTIC_DOWN:
                            exoticFarm(menu, b.getRelative(BlockFace.DOWN));
                            break;
                        case CHOP_TREE:
                            refresh = chopTree(b, menu, face);
                            break;
                        case ATTACK_MOBS_ANIMALS:
                            killEntities(b, damage, e -> true);
                            break;
                        case ATTACK_MOBS:
                            killEntities(b, damage, e -> e instanceof Monster);
                            break;
                        case ATTACK_ANIMALS:
                            killEntities(b, damage, e -> e instanceof Animals);
                            break;
                        case ATTACK_ANIMALS_ADULT:
                            killEntities(b, damage, e -> e instanceof Animals && e instanceof org.bukkit.entity.Ageable && ((org.bukkit.entity.Ageable) e).isAdult());
                            break;
                        default:
                            break;
                    }
                }
                if (refresh) {
                    BlockStorage.addBlockInfo(b, "index", String.valueOf(index));
                }
            }
        }
    }

    private void depositItems(BlockMenu menu, Block facedBlock) {
        if (facedBlock.getType() == Material.DISPENSER && BlockStorage.check(facedBlock, "ANDROID_INTERFACE_ITEMS")) {
            Dispenser d = (Dispenser) facedBlock.getState();

            for (int slot : getOutputSlots()) {
                ItemStack stack = menu.getItemInSlot(slot);

                if (stack != null) {
                    Optional<ItemStack> optional = d.getInventory().addItem(stack).values().stream().findFirst();

                    if (optional.isPresent()) {
                        menu.replaceExistingItem(slot, optional.get());
                    } else {
                        menu.replaceExistingItem(slot, null);
                    }
                }
            }
        }
    }

    private void consumeFuel(Block b, BlockMenu menu) {
        ItemStack item = menu.getItemInSlot(43);

        if (item != null) {
            for (MachineFuel fuel : fuelTypes) {
                if (fuel.test(item)) {
                    menu.consumeItem(43);

                    if (getTier() == 2) {
                        menu.pushItem(new ItemStack(Material.BUCKET), getOutputSlots());
                    }

                    BlockStorage.addBlockInfo(b, "fuel", String.valueOf((int) (fuel.getTicks() * this.getFuelEfficiency())));
                    break;
                }
            }
        }
    }

    private void refuel(BlockMenu menu, Block facedBlock) {
        if (facedBlock.getType() == Material.DISPENSER && BlockStorage.check(facedBlock, "ANDROID_INTERFACE_FUEL")) {
            Dispenser d = (Dispenser) facedBlock.getState();

            for (int slot = 0; slot < 9; slot++) {
                ItemStack item = d.getInventory().getItem(slot);

                if (item != null) {
                    insertFuel(menu, d.getInventory(), slot, menu.getItemInSlot(43), item);
                }
            }
        }
    }

    private boolean insertFuel(BlockMenu menu, Inventory dispenser, int slot, ItemStack currentFuel, ItemStack newFuel) {
        if (currentFuel == null) {
            menu.replaceExistingItem(43, newFuel);
            dispenser.setItem(slot, null);
            return true;
        } else if (SlimefunUtils.isItemSimilar(newFuel, currentFuel, true)) {
            int rest = newFuel.getType().getMaxStackSize() - currentFuel.getAmount();

            if (rest > 0) {
                int amount = newFuel.getAmount() > rest ? rest : newFuel.getAmount();
                menu.replaceExistingItem(43, new CustomItem(newFuel, currentFuel.getAmount() + amount));
                ItemUtils.consumeItem(newFuel, amount, false);
            }

            return true;
        }

        return false;
    }

    private void constructMenu(BlockMenuPreset preset) {
        for (int i : BORDER) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "), ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : OUTPUT_BORDER) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), " "), ChestMenuUtils.getEmptyClickHandler());
        }

        for (int i : getOutputSlots()) {
            preset.addMenuClickHandler(i, new AdvancedMenuClickHandler() {

                @Override
                public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                    return false;
                }

                @Override
                public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
                    return cursor == null || cursor.getType() == null || cursor.getType() == Material.AIR;
                }
            });
        }

        ItemStack generator = SlimefunUtils.getCustomHead("9343ce58da54c79924a2c9331cfc417fe8ccbbea9be45a7ac85860a6c730");

        if (getTier() == 1) {
            preset.addItem(34, new CustomItem(generator, "&8\u21E9 &c燃料输入口 &8\u21E9", "", "&r这类机器人需要固态燃料", "&r例如煤, 原木等..."), ChestMenuUtils.getEmptyClickHandler());
        }
        else if (getTier() == 2) {
            preset.addItem(34, new CustomItem(generator, "&8\u21E9 &c燃料输入口 &8\u21E9", "", "&r这类机器人需要液态燃料", "&r例如岩浆, 原油, 燃油等..."), ChestMenuUtils.getEmptyClickHandler());
        }
        else {
            preset.addItem(34, new CustomItem(generator, "&8\u21E9 &c燃料输入口 &8\u21E9", "", "&r这类机器人需要放射性燃料", "&r例如铀, 镎或强化铀"), ChestMenuUtils.getEmptyClickHandler());
        }
    }

    public void addItems(Block b, ItemStack... items) {
        BlockMenu inv = BlockStorage.getInventory(b);

        for (ItemStack item : items) {
            inv.pushItem(item, getOutputSlots());
        }
    }

    public void registerFuelType(MachineFuel fuel) {
        fuelTypes.add(fuel);
    }

    protected void move(Block b, BlockFace face, Block block) {
        Player p = Bukkit.getPlayer(ProtectionChecker.getOwnerByJson(BlockStorage.getBlockInfoAsJson(b.getLocation())));

        if (p != null && !ProtectionChecker.canInteract(p, block, ProtectionChecker.InteractType.MOVE)) {
            BlockStorage.addBlockInfo(b, "paused", "false");
            SlimefunPlugin.getLocal().sendMessage(p, "messages.android-no-permission", true);
            return;
        }

        if (block.getY() > 0 && block.getY() < block.getWorld().getMaxHeight() && (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR)) {
            block.setType(Material.PLAYER_HEAD);
            Rotatable blockData = (Rotatable) block.getBlockData();
            blockData.setRotation(face.getOppositeFace());
            block.setBlockData(blockData);

            SkullBlock.setFromBase64(block, texture);

            b.setType(Material.AIR);
            BlockStorage.moveBlockInfo(b.getLocation(), block.getLocation());
        }
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

}