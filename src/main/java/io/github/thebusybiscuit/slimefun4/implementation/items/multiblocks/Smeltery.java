package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dropper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Smeltery extends AbstractSmeltery {

    private final BlockFace[] faces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
    private final ItemSetting<Integer> fireBreakingChance = new ItemSetting<>("fire-breaking-chance", 34);

    public Smeltery(Category category, SlimefunItemStack item) {
        super(category, item, new ItemStack[]{null, new ItemStack(Material.NETHER_BRICK_FENCE), null, new ItemStack(Material.NETHER_BRICKS), new CustomItem(Material.DISPENSER, "发射器 (朝上)"), new ItemStack(Material.NETHER_BRICKS), null, new ItemStack(Material.FLINT_AND_STEEL), null}, new ItemStack[]{SlimefunItems.IRON_DUST, new ItemStack(Material.IRON_INGOT)}, BlockFace.DOWN);

        addItemSetting(fireBreakingChance);
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> items = new ArrayList<>();

        for (int i = 0; i < recipes.size() - 1; i += 2) {
            if (Arrays.stream(recipes.get(i)).skip(1).anyMatch(Objects::nonNull)) {
                continue;
            }

            items.add(recipes.get(i)[0]);
            items.add(recipes.get(i + 1)[0]);
        }

        return items;
    }

    @Override
    protected void craft(Player p, Block b, Inventory inv, ItemStack[] recipe, ItemStack output, Inventory outputInv) {
        super.craft(p, b, inv, recipe, output, outputInv);

        if (ThreadLocalRandom.current().nextInt(100) < fireBreakingChance.getValue()) {
            consumeFire(p, b.getRelative(BlockFace.DOWN), b);
        }
    }

    private void consumeFire(Player p, Block dispenser, Block b) {
        Inventory chamber = findIgnitionChamber(dispenser);

        if (chamber != null) {
            if (chamber.contains(Material.FLINT_AND_STEEL)) {
                ItemStack item = chamber.getItem(chamber.first(Material.FLINT_AND_STEEL));
                ItemMeta meta = item.getItemMeta();
                ((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 1);
                item.setItemMeta(meta);

                if (((Damageable) item.getItemMeta()).getDamage() >= item.getType().getMaxDurability()) {
                    item.setAmount(0);
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                }

                p.getWorld().playSound(p.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 1, 1);
            } else {
                SlimefunPlugin.getLocalization().sendMessage(p, "machines.ignition-chamber-no-flint", true);

                Block fire = b.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
                fire.getWorld().playEffect(fire.getLocation(), Effect.STEP_SOUND, fire.getType());
                fire.setType(Material.AIR);
            }
        } else {
            Block fire = b.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
            fire.getWorld().playEffect(fire.getLocation(), Effect.STEP_SOUND, fire.getType());
            fire.setType(Material.AIR);
        }
    }

    private Inventory findIgnitionChamber(Block b) {
        for (BlockFace face : faces) {
            if (b.getRelative(face).getType() == Material.DROPPER && BlockStorage.check(b.getRelative(face), "IGNITION_CHAMBER")) {
                return ((Dropper) b.getRelative(face).getState()).getInventory();
            }
        }

        return null;
    }

}