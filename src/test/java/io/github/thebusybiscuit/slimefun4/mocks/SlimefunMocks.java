package io.github.thebusybiscuit.slimefun4.mocks;

import static org.mockito.Mockito.when;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mockito.Mockito;

import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public final class SlimefunMocks {

    private static final Category category = new Category(new NamespacedKey(SlimefunPlugin.instance, "test"), new ItemStack(Material.EMERALD));

    private SlimefunMocks() {}

    public static Inventory mockInventory(InventoryType type, ItemStack... contents) {
        Inventory inv = Mockito.mock(Inventory.class);

        when(inv.getType()).thenReturn(type);
        when(inv.getContents()).thenReturn(contents);

        return inv;
    }

    public static SlimefunItem mockSlimefunItem(String id, ItemStack item) {
        return new MockSlimefunItem(category, item, id);
    }

    public static VanillaItem mockVanillaItem(Material type, boolean enabled) {
        VanillaItem item = new VanillaItem(category, new ItemStack(type), type.name(), null, new ItemStack[9]);
        SlimefunPlugin.getItemCfg().setValue(type.name() + ".enabled", enabled);
        return item;
    }

}
