package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks.MultiBlockMachine;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OreCrusher extends MultiBlockMachine {

	private final DoubleOreSetting doubleOres = new DoubleOreSetting();

	public OreCrusher(Category category) {
		super(category, SlimefunItems.ORE_CRUSHER, new ItemStack[]{null, null, null, null, new ItemStack(Material.NETHER_BRICK_FENCE), null, new ItemStack(Material.IRON_BARS), new CustomItem(Material.DISPENSER, "发射器(朝上)"), new ItemStack(Material.IRON_BARS)},
				new ItemStack[]{SlimefunItems.GOLD_4K, SlimefunItems.GOLD_DUST, new ItemStack(Material.GRAVEL), new ItemStack(Material.SAND), new ItemStack(Material.MAGMA_BLOCK, 4), SlimefunItems.SULFATE}, BlockFace.SELF);

		addItemSetting(doubleOres);
	}

	public boolean isOreDoublingEnabled() {
		return doubleOres.getValue();
	}

	@Override
	public void postRegister() {
		super.postRegister();

		shownRecipes.addAll(Arrays.asList(new ItemStack(Material.COAL_ORE), doubleOres.getCoal(), new ItemStack(Material.LAPIS_ORE), doubleOres.getLapisLazuli(), new ItemStack(Material.REDSTONE_ORE), doubleOres.getRedstone(), new ItemStack(Material.DIAMOND_ORE), doubleOres.getDiamond(), new ItemStack(Material.EMERALD_ORE), doubleOres.getEmerald()));
	}

	@Override
	public List<ItemStack> getDisplayRecipes() {
		return recipes.stream().map(items -> items[0]).collect(Collectors.toList());
	}

	@Override
	public void onInteract(Player p, Block b) {
		Block dispBlock = b.getRelative(BlockFace.DOWN);
		Dispenser disp = (Dispenser) dispBlock.getState();
		Inventory inv = disp.getInventory();

		for (ItemStack current : inv.getContents()) {
			for (ItemStack convert : RecipeType.getRecipeInputs(this)) {
				if (convert != null && SlimefunUtils.isItemSimilar(current, convert, true)) {
					ItemStack adding = RecipeType.getRecipeOutput(this, convert);
					Inventory outputInv = findOutputInventory(adding, dispBlock, inv);
					if (outputInv != null) {
						ItemStack removing = current.clone();
						removing.setAmount(convert.getAmount());
						inv.removeItem(removing);
						outputInv.addItem(adding);
						p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, 1);
					} else SlimefunPlugin.getLocal().sendMessage(p, "machines.full-inventory", true);

					return;
				}
			}
		}

		SlimefunPlugin.getLocal().sendMessage(p, "machines.unknown-material", true);
	}

	private static class DoubleOreSetting extends ItemSetting<Boolean> {

		private final ItemStack coal = new ItemStack(Material.COAL, 1);
		private final ItemStack lapis = new ItemStack(Material.LAPIS_LAZULI, 7);
		private final ItemStack redstone = new ItemStack(Material.REDSTONE, 4);
		private final ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
		private final ItemStack emerald = new ItemStack(Material.EMERALD, 1);

		public DoubleOreSetting() {
			super("double-ores", true);
		}

		private void apply(boolean value) {
			coal.setAmount(value ? 2 : 1);
			lapis.setAmount(value ? 14 : 7);
			redstone.setAmount(value ? 8 : 4);
			diamond.setAmount(value ? 2 : 1);
			emerald.setAmount(value ? 2 : 1);

			SlimefunItem ironDust = SlimefunItem.getByID("IRON_DUST");
			if (ironDust != null) {
				ironDust.setRecipeOutput(new CustomItem(SlimefunItems.IRON_DUST, value ? 2 : 1));
			}

			SlimefunItem goldDust = SlimefunItem.getByID("GOLD_DUST");
			if (goldDust != null) {
				goldDust.setRecipeOutput(new CustomItem(SlimefunItems.GOLD_DUST, value ? 2 : 1));
			}
		}

		@Override
		public void update(Boolean newValue) {
			super.update(newValue);
			apply(newValue);
		}

		@Override
		public void load(SlimefunItem item) {
			super.load(item);
			apply(getValue());
		}

		public ItemStack getCoal() {
			return coal;
		}

		public ItemStack getLapisLazuli() {
			return lapis;
		}

		public ItemStack getRedstone() {
			return redstone;
		}

		public ItemStack getDiamond() {
			return diamond;
		}

		public ItemStack getEmerald() {
			return emerald;
		}

	}

}