package io.github.thebusybiscuit.slimefun4.core.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

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
    @DisplayName("Test if /sf charge charges the item the player is holding")
    void testCommand() {
        Category category = TestUtilities.getCategory(plugin, "rechargeable");
        SlimefunItemStack RECHARGEABLE_ITEM = new SlimefunItemStack("SF_CHARGE_TEST_ITEM", Material.REDSTONE_BLOCK, "Rechargeable Item", "This isn't real", LoreBuilder.powerCharged(0, 100));
        new RechargeableMock(category, RECHARGEABLE_ITEM, RecipeType.NULL, new ItemStack[9]).register(plugin);

        Player player = server.addPlayer();
        player.setOp(true);
        player.getInventory().setItemInMainHand(RECHARGEABLE_ITEM.clone());

        ItemStack chargedItemStack = player.getInventory().getItemInMainHand();
        Rechargeable chargedItem = (Rechargeable) SlimefunItem.getByItem(chargedItemStack);

        Assertions.assertEquals(chargedItem.getItemCharge(chargedItemStack), 0);
        server.execute("slimefun", player, "charge");

        chargedItemStack = player.getInventory().getItemInMainHand();
        chargedItem = (Rechargeable) SlimefunItem.getByItem(chargedItemStack);

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