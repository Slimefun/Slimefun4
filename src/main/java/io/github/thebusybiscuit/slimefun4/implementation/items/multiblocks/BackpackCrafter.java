package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerBackpack;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.SlimefunBackpack;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This abstract super class is responsible for some utility methods for machines which
 * are capable of upgrading backpacks.
 * 
 * @author TheBusyBiscuit
 * 
 * @see EnhancedCraftingTable
 * @see MagicWorkbench
 *
 */
abstract class BackpackCrafter extends MultiBlockMachine {

    BackpackCrafter(Category category, SlimefunItemStack item, ItemStack[] recipe, BlockFace trigger) {
        super(category, item, recipe, trigger);
    }

    protected Inventory createVirtualInventory(Inventory inv) {
        Inventory fakeInv = Bukkit.createInventory(null, 9, "Fake Inventory");

        for (int j = 0; j < inv.getContents().length; j++) {
            ItemStack stack = inv.getContents()[j];

            // Fixes #2103 - Properly simulating the consumption
            // (which may leave behind empty buckets or glass bottles)
            if (stack != null) {
                stack = stack.clone();
                ItemUtils.consumeItem(stack, true);
            }

            fakeInv.setItem(j, stack);
        }

        return fakeInv;
    }

    protected void upgradeBackpack(Player p, Inventory inv, SlimefunBackpack backpack, ItemStack output) {
        ItemStack backpackItem = null;

        for (int j = 0; j < 9; j++) {
            if (inv.getContents()[j] != null && inv.getContents()[j].getType() != Material.AIR && SlimefunItem.getByItem(inv.getContents()[j]) instanceof SlimefunBackpack) {
                backpackItem = inv.getContents()[j];
                break;
            }
        }

        int size = backpack.getSize();
        Optional<String> id = retrieveID(backpackItem, size);

        if (id.isPresent()) {
            for (int line = 0; line < output.getItemMeta().getLore().size(); line++) {
                if (output.getItemMeta().getLore().get(line).equals(ChatColors.color("&7ID: <ID>"))) {
                    ItemMeta im = output.getItemMeta();
                    List<String> lore = im.getLore();
                    lore.set(line, lore.get(line).replace("<ID>", id.get()));
                    im.setLore(lore);
                    output.setItemMeta(im);
                    break;
                }
            }
        } else {
            for (int line = 0; line < output.getItemMeta().getLore().size(); line++) {
                if (output.getItemMeta().getLore().get(line).equals(ChatColors.color("&7ID: <ID>"))) {
                    int target = line;

                    PlayerProfile.get(p, profile -> {
                        int backpackId = profile.createBackpack(size).getId();
                        SlimefunPlugin.getBackpackListener().setBackpackId(p, output, target, backpackId);
                    });

                    break;
                }
            }
        }
    }

    private Optional<String> retrieveID(ItemStack backpack, int size) {
        if (backpack != null) {
            for (String line : backpack.getItemMeta().getLore()) {
                if (line.startsWith(ChatColors.color("&7ID: ")) && line.contains("#")) {
                    String id = line.replace(ChatColors.color("&7ID: "), "");
                    String[] idSplit = PatternUtils.HASH.split(id);

                    PlayerProfile.fromUUID(UUID.fromString(idSplit[0]), profile -> {
                        Optional<PlayerBackpack> optional = profile.getBackpack(Integer.parseInt(idSplit[1]));
                        optional.ifPresent(playerBackpack -> playerBackpack.setSize(size));
                    });

                    return Optional.of(id);
                }
            }
        }

        return Optional.empty();
    }

}
