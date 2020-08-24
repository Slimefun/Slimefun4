package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.events.ClimbingPickLaunchEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link ClimbingPick} launches you 1 block upwards when you right click
 * on a ice {@link Block}.
 * Every level of efficiency {@link Enchantment} increases the launch by 0.2 blocks.
 *
 * @author Linox
 *
 */
public class ClimbingPick extends SimpleSlimefunItem<ItemUseHandler> implements DamageableItem, RecipeDisplayItem {

    private static final double POWER = 0.75;

    private final Map<Material, Double> materialSpeeds;
    private final ItemSetting<Boolean> dualWielding = new ItemSetting<>("dual-wielding", true);
    private final Set<UUID> users = new HashSet<>();

    public ClimbingPick(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        addItemSetting(dualWielding);

        String cfgKey = getID() + ".launch-amounts.";
        Config itemCfg = SlimefunPlugin.getItemCfg();
        materialSpeeds = new EnumMap<>(Material.class);

        for (Material mat : MaterialCollections.getAllIceBlocks()) {
            materialSpeeds.put(mat, itemCfg.getOrSetDefault(cfgKey + mat.name(), 1.0));
        }

        for (Material mat : MaterialCollections.getAllConcretePowderColors()) {
            materialSpeeds.put(mat, itemCfg.getOrSetDefault(cfgKey + mat.name(), 1.0));
        }

        for (Material mat : MaterialCollections.getAllTerracottaColors()) {
            materialSpeeds.put(mat, itemCfg.getOrSetDefault(cfgKey + mat.name(), 1.0));
        }

        materialSpeeds.put(Material.GRAVEL, itemCfg.getOrSetDefault(cfgKey + Material.GRAVEL.name(), 0.4));
        materialSpeeds.put(Material.SAND, itemCfg.getOrSetDefault(cfgKey + Material.SAND.name(), 0.4));
        materialSpeeds.put(Material.STONE, itemCfg.getOrSetDefault(cfgKey + Material.STONE.name(), 0.6));
        materialSpeeds.put(Material.DIORITE, itemCfg.getOrSetDefault(cfgKey + Material.DIORITE.name(), 0.6));
        materialSpeeds.put(Material.GRANITE, itemCfg.getOrSetDefault(cfgKey + Material.GRANITE.name(), 0.6));
        materialSpeeds.put(Material.ANDESITE, itemCfg.getOrSetDefault(cfgKey + Material.ANDESITE.name(), 0.6));
        materialSpeeds.put(Material.NETHERRACK, itemCfg.getOrSetDefault(cfgKey + Material.NETHERRACK.name(), 0.6));

        if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_16)) {
            materialSpeeds.put(Material.BLACKSTONE, itemCfg.getOrSetDefault(cfgKey + Material.BLACKSTONE.name(), 0.6));
        }
    }

    public boolean isDualWieldingEnabled() {
        return dualWielding.getValue();
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            if (!e.getClickedBlock().isPresent()) {
                return;
            }

            Block block = e.getClickedBlock().get();
            Player p = e.getPlayer();

            if (p.getLocation().distanceSquared(block.getLocation()) > 2.8) {
                return;
            }

            if (isDualWieldingEnabled() && !isItem(getOtherHandItem(p, e.getHand()))) {
                SlimefunPlugin.getLocalization().sendMessage(p, "messages.climbing-pick.dual-wielding");
            }

            if (e.getClickedFace() == BlockFace.DOWN || e.getClickedFace() == BlockFace.UP) {
                return;
            }

            // Prevent players from spamming this
            if (!users.contains(p.getUniqueId())) {
                climb(p, e.getHand(), e.getItem(), block);
            }
        };
    }

    private ItemStack getOtherHandItem(Player p, EquipmentSlot hand) {
        if (hand == EquipmentSlot.HAND) {
            return p.getInventory().getItemInOffHand();
        }
        else {
            return p.getInventory().getItemInMainHand();
        }
    }

    private void climb(Player p, EquipmentSlot hand, ItemStack item, Block block) {
        double power = materialSpeeds.getOrDefault(block.getType(), 0.0);

        Vector velocity = new Vector(0, 0, 0);
        if (power > 0.05) {
            int efficiencyLevel = item.getEnchantmentLevel(Enchantment.DIG_SPEED);

            if (efficiencyLevel != 0) {
                power += efficiencyLevel * 0.1;
            }

            velocity.setY(power * POWER);

            users.add(p.getUniqueId());
            Bukkit.getScheduler().runTaskLater(SlimefunPlugin.instance(), () -> users.remove(p.getUniqueId()), 4L);

            ClimbingPickLaunchEvent event = new ClimbingPickLaunchEvent(p, velocity, this, item, block);
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                p.setVelocity(event.getVelocity());
                p.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
                swing(p, hand, item);
            }
        }
    }

    private void swing(Player p, EquipmentSlot hand, ItemStack item) {
        if (p.getGameMode() != GameMode.CREATIVE) {
            if (isDualWieldingEnabled()) {
                if (ThreadLocalRandom.current().nextBoolean()) {
                    p.swingMainHand();
                }
                else {
                    p.swingOffHand();
                }

                damageItem(p, p.getInventory().getItemInMainHand());
                damageItem(p, p.getInventory().getItemInOffHand());
            }
            else {
                damageItem(p, item);

                if (hand == EquipmentSlot.HAND) {
                    p.swingMainHand();
                }
                else {
                    p.swingOffHand();
                }
            }
        }
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> display = new ArrayList<>();

        for (Material mat : materialSpeeds.keySet()) {
            display.add(new ItemStack(mat));
        }

        return display;
    }

    @Override
    public String getLabelLocalPath() {
        return "guide.tooltips.recipes.climbing-pick";
    }
}
