package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.EntityDamageHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class HeadCracker extends SimpleSlimefunItem<ItemUseHandler> implements DamageableItem {

    public HeadCracker(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Optional<Block> block = e.getClickedBlock();
            Player p = e.getPlayer();

            if (block.isPresent()) {
                Block b = block.get();
                if (!MaterialCollections.getAllHeads().contains(b.getType())) return;

                if (SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.BREAK_BLOCK)) {
                    BlockBreakEvent event = new BlockBreakEvent(b, p);
                    Bukkit.getPluginManager().callEvent(event);

                    b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
                    damageItem(p, e.getItem());
                }
            }

            e.cancel();
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

    // This is kind of an Easter Egg.
    // Don't add this to the item lore.
    // I implemented this because I thought it was funny.
    // ~Linox
    private EntityDamageHandler getEntityDamageHandler() {
        return (e, entity, item1) -> {
            if (entity instanceof Skeleton  || entity instanceof SkeletonHorse) {
                ((Creature) entity).damage(e.getFinalDamage() + 4D);
                e.setCancelled(true);
            }
        };
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public void preRegister() {
        super.preRegister();

        addItemHandler(getBlockBreakHandler(), getEntityDamageHandler());
    }
}
