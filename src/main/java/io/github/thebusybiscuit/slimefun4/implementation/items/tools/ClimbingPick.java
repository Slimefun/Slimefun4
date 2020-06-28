package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.ArrayList;
import java.util.EnumMap;
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

import io.github.thebusybiscuit.cscorelib2.config.Config;
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

    private final Map<Material, Double> materialSpeeds;
    private final Set<UUID> users = new HashSet<>();

    public ClimbingPick(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

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
                double launch = materialSpeeds.getOrDefault(block.getType(), 0D);

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
