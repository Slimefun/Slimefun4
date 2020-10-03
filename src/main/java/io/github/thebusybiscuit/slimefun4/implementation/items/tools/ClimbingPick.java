package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

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
 * @author TheBusyBiscuit
 *
 */
public class ClimbingPick extends SimpleSlimefunItem<ItemUseHandler> implements DamageableItem, RecipeDisplayItem {

    private static final double BASE_POWER = 1;
    private static final double MAX_DISTANCE = 4.4;

    private final ItemSetting<Boolean> dualWielding = new ItemSetting<>("dual-wielding", true);
    private final ItemSetting<Boolean> damageOnUse = new ItemSetting<>("damage-on-use", true);
    private final Map<Material, Double> materialSpeeds;
    private final Set<UUID> users = new HashSet<>();

    @ParametersAreNonnullByDefault
    public ClimbingPick(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        addItemSetting(dualWielding, damageOnUse);

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
            materialSpeeds.put(Material.BASALT, itemCfg.getOrSetDefault(cfgKey + Material.BASALT.name(), 0.7));
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

            // Check if the Player is standing close to the wall
            if (p.getLocation().distanceSquared(block.getLocation().add(0.5, 0.5, 0.5)) > MAX_DISTANCE) {
                return;
            }

            // Check for dual wielding
            if (isDualWieldingEnabled() && !isItem(getOtherHandItem(p, e.getHand()))) {
                SlimefunPlugin.getLocalization().sendMessage(p, "messages.climbing-pick.dual-wielding");
                return;
            }

            // Top and bottom faces won't be allowed
            if (e.getClickedFace() == BlockFace.DOWN || e.getClickedFace() == BlockFace.UP) {
                return;
            }

            climb(p, e.getHand(), e.getItem(), block);
        };
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    private ItemStack getOtherHandItem(Player p, EquipmentSlot hand) {
        if (hand == EquipmentSlot.HAND) {
            return p.getInventory().getItemInOffHand();
        }
        else {
            return p.getInventory().getItemInMainHand();
        }
    }

    @ParametersAreNonnullByDefault
    private void climb(Player p, EquipmentSlot hand, ItemStack item, Block block) {
        double power = materialSpeeds.getOrDefault(block.getType(), 0.0);

        if (power > 0.05) {
            // Prevent players from spamming this
            if (users.add(p.getUniqueId())) {
                int efficiencyLevel = item.getEnchantmentLevel(Enchantment.DIG_SPEED);

                if (efficiencyLevel != 0) {
                    power += efficiencyLevel * 0.1;
                }

                SlimefunPlugin.runSync(() -> users.remove(p.getUniqueId()), 4L);
                Vector velocity = new Vector(0, power * BASE_POWER, 0);
                ClimbingPickLaunchEvent event = new ClimbingPickLaunchEvent(p, velocity, this, item, block);
                Bukkit.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    p.setVelocity(event.getVelocity());
                    p.setFallDistance(0);
                    swing(p, block, hand, item);
                }
            }
        }
        else if (!isDualWieldingEnabled() || hand == EquipmentSlot.HAND) {
            // We don't wanna send the message twice, so we check for dual wielding
            SlimefunPlugin.getLocalization().sendMessage(p, "messages.climbing-pick.wrong-material");
        }
    }

    @ParametersAreNonnullByDefault
    private void swing(Player p, Block b, EquipmentSlot hand, ItemStack item) {
        if (isDualWieldingEnabled()) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                damageItem(p, p.getInventory().getItemInMainHand());
                playAnimation(p, b, EquipmentSlot.HAND);
            }
            else {
                damageItem(p, p.getInventory().getItemInOffHand());
                playAnimation(p, b, EquipmentSlot.OFF_HAND);
            }
        }
        else {
            damageItem(p, item);
            playAnimation(p, b, hand);
        }
    }

    @Override
    public void damageItem(Player p, ItemStack item) {
        if (p.getGameMode() != GameMode.CREATIVE) {
            DamageableItem.super.damageItem(p, item);
        }
    }

    @Override
    public boolean isDamageable() {
        return damageOnUse.getValue();
    }

    @ParametersAreNonnullByDefault
    private void playAnimation(Player p, Block b, EquipmentSlot hand) {
        MinecraftVersion version = SlimefunPlugin.getMinecraftVersion();

        if (version != MinecraftVersion.UNIT_TEST) {
            p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());

            if (version.isAtLeast(MinecraftVersion.MINECRAFT_1_15)) {
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
