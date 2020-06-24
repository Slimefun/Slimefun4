package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import io.github.starwishsama.extra.ProtectionChecker;
import io.github.thebusybiscuit.cscorelib2.blocks.Vein;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialConverter;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class WoodcutterAndroid extends ProgrammableAndroid {

    public WoodcutterAndroid(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public AndroidType getAndroidType() {
        return AndroidType.WOODCUTTER;
    }

    @Override
    protected boolean chopTree(Block b, BlockMenu menu, BlockFace face) {
        if (MaterialCollections.getAllLogs().contains(b.getRelative(face).getType())) {
            List<Block> list = Vein.find(b.getRelative(face), 180, block -> MaterialCollections.getAllLogs().contains(block.getType()));

            if (!list.isEmpty()) {
                Block log = list.get(list.size() - 1);
                log.getWorld().playEffect(log.getLocation(), Effect.STEP_SOUND, log.getType());
                OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")));

                if (SlimefunPlugin.getProtectionManager().hasPermission(p, log.getLocation(), ProtectableAction.BREAK_BLOCK)
                        && ProtectionChecker.canInteract(p.getPlayer(), log, ProtectableAction.BREAK_BLOCK)) {
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

                if (sapling.isPresent()) {
                    log.setType(sapling.get());
                }
            } else {
                log.setType(Material.AIR);
            }
        }
    }


}
