package io.github.thebusybiscuit.slimefun4.testing.tests.listeners;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerBackpack;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.SlimefunBackpack;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.BackpackListener;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class TestBackpackListener {

    private static final int BACKPACK_SIZE = 27;
    private static ServerMock server;
    private static SlimefunPlugin plugin;
    private static BackpackListener listener;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
        listener = new BackpackListener();
        listener.register(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    private PlayerBackpack awaitBackpack(ItemStack item) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<PlayerBackpack> ref = new AtomicReference<>();

        // This loads the backpack asynchronously
        PlayerProfile.getBackpack(item, backpack -> {
            ref.set(backpack);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
        return ref.get();
    }

    private PlayerBackpack openMockBackpack(Player player, String id, int size) throws InterruptedException {
        SlimefunItemStack item = new SlimefunItemStack(id, Material.CHEST, "&4Mock Backpack", "", "&7Size: &e" + BACKPACK_SIZE, "&7ID: <ID>", "", "&7&eRight Click&7 to open");
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        PlayerBackpack backpack = profile.createBackpack(size);
        listener.setBackpackId(player, item, 2, backpack.getId());

        Category category = new Category(new NamespacedKey(plugin, "test_backpacks"), new CustomItem(Material.CHEST, "&4Test Backpacks"));
        SlimefunBackpack slimefunBackpack = new SlimefunBackpack(size, category, item, RecipeType.NULL, new ItemStack[9]);
        slimefunBackpack.register(plugin);

        listener.openBackpack(player, item, slimefunBackpack);
        return backpack;
    }

    @Test
    public void testIllegalSetId() {
        Player player = server.addPlayer();

        Assertions.assertThrows(IllegalArgumentException.class, () -> listener.setBackpackId(null, null, 1, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> listener.setBackpackId(player, null, 1, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> listener.setBackpackId(player, new ItemStack(Material.REDSTONE), 1, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> listener.setBackpackId(player, new CustomItem(Material.REDSTONE, "Hi", "lore"), 1, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> listener.setBackpackId(player, new CustomItem(Material.REDSTONE, "Hi", "lore", "no id"), 1, 1));
    }

    @Test
    public void testSetId() throws InterruptedException {
        Player player = server.addPlayer();
        ItemStack item = new CustomItem(Material.CHEST, "&cA mocked Backpack", "", "&7Size: &e" + BACKPACK_SIZE, "&7ID: <ID>", "", "&7&eRight Click&7 to open");

        PlayerProfile profile = TestUtilities.awaitProfile(player);
        int id = profile.createBackpack(BACKPACK_SIZE).getId();

        listener.setBackpackId(player, item, 2, id);
        Assertions.assertEquals(ChatColor.GRAY + "ID: " + player.getUniqueId() + "#" + id, item.getItemMeta().getLore().get(2));

        PlayerBackpack backpack = awaitBackpack(item);
        Assertions.assertEquals(player.getUniqueId(), backpack.getOwner().getUUID());
        Assertions.assertEquals(id, backpack.getId());
    }

    @Test
    public void testOpenBackpack() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerBackpack backpack = openMockBackpack(player, "TEST_OPEN_BACKPACK", 27);
        InventoryView view = player.getOpenInventory();
        Assertions.assertEquals(backpack.getInventory(), view.getTopInventory());
    }

    @Test
    public void testCloseBackpack() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerBackpack backpack = openMockBackpack(player, "TEST_CLOSE_BACKPACK", 27);
        listener.onClose(new InventoryCloseEvent(player.getOpenInventory()));

        Assertions.assertTrue(backpack.getOwner().isDirty());
    }

    @Test
    public void testBackpackDropNormalItem() throws InterruptedException {
        Player player = server.addPlayer();
        openMockBackpack(player, "DROP_NORMAL_ITEM_BACKPACK_TEST", 27);

        Item item = Mockito.mock(Item.class);
        Mockito.when(item.getItemStack()).thenReturn(new ItemStack(Material.SUGAR_CANE));
        PlayerDropItemEvent event = new PlayerDropItemEvent(player, item);
        listener.onItemDrop(event);

        Assertions.assertFalse(event.isCancelled());
    }

    private boolean isAllowed(String id, ItemStack item) throws InterruptedException {
        Player player = server.addPlayer();
        Inventory inv = openMockBackpack(player, id, 9).getInventory();

        int slot = 7;
        inv.setItem(slot, item);
        InventoryClickEvent event = new InventoryClickEvent(player.getOpenInventory(), SlotType.CONTAINER, slot, ClickType.LEFT, InventoryAction.PICKUP_ONE);
        listener.onClick(event);
        return !event.isCancelled();
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = { "AIR", "DIAMOND", "STONE" })
    public void areItemsAllowed(Material type) throws InterruptedException {
        Assertions.assertTrue(isAllowed("BACKPACK_ALLOWANCE_" + type.name(), new ItemStack(type)));
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = { "SHULKER_BOX", "RED_SHULKER_BOX", "BLUE_SHULKER_BOX", "BLACK_SHULKER_BOX" })
    public void areShulkerBoxesAllowed(Material type) throws InterruptedException {
        Assertions.assertFalse(isAllowed("BACKPACK_ALLOWANCE_" + type.name(), new ItemStack(type)));
    }

    @ParameterizedTest
    @EnumSource(value = Material.class, names = { "AIR", "SHULKER_BOX" })
    public void testHotbarKey(Material type) throws InterruptedException {
        Player player = server.addPlayer();
        openMockBackpack(player, "BACKPACK_HOTBAR_" + type.name(), 9);

        int slot = 7;
        player.getInventory().setItem(slot, new ItemStack(type));
        InventoryClickEvent event = new InventoryClickEvent(player.getOpenInventory(), SlotType.CONTAINER, slot, ClickType.NUMBER_KEY, InventoryAction.PICKUP_ONE, slot);
        listener.onClick(event);

        Assertions.assertEquals(type != Material.AIR, event.isCancelled());
    }

}
