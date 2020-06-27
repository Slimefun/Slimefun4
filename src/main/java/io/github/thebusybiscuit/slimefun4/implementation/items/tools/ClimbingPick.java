package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.slimefun4.api.events.ClimbingPickLaunchEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link ClimbingPick} launches you 1 block upwards when you right click
 * on a ice {@link Block}.
 * Every level of efficiency {@link Enchantment} increases the launch by 0.2 blocks.
 *
 * @author Linox
 *
 */
public class ClimbingPick extends SimpleSlimefunItem<ItemUseHandler> implements DamageableItem {

    private final Map<String, Double> materialSpeedsDef = new HashMap<>();
    private final ItemSetting<Map<String, Double>> materialSpeeds = new ItemSetting<>("launch-amounts", materialSpeedsDef);
    private final Set<UUID> users = new HashSet<>();

    public ClimbingPick(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

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
        materialSpeedsDef.put(Material.STONE.name(), 0.6D);
        materialSpeedsDef.put(Material.DIORITE.name(), 0.6D);
        materialSpeedsDef.put(Material.GRANITE.name(), 0.6D);
        materialSpeedsDef.put(Material.ANDESITE.name(), 0.6D);
        materialSpeedsDef.put(Material.NETHERRACK.name(), 0.6D);
        if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_16)) {
            materialSpeedsDef.put(Material.BLACKSTONE.name(), 0.6D);
        }

        addItemSetting(materialSpeeds);
    }

    @Override
    public void preRegister() {
        super.preRegister();

        addItemHandler(getBlockBreakHandler());
    }

    
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            if (!e.getClickedBlock().isPresent()) return;

            Block block = e.getClickedBlock().get();
            ItemStack item = e.getItem();
            Player p = e.getPlayer();

            if (!getID().startsWith("TEST_CLIMBING_PICK") && p.getLocation().distance(block.getLocation()) > 1.5) return;
            if (e.getClickedFace() == BlockFace.DOWN || e.getClickedFace() == BlockFace.UP) return;

            if (!users.contains(p.getUniqueId())) {
                Material mat = block.getType();
                Double launch = materialSpeeds.getValue().get(mat.name());

                Vector velocity = new Vector(0, 0, 0);
                if (launch != null) {
                    Integer efficiencyLevel = item.getEnchantments().get(Enchantment.DIG_SPEED);
                    if (efficiencyLevel != null){
                        launch += (efficiencyLevel * 0.2);
                    }
                    velocity.setY(launch);

                    users.add(p.getUniqueId());
                    Bukkit.getScheduler().runTaskLaterAsynchronously(SlimefunPlugin.instance, () -> users.remove(p.getUniqueId()), 4L);
                }

                ClimbingPickLaunchEvent event = new ClimbingPickLaunchEvent(p, velocity, this);
                Bukkit.getPluginManager().callEvent(event);

                p.setVelocity(event.getVelocity());
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);

                if (p.getGameMode() != GameMode.CREATIVE) {
                    damageItem(p, e.getItem());
                }
            }
        };
    }

    private BlockBreakHandler getBlockBreakHandler() {
        return new BlockBreakHandler() {
            @Override
            public boolean onBlockBreak(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops) {
                if (isItem(item)) {
                    e.setCancelled(true);
                    return true;
                }
                return false;
            }

            @Override
            public boolean isPrivate() {
                return false;
            }
        };
    }

    @Override
    public boolean isDamageable() {
        return true;
    }
}
