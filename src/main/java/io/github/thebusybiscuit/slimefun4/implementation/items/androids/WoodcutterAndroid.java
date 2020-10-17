package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.blocks.Vein;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialConverter;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class WoodcutterAndroid extends ProgrammableAndroid {

    private static final int MAX_REACH = 160;

    public WoodcutterAndroid(Category category, int tier, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, tier, item, recipeType, recipe);
    }

    @Override
    public AndroidType getAndroidType() {
        return AndroidType.WOODCUTTER;
    }

    @Override
    protected boolean chopTree(Block b, BlockMenu menu, BlockFace face) {
        Block target = b.getRelative(face);

        if (Tag.LOGS.isTagged(target.getType())) {
            List<Block> list = Vein.find(target, MAX_REACH, block -> Tag.LOGS.isTagged(block.getType()));

            if (!list.isEmpty()) {
                Block log = list.get(list.size() - 1);
                log.getWorld().playEffect(log.getLocation(), Effect.STEP_SOUND, log.getType());

                OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")));
                if (SlimefunPlugin.getProtectionManager().hasPermission(owner, log.getLocation(), ProtectableAction.BREAK_BLOCK)) {
                    breakLog(log, b, menu, face);
                }

                return false;
            }
        }

        return true;
    }

    private void breakLog(Block log, Block android, BlockMenu menu, BlockFace face) {
        ItemStack drop = new ItemStack(log.getType());

        if (menu.fits(drop, getOutputSlots())) {
            menu.pushItem(drop, getOutputSlots());
            log.getWorld().playEffect(log.getLocation(), Effect.STEP_SOUND, log.getType());

            if (log.getY() == android.getRelative(face).getY()) {
                Optional<Material> sapling = MaterialConverter.getSaplingFromLog(log.getType());

                sapling.ifPresent(log::setType);
            } else {
                log.setType(Material.AIR);
            }
        }
    }

}
