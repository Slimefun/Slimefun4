package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.chat.ChatInput;
import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.skull.SkullBlock;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

public abstract class ProgrammableAndroid extends SlimefunItem implements InventoryBlock, RecipeDisplayItem {

    private static final List<BlockFace> POSSIBLE_ROTATIONS = Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
    private static final int[] BORDER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 18, 24, 25, 26, 27, 33, 35, 36, 42, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };
    private static final int[] OUTPUT_BORDER = { 10, 11, 12, 13, 14, 19, 23, 28, 32, 37, 38, 39, 40, 41 };
    private static final String DEFAULT_SCRIPT = "START-TURN_LEFT-REPEAT";

    protected final Set<MachineFuel> fuelTypes = new HashSet<>();
    protected final String texture;

    public ProgrammableAndroid(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        texture = item.getSkullTexture().orElse(null);
        registerDefaultFuelTypes();

        new BlockMenuPreset(getID(), "Programmable Android") {

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
                menu.replaceExistingItem(15, new CustomItem(SlimefunUtils.getCustomHead("e01c7b5726178974b3b3a01b42a590e54366026fd43808f2a787648843a7f5a"), "&aStart/Continue"));
                menu.addMenuClickHandler(15, (p, slot, item, action) -> {
                    SlimefunPlugin.getLocal().sendMessage(p, "android.started", true);
                    BlockStorage.addBlockInfo(b, "paused", "false");
                    p.closeInventory();
                    return false;
                });

                menu.replaceExistingItem(17, new CustomItem(SlimefunUtils.getCustomHead("16139fd1c5654e56e9e4e2c8be7eb2bd5b499d633616663feee99b74352ad64"), "&4Pause"));
                menu.addMenuClickHandler(17, (p, slot, item, action) -> {
                    BlockStorage.addBlockInfo(b, "paused", "true");
                    SlimefunPlugin.getLocal().sendMessage(p, "android.stopped", true);
                    return false;
                });

                menu.replaceExistingItem(16, new CustomItem(SlimefunUtils.getCustomHead("d78f2b7e5e75639ea7fb796c35d364c4df28b4243e66b76277aadcd6261337"), "&bMemory Core", "", "&8\u21E8 &7Click to open the Script Editor"));
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
                BlockStorage.addBlockInfo(b, "script", DEFAULT_SCRIPT);
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
            public void tick(Block b, SlimefunItem item, Config data) {
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

        menu.addItem(0, new CustomItem(Instruction.START.getItem(), SlimefunPlugin.getLocal().getMessage(p, "android.scripts.instructions.START"), "", "&7\u21E8 &eLeft Click &7to return to the Android's interface"));
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
                    menu.addItem(i, new CustomItem(SlimefunUtils.getCustomHead("171d8979c1878a05987a7faf21b56d1b744f9d068c74cffcde1ea1edad5852"), "&7> Add new Command"));
                    menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
                        openScriptComponentEditor(pl, b, script, index);
                        return false;
                    });
                }

                menu.addItem(i + additional, new CustomItem(Instruction.REPEAT.getItem(), SlimefunPlugin.getLocal().getMessage(p, "android.scripts.instructions.REPEAT"), "", "&7\u21E8 &eLeft Click &7to return to the Android's interface"));
                menu.addMenuClickHandler(i + additional, (pl, slot, item, action) -> {
                    BlockStorage.getInventory(b).open(pl);
                    return false;
                });
            }
            else {
                ItemStack stack = Instruction.valueOf(commands[i]).getItem();
                menu.addItem(i, new CustomItem(stack, SlimefunPlugin.getLocal().getMessage(p, "android.scripts.instructions." + Instruction.valueOf(commands[i]).name()), "", "&7\u21E8 &eLeft Click &7to edit", "&7\u21E8 &eRight Click &7to delete", "&7\u21E8 &eShift + Right Click &7to duplicate"));
                menu.addMenuClickHandler(i, (pl, slot, item, action) -> {
                    if (action.isRightClicked() && action.isShiftClicked()) {
                        if (commands.length == 54) return false;

                        int j = 0;
                        StringBuilder builder = new StringBuilder(Instruction.START + "-");

                        for (String command : commands) {
                            if (j > 0) {
                                if (j == index) {
                                    builder.append(commands[j]).append('-').append(commands[j]).append('-');
                                }
                                else if (j < commands.length - 1) {
                                    builder.append(command).append('-');
                                }
                            }
                            j++;
                        }
                        builder.append(Instruction.REPEAT);
                        setScript(b.getLocation(), builder.toString());
                        openScript(pl, b, builder.toString());
                    }
                    else if (action.isRightClicked()) {
                        int j = 0;
                        StringBuilder builder = new StringBuilder(Instruction.START + "-");

                        for (String command : commands) {
                            if (j != index && j > 0 && j < commands.length - 1) builder.append(command).append('-');
                            j++;
                        }

                        builder.append(Instruction.REPEAT);
                        setScript(b.getLocation(), builder.toString());

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

        List<Script> scripts = Script.getUploadedScripts(getAndroidType());
        int pages = (scripts.size() / 45) + 1;

        for (int i = 45; i < 54; i++) {
            menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
            menu.addMenuClickHandler(i, (pl, slot, item, action) -> false);
        }

        menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page, pages));
        menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
            int next = page - 1;
            if (next < 1) {
                next = pages;
            }
            if (next != page) {
                openScriptDownloader(pl, b, next);
            }
            return false;
        });

        menu.addItem(48, new CustomItem(SlimefunUtils.getCustomHead("105a2cab8b68ea57e3af992a36e47c8ff9aa87cc8776281966f8c3cf31a38"), "&eUpload a Script", "", "&6Click &7to upload your Android's Script", "&7to the Server's database"));
        menu.addMenuClickHandler(48, (pl, slot, item, action) -> {
            uploadScript(pl, b, page);
            return false;
        });

        menu.addItem(50, ChestMenuUtils.getNextButton(p, page, pages));
        menu.addMenuClickHandler(50, (pl, slot, item, action) -> {
            int next = page + 1;
            if (next > pages) {
                next = 1;
            }
            if (next != page) {
                openScriptDownloader(pl, b, next);
            }
            return false;
        });

        menu.addItem(53, new CustomItem(SlimefunUtils.getCustomHead("185c97dbb8353de652698d24b64327b793a3f32a98be67b719fbedab35e"), "&6> Back", "", "&7Return to the Android's interface"));
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
            }
            else {
                Script script = scripts.get(target);
                List<String> lore = new LinkedList<>();
                lore.add("&7by &r" + script.getAuthor());
                lore.add("");
                lore.add("&7Downloads: &r" + script.getDownloads());
                lore.add("&7Rating: " + getScriptRatingPercentage(script));
                lore.add("&a" + script.getUpvotes() + " \u263A &7| &4\u2639 " + script.getDownvotes());
                lore.add("");
                lore.add("&eLeft Click &rto download this Script");
                lore.add("&4(This will override your current Script)");

                if (script.canRate(p)) {
                    lore.add("&eShift + Left Click &rto leave a positive Rating");
                    lore.add("&eShift + Right Click &rto leave a negative Rating");
                }

                ItemStack item = new CustomItem(getItem(), "&b" + script.getName(), lore.toArray(new String[0]));
                menu.addItem(index, item, (player, slot, stack, action) -> {
                    if (action.isShiftClicked()) {
                        if (script.isAuthor(player)) {
                            SlimefunPlugin.getLocal().sendMessage(player, "android.scripts.rating.own", true);
                        }
                        else if (script.canRate(player)) {
                            script.rate(player, !action.isRightClicked());
                            openScriptDownloader(player, b, page);
                        }
                        else {
                            SlimefunPlugin.getLocal().sendMessage(player, "android.scripts.rating.already", true);
                        }
                    }
                    else if (!action.isRightClicked()) {
                        script.download();
                        setScript(b.getLocation(), script.getSourceCode());
                        openScriptEditor(player, b);
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

        if (code == null) {
            return;
        }

        int nextId = 1;

        for (Script script : Script.getUploadedScripts(getAndroidType())) {
            if (script.isAuthor(p)) {
                nextId++;
            }

            if (script.getSourceCode().equals(code)) {
                SlimefunPlugin.getLocal().sendMessage(p, "android.scripts.already-uploaded", true);
                return;
            }
        }

        p.closeInventory();
        SlimefunPlugin.getLocal().sendMessages(p, "android.scripts.enter-name");
        int id = nextId;

        ChatInput.waitForPlayer(SlimefunPlugin.instance, p, msg -> {
            Script.upload(p, getAndroidType(), id, msg, code);
            SlimefunPlugin.getLocal().sendMessages(p, "android.scripts.uploaded");
            openScriptDownloader(p, b, page);
        });
    }

    public void openScriptEditor(Player p, Block b) {
        ChestMenu menu = new ChestMenu(ChatColor.DARK_AQUA + SlimefunPlugin.getLocal().getMessage(p, "android.scripts.editor"));

        menu.addItem(1, new CustomItem(SlimefunUtils.getCustomHead("d9bf6db4aeda9d8822b9f736538e8c18b9a4844f84eb45504adfbfee87eb"), "&2> Edit Script", "", "&aEdits your current Script"));
        menu.addMenuClickHandler(1, (pl, slot, item, action) -> {
            openScript(pl, b, getScript(b.getLocation()));
            return false;
        });

        menu.addItem(3, new CustomItem(SlimefunUtils.getCustomHead("171d8979c1878a05987a7faf21b56d1b744f9d068c74cffcde1ea1edad5852"), "&4> Create new Script", "", "&cDeletes your current Script", "&cand creates a blank one"));
        menu.addMenuClickHandler(3, (pl, slot, item, action) -> {
            openScript(pl, b, DEFAULT_SCRIPT);
            return false;
        });

        menu.addItem(5, new CustomItem(SlimefunUtils.getCustomHead("c01586e39f6ffa63b4fb301b65ca7da8a92f7353aaab89d3886579125dfbaf9"), "&6> Download a Script", "", "&eDownload a Script from the Server", "&eYou can edit or simply use it"));
        menu.addMenuClickHandler(5, (pl, slot, item, action) -> {
            openScriptDownloader(pl, b, 1);
            return false;
        });

        menu.addItem(8, new CustomItem(SlimefunUtils.getCustomHead("a185c97dbb8353de652698d24b64327b793a3f32a98be67b719fbedab35e"), "&6> Back", "", "&7Return to the Android's interface"));
        menu.addMenuClickHandler(8, (pl, slot, item, action) -> {
            BlockStorage.getInventory(b).open(p);
            return false;
        });

        menu.open(p);
    }

    protected List<Instruction> getValidScriptInstructions() {
        List<Instruction> list = new ArrayList<>();

        for (Instruction part : Instruction.values()) {
            if (part == Instruction.START || part == Instruction.REPEAT) {
                continue;
            }

            if (getAndroidType().isType(part.getRequiredType())) {
                list.add(part);
            }
        }

        return list;
    }

    protected String getScriptRatingPercentage(Script script) {
        float percentage = script.getRating();
        return NumberUtils.getColorFromPercentage(percentage) + String.valueOf(percentage) + ChatColor.RESET + "% ";
    }

    protected void openScriptComponentEditor(Player p, Block b, String script, int index) {
        ChestMenu menu = new ChestMenu(ChatColor.DARK_AQUA + SlimefunPlugin.getLocal().getMessage(p, "android.scripts.editor"));
        String[] commands = PatternUtils.DASH.split(script);

        ChestMenuUtils.drawBackground(menu, 0, 1, 2, 3, 4, 5, 6, 7, 8);

        menu.addItem(9, new CustomItem(SlimefunUtils.getCustomHead("16139fd1c5654e56e9e4e2c8be7eb2bd5b499d633616663feee99b74352ad64"), "&rDo nothing"), (pl, slot, item, action) -> {
            int i = 0;
            StringBuilder builder = new StringBuilder("START-");

            for (String command : commands) {
                if (i != index && i > 0 && i < commands.length - 1) {
                    builder.append(command).append('-');
                }

                i++;
            }

            builder.append("REPEAT");
            setScript(b.getLocation(), builder.toString());

            openScript(p, b, builder.toString());
            return false;
        });

        int i = 10;
        for (Instruction part : getValidScriptInstructions()) {
            menu.addItem(i, new CustomItem(part.getItem(), SlimefunPlugin.getLocal().getMessage(p, "android.scripts.instructions." + part.name())), (pl, slot, item, action) -> {
                addInstruction(pl, b, index, part, commands);
                return false;
            });
            i++;
        }

        menu.open(p);
    }

    private void addInstruction(Player p, Block b, int index, Instruction part, String[] commands) {
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
        String script = BlockStorage.getLocationInfo(l, "script");
        return script != null ? script : DEFAULT_SCRIPT;
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

    public void registerFuelType(MachineFuel fuel) {
        Validate.notNull(fuel, "Cannot register null as a Fuel type");
        fuelTypes.add(fuel);
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
            lore.add(ChatColors.color("&8\u21E8 &7Lasts " + NumberUtils.getTimeLeft(fuel.getTicks() / 2)));
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
        return new int[] { 20, 21, 22, 29, 30, 31 };
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
            }
            else {
                String[] script = PatternUtils.DASH.split(BlockStorage.getLocationInfo(b.getLocation(), "script"));

                int index = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "index")) + 1;
                if (index >= script.length) {
                    index = 0;
                }

                boolean refresh = true;
                BlockStorage.addBlockInfo(b, "fuel", String.valueOf(fuel - 1));
                Instruction instruction = Instruction.valueOf(script[index]);

                if (getAndroidType().isType(instruction.getRequiredType())) {
                    BlockFace face = BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"));

                    switch (instruction) {
                    case REPEAT:
                        BlockStorage.addBlockInfo(b, "index", String.valueOf(0));
                        break;
                    case CHOP_TREE:
                        refresh = chopTree(b, menu, face);
                        break;
                    default:
                        instruction.execute(this, b, menu, face);
                        break;
                    }
                }

                if (refresh) {
                    BlockStorage.addBlockInfo(b, "index", String.valueOf(index));
                }
            }
        }
    }

    protected void rotate(Block b, int mod) {
        BlockFace current = BlockFace.valueOf(BlockStorage.getLocationInfo(b.getLocation(), "rotation"));
        int index = POSSIBLE_ROTATIONS.indexOf(current) + mod;

        if (index == POSSIBLE_ROTATIONS.size()) {
            index = 0;
        }
        else if (index < 0) {
            index = POSSIBLE_ROTATIONS.size() - 1;
        }

        BlockFace rotation = POSSIBLE_ROTATIONS.get(index);

        Rotatable rotatatable = (Rotatable) b.getBlockData();
        rotatatable.setRotation(rotation);
        b.setBlockData(rotatatable);
        BlockStorage.addBlockInfo(b, "rotation", rotation.name());
    }

    protected void depositItems(BlockMenu menu, Block facedBlock) {
        if (facedBlock.getType() == Material.DISPENSER && BlockStorage.check(facedBlock, "ANDROID_INTERFACE_ITEMS")) {
            Dispenser d = (Dispenser) facedBlock.getState();

            for (int slot : getOutputSlots()) {
                ItemStack stack = menu.getItemInSlot(slot);

                if (stack != null) {
                    Optional<ItemStack> optional = d.getInventory().addItem(stack).values().stream().findFirst();

                    if (optional.isPresent()) {
                        menu.replaceExistingItem(slot, optional.get());
                    }
                    else {
                        menu.replaceExistingItem(slot, null);
                    }
                }
            }
        }
    }

    protected void refuel(BlockMenu menu, Block facedBlock) {
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
        }
        else if (SlimefunUtils.isItemSimilar(newFuel, currentFuel, true)) {
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
            preset.addItem(34, new CustomItem(generator, "&8\u21E9 &cFuel Input &8\u21E9", "", "&rThis Android runs on solid Fuel", "&re.g. Coal, Wood, etc..."), ChestMenuUtils.getEmptyClickHandler());
        }
        else if (getTier() == 2) {
            preset.addItem(34, new CustomItem(generator, "&8\u21E9 &cFuel Input &8\u21E9", "", "&rThis Android runs on liquid Fuel", "&re.g. Lava, Oil, Fuel, etc..."), ChestMenuUtils.getEmptyClickHandler());
        }
        else {
            preset.addItem(34, new CustomItem(generator, "&8\u21E9 &cFuel Input &8\u21E9", "", "&rThis Android runs on radioactive Fuel", "&re.g. Uranium, Neptunium or Boosted Uranium"), ChestMenuUtils.getEmptyClickHandler());
        }
    }

    public void addItems(Block b, ItemStack... items) {
        BlockMenu inv = BlockStorage.getInventory(b);

        for (ItemStack item : items) {
            inv.pushItem(item, getOutputSlots());
        }
    }

    protected void move(Block b, BlockFace face, Block block) {
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

    protected void attack(Block b, Predicate<LivingEntity> predicate) {
        throw new UnsupportedOperationException("Non-butcher Android tried to butcher!");
    }

    protected void fish(Block b, BlockMenu menu) {
        throw new UnsupportedOperationException("Non-fishing Android tried to fish!");
    }

    protected void dig(Block b, BlockMenu menu, Block block) {
        throw new UnsupportedOperationException("Non-mining Android tried to mine!");
    }

    protected void moveAndDig(Block b, BlockMenu menu, BlockFace face, Block block) {
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
