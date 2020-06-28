package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.slimefun4.api.events.ClimbingPickLaunchEvent;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
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

    private final Map<String, Double> materialSpeeds;
    private final Set<UUID> users = new HashSet<>();

    public ClimbingPick(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        Map<String, Double> materialSpeedsDef = new HashMap<>();
        for (Material mat : MaterialCollections.getAllIceBlocks()) {
            materialSpeedsDef.put(mat.name(), 1D);
        }
        for (Material mat : MaterialCollections.getAllConcretePowderColors()) {
            materialSpeedsDef.put(mat.name(), 1D);
        }
        for (Material mat : MaterialCollections.getAllTerracottaColors()) {
            materialSpeedsDef.put(mat.name(), 1D);
        }

        materialSpeedsDef.put(Material.GRAVEL.name(), 0.4);
        materialSpeedsDef.put(Material.SAND.name(), 0.4);
        materialSpeedsDef.put(Material.STONE.name(), 0.6);
        materialSpeedsDef.put(Material.DIORITE.name(), 0.6);
        materialSpeedsDef.put(Material.GRANITE.name(), 0.6);
        materialSpeedsDef.put(Material.ANDESITE.name(), 0.6);
        materialSpeedsDef.put(Material.NETHERRACK.name(), 0.6);
        if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_16)) {
            materialSpeedsDef.put(Material.BLACKSTONE.name(), 0.6);
        }
        materialSpeeds = SlimefunPlugin.getItemCfg().getOrSetDefault(getID() + ".launch-amounts", materialSpeedsDef);
    }
    
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            if (!e.getClickedBlock().isPresent()) return;

            Block block = e.getClickedBlock().get();
            ItemStack item = e.getItem();
            Player p = e.getPlayer();

            if (p.getLocation().distanceSquared(block.getLocation()) > 2.25) return;
            if (e.getClickedFace() == BlockFace.DOWN || e.getClickedFace() == BlockFace.UP) return;

            if (!users.contains(p.getUniqueId())) {
                Material mat = block.getType();
                double launch = materialSpeeds.getOrDefault(mat.name(), 0D);

                Vector velocity = new Vector(0, 0, 0);
                if (launch > 0.05) {
                    int efficiencyLevel = item.getEnchantments().getOrDefault(Enchantment.DIG_SPEED, 0);
                    if (efficiencyLevel != 0){
                        launch += (efficiencyLevel * 0.2);
                    }
                    velocity.setY(launch);

                    users.add(p.getUniqueId());
                    Bukkit.getScheduler().runTaskLater(SlimefunPlugin.instance, () -> users.remove(p.getUniqueId()), 4L);
                }

                ClimbingPickLaunchEvent event = new ClimbingPickLaunchEvent(p, velocity, this, item, block);
                Bukkit.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    p.setVelocity(event.getVelocity());
                    p.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, 1);

                    if (p.getGameMode() != GameMode.CREATIVE) {
                        damageItem(p, e.getItem());
                    }
                }
            }
        };
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> display = new ArrayList<>();
        for (String matName : materialSpeeds.keySet()) {
            Material mat = Material.getMaterial(matName);
            if (mat == null) continue;
            display.add(new CustomItem(mat, "&bCan Climb This Block"));
        }

        return display;
    }
}
