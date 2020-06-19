package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import org.bukkit.inventory.ItemStack;

/**
 * This enum covers all different fuel sources a {@link ProgrammableAndroid} can have.
 *
 * @author TheBusyBiscuit
 */
public enum AndroidFuelSource {

    /**
     * This {@link ProgrammableAndroid} runs on solid fuel, e.g. Wood or coal
     */
    SOLID("", "&r这类机器人需要固态燃料", "&r例如煤, 原木等..."),

    /**
     * This {@link ProgrammableAndroid} runs on liquid fuel, e.g. Fuel, Oil or Lava
     */
    LIQUID("", "&r这类机器人需要液态燃料", "&r例如岩浆, 原油, 燃油等..."),

    /**
     * This {@link ProgrammableAndroid} runs on nuclear fuel, e.g. Uranium
     */
    NUCLEAR("", "&r这类机器人需要放射性燃料", "&r例如铀, 镎或强化铀");

    private final String[] lore;

    AndroidFuelSource(String... lore) {
        this.lore = lore;
    }

    /**
     * This returns a display {@link ItemStack} for this {@link AndroidFuelSource}.
     *
     * @return An {@link ItemStack} to display
     */
    public ItemStack getItem() {
        return new CustomItem(SlimefunItems.COAL_GENERATOR, "&8\u21E9 &c燃料输入口 &8\u21E9", lore);
    }

}