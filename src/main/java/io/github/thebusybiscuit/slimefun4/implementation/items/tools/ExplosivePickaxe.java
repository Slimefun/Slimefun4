package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import io.github.starwishsama.extra.ProtectionChecker;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.HandledBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.NotPlaceable;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ExplosivePickaxe extends SimpleSlimefunItem<BlockBreakHandler> implements NotPlaceable, DamageableItem {

    private final ItemSetting<Boolean> damageOnUse = new ItemSetting<>("damage-on-use", true);
    private final ItemSetting<List<String>> unbreakableBlocks = new ItemSetting<>("unbreakable-blocks", new ArrayList<>());

    public ExplosivePickaxe(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemSetting(damageOnUse, unbreakableBlocks);
    }

    @Override
    public BlockBreakHandler getItemHandler() {
        return new BlockBreakHandler() {

            @Override
            public boolean isPrivate() {
                return false;
            }

            @Override
            public boolean onBlockBreak(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops) {
                if (isItem(item)) {
                    if (Slimefun.hasUnlocked(e.getPlayer(), ExplosivePickaxe.this, true)) {
                        e.getBlock().getWorld().createExplosion(e.getBlock().getLocation(), 0.0F);
                        e.getBlock().getWorld().playSound(e.getBlock().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.3F, 1F);

                        for (int x = -1; x <= 1; x++) {
                            for (int y = -1; y <= 1; y++) {
                                for (int z = -1; z <= 1; z++) {
                                    if (x == 0 && y == 0 && z == 0) {
                                        continue;
                                    }

                                    Block b = e.getBlock().getRelative(x, y, z);
                                    breakBlock(e.getPlayer(), b, fortune, drops, item);
                                }
                            }
                        }
                    }
                    return true;
                } else return false;
            }
        };
    }

    private boolean isUnbreakable(String name) {
        if (!unbreakableBlocks.getValue().isEmpty()) {
            return unbreakableBlocks.getValue().contains(name);
        }
        return false;
    }

    private void breakBlock(Player p, Block b, int fortune, List<ItemStack> drops) {
        if (!isUnbreakable(b.getType().name()) && ProtectionChecker.check(p, b, true) && b.getType() != Material.AIR && !b.isLiquid() && !MaterialCollections.getAllUnbreakableBlocks().contains(b.getType()) && SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.BREAK_BLOCK)) {
            SlimefunPlugin.getProtectionManager().logAction(p, b, ProtectableAction.BREAK_BLOCK);

            b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
            SlimefunItem sfItem = BlockStorage.check(b);

            if (sfItem != null && !(sfItem instanceof HandledBlock)) {
                SlimefunBlockHandler handler = SlimefunPlugin.getRegistry().getBlockHandlers().get(sfItem.getID());

                if (handler != null && !handler.onBreak(p, b, sfItem, UnregisterReason.PLAYER_BREAK)) {
                    drops.add(BlockStorage.retrieve(b));
                }
            } else if (b.getType() == Material.PLAYER_HEAD || b.getType().name().endsWith("_SHULKER_BOX")) {
                b.breakNaturally();
            } else {
                for (ItemStack drop : b.getDrops(getItem())) {
                    b.getWorld().dropItemNaturally(b.getLocation(), (b.getType().toString().endsWith("_ORE") && b.getType() != Material.IRON_ORE && b.getType() != Material.GOLD_ORE) ? new CustomItem(drop, fortune) : drop);
                }

                b.setType(Material.AIR);
                
            }
            
            damageItem(p, item);
        }
    }

    @Override
    public boolean isDamageable() {
        return damageOnUse.getValue();
    }

}
