package io.github.thebusybiscuit.slimefun4.testing.tests.commands;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TestChargeCommand {

    private static ServerMock server;
    private static SlimefunPlugin plugin;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test if /charge charges the item the player is holding")
    void testCommand() {
        Category category = TestUtilities.getCategory(plugin, "rechargeable");
        final SlimefunItemStack RECHARGEABLE_ITEM = new SlimefunItemStack("RECHARGEABLE_ITEM", Material.REDSTONE_BLOCK, "Rechargeable Item", "This isn't real", LoreBuilder.powerCharged(0, 100));
        new RechargeableMock(category, RECHARGEABLE_ITEM, RecipeType.NULL, new ItemStack[9]).register(plugin);

        Player player = server.addPlayer();
        player.setOp(true);
        player.getInventory().setItemInMainHand(RECHARGEABLE_ITEM.clone());
        server.execute("slimefun", player, "charge");

        ItemStack chargedItemStack = player.getInventory().getItemInMainHand();
        Rechargeable chargedItem = (Rechargeable) SlimefunItem.getByItem(chargedItemStack);

        Assertions.assertEquals(chargedItem.getItemCharge(chargedItemStack), chargedItem.getMaxItemCharge(chargedItemStack));
    }


    private class RechargeableMock extends SlimefunItem implements Rechargeable {

        public RechargeableMock(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
            super(category, item, recipeType, recipe);
        }

        @Override
        public float getMaxItemCharge(ItemStack item) {
            return 100;
        }
    }

}