package io.github.thebusybiscuit.slimefun4.testing.tests.commands;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.GoldPan;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import io.github.thebusybiscuit.slimefun4.testing.tests.items.TestRechargeableItems;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

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

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    @DisplayName("Test if /charge charges the item the player is holding")
    void testCommand(boolean op) {
        Category category = TestUtilities.getCategory(plugin, "rechargeable");
        final SlimefunItemStack RECHARGEABLE_ITEM = new SlimefunItemStack("RECHARGEABLE_ITEM", Material.REDSTONE_BLOCK, "Rechargeable Item", "This isn't real", LoreBuilder.powerCharged(0, 100));
        new RechargeableMock(category, RECHARGEABLE_ITEM, RecipeType.NULL, new ItemStack[9]).register(plugin);

        Player player = server.addPlayer();
        player.setOp(op);
        player.getInventory().setItemInMainHand(RECHARGEABLE_ITEM.clone());
        server.execute("slimefun", player, "charge");

        ItemStack chargedItemStack = player.getInventory().getItemInMainHand();
        Rechargeable chargedItem = (Rechargeable) SlimefunItem.getByItem(chargedItemStack);

        Assertions.assertEquals(chargedItem.getItemCharge(chargedItemStack), 100);
        System.out.println(chargedItem.getItemCharge(chargedItemStack));
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