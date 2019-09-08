package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectionModule;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.DamageableItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.NotPlaceable;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class HeadCracker extends SimpleSlimefunItem<ItemInteractionHandler> implements NotPlaceable, DamageableItem {

    private boolean damageOnUse;

    public HeadCracker(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
        super(category, item, id, recipeType, recipe, keys, values);
    }

    @Override
    public ItemInteractionHandler getItemHandler() {
        return (e, p, item) -> {
            if (SlimefunManager.isItemSimiliar(item, SlimefunItems.HEAD_CRACKER, true)) {
                Block b = e.getClickedBlock();
                if (SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectionModule.Action.BREAK_BLOCK)) {
                    if (b.getType().name().contains("SKULL") || b.getType().name().contains("HEAD")) {

                        SlimefunItem SFItem = BlockStorage.check(b);
                        if (SFItem != null) {
                            BlockStorage.clearBlockInfo(b);
                            b.setType(Material.AIR);
                            b.getWorld().dropItemNaturally(b.getLocation(), SFItem.getItem());
                        } else e.getClickedBlock().breakNaturally();

                        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, SoundCategory.BLOCKS, 0.3F, 1F);
                        damageItem(p, item);
                    }
                }
                else {
                    Messages.local.sendTranslation(p, "messages.cannot-break", true);
                }
                return true;
            }
            return false;
        };
    }

    @Override
    public void postRegister() {
        damageOnUse = ((boolean) Slimefun.getItemValue(getID(), "damage-on-use"));
    }

    @Override
    public boolean isDamageable() {
        return damageOnUse;
    }
}
