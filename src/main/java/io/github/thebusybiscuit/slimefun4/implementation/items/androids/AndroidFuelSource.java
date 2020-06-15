package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

/**
 * This enum covers all different fuel sources a {@link ProgrammableAndroid} can have.
 * 
 * @author TheBusyBiscuit
 *
 */
public enum AndroidFuelSource {

    /**
     * This {@link ProgrammableAndroid} runs on solid fuel, e.g. Wood or coal
     */
    SOLID("", "&rThis Android runs on solid Fuel", "&re.g. Coal, Wood, etc..."),

    /**
     * This {@link ProgrammableAndroid} runs on liquid fuel, e.g. Fuel, Oil or Lava
     */
    LIQUID("", "&rThis Android runs on liquid Fuel", "&re.g. Lava, Oil, Fuel, etc..."),

    /**
     * This {@link ProgrammableAndroid} runs on nuclear fuel, e.g. Uranium
     */
    NUCLEAR("", "&rThis Android runs on radioactive Fuel", "&re.g. Uranium, Neptunium or Boosted Uranium");

    private final String[] lore;

    private AndroidFuelSource(String... lore) {
        this.lore = lore;
    }

    public ItemStack getItem() {
        return new CustomItem(SlimefunItems.COAL_GENERATOR, "&8\u21E9 &cFuel Input &8\u21E9", lore);
    }

}
