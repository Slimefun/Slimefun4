package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.miner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link IndustrialMiner} is a {@link MultiBlockMachine} that can mine any
 * ores it finds in a given range underneath where it was placed.
 * 
 * <i>And for those of you who are wondering... yes this is the replacement for the
 * long-time deprecated Digital Miner.</i>
 * 
 * @author TheBusyBiscuit
 * 
 * @see AdvancedIndustrialMiner
 * @see ActiveMiner
 *
 */
public class IndustrialMiner extends MultiBlockMachine {

    protected final Map<Location, ActiveMiner> activeMiners = new HashMap<>();
    protected final List<MachineFuel> fuelTypes = new ArrayList<>();

    private final int range;
    private final boolean silkTouch;
    private final ItemSetting<Boolean> canMineAncientDebris = new ItemSetting<>("can-mine-ancient-debris", false);

    public IndustrialMiner(Category category, SlimefunItemStack item, Material baseMaterial, boolean silkTouch, int range) {
        super(category, item, new ItemStack[] { null, null, null, new CustomItem(Material.PISTON, "Piston (facing up)"), new ItemStack(Material.CHEST), new CustomItem(Material.PISTON, "Piston (facing up)"), new ItemStack(baseMaterial), new ItemStack(Material.BLAST_FURNACE), new ItemStack(baseMaterial) }, BlockFace.UP);

        this.range = range;
        this.silkTouch = silkTouch;

        registerDefaultFuelTypes();
        addItemSetting(canMineAncientDebris);
    }

    /**
     * This returns whether this {@link IndustrialMiner} will output ores as they are.
     * Similar to the Silk Touch {@link Enchantment}.
     * 
     * @return Whether to treat ores with Silk Touch
     */
    public boolean hasSilkTouch() {
        return silkTouch;
    }

    /**
     * This method returns the range of the {@link IndustrialMiner}.
     * The total area will be determined by the range multiplied by 2 plus the actual center
     * of the machine.
     * 
     * So a range of 3 will make the {@link IndustrialMiner} affect an area of 7x7 blocks.
     * 3 on all axis, plus the center of the machine itself.
     * 
     * @return The range of this {@link IndustrialMiner}
     */
    public int getRange() {
        return range;
    }

    /**
     * This registers the various types of fuel that can be used in the
     * {@link IndustrialMiner}.
     */
    protected void registerDefaultFuelTypes() {
        // Coal & Charcoal
        fuelTypes.add(new MachineFuel(4, new ItemStack(Material.COAL)));
        fuelTypes.add(new MachineFuel(4, new ItemStack(Material.CHARCOAL)));

        fuelTypes.add(new MachineFuel(40, new ItemStack(Material.COAL_BLOCK)));
        fuelTypes.add(new MachineFuel(10, new ItemStack(Material.DRIED_KELP_BLOCK)));
        fuelTypes.add(new MachineFuel(4, new ItemStack(Material.BLAZE_ROD)));

        // Logs
        for (Material mat : Tag.LOGS.getValues()) {
            fuelTypes.add(new MachineFuel(1, new ItemStack(mat)));
        }
    }

    /**
     * This method returns the outcome that mining certain ores yields.
     * 
     * @param ore
     *            The {@link Material} of the ore that was mined
     * 
     * @return The outcome when mining this ore
     */
    public ItemStack getOutcome(Material ore) {
        if (hasSilkTouch()) {
            return new ItemStack(ore);
        }

        Random random = ThreadLocalRandom.current();

        switch (ore) {
        case COAL_ORE:
            return new ItemStack(Material.COAL);
        case DIAMOND_ORE:
            return new ItemStack(Material.DIAMOND);
        case EMERALD_ORE:
            return new ItemStack(Material.EMERALD);
        case NETHER_QUARTZ_ORE:
            return new ItemStack(Material.QUARTZ);
        case REDSTONE_ORE:
            return new ItemStack(Material.REDSTONE, 4 + random.nextInt(2));
        case LAPIS_ORE:
            return new ItemStack(Material.LAPIS_LAZULI, 4 + random.nextInt(4));
        default:
            // This includes Iron and Gold ore (and Ancient Debris)
            return new ItemStack(ore);
        }
    }

    /**
     * This registers a new fuel type for this {@link IndustrialMiner}.
     * 
     * @param ores
     *            The amount of ores this allows you to mine
     * @param item
     *            The item that shall be consumed
     */
    public void addFuelType(int ores, ItemStack item) {
        Validate.isTrue(ores > 1 && ores % 2 == 0, "The amount of ores must be at least 2 and a multiple of 2.");
        fuelTypes.add(new MachineFuel(ores / 2, item));
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
            lore.add(ChatColors.color("&8\u21E8 &7Lasts for max. " + fuel.getTicks() + " Ores"));
            im.setLore(lore);
            item.setItemMeta(im);
            list.add(item);
        }

        return list;
    }

    @Override
    public void onInteract(Player p, Block b) {
        if (activeMiners.containsKey(b.getLocation())) {
            SlimefunPlugin.getLocalization().sendMessage(p, "machines.INDUSTRIAL_MINER.already-running");
            return;
        }

        Block chest = b.getRelative(BlockFace.UP);
        Block[] pistons = findPistons(chest);

        int mod = getRange();
        Block start = b.getRelative(-mod, -1, -mod);
        Block end = b.getRelative(mod, -1, mod);

        ActiveMiner instance = new ActiveMiner(this, p.getUniqueId(), chest, pistons, start, end);
        instance.start(b);
    }

    private Block[] findPistons(Block chest) {
        Block northern = chest.getRelative(BlockFace.NORTH);

        if (northern.getType() == Material.PISTON) {
            return new Block[] { northern, chest.getRelative(BlockFace.SOUTH) };
        } else {
            return new Block[] { chest.getRelative(BlockFace.WEST), chest.getRelative(BlockFace.EAST) };
        }
    }

    /**
     * This returns whether this {@link IndustrialMiner} can mine the given {@link Material}.
     * 
     * @param type
     *            The {@link Material} to check
     * 
     * @return Whether this {@link IndustrialMiner} is capable of mining this {@link Material}
     */
    public boolean canMine(Material type) {
        if (SlimefunTag.INDUSTRIAL_MINER_ORES.isTagged(type)) {
            return true;
        } else if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_16)) {
            return type == Material.ANCIENT_DEBRIS && canMineAncientDebris.getValue();

        }

        return false;
    }

}
