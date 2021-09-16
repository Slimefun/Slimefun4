package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;

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
    SOLID("", "&fCet Androïde fonctionne avec du carburant solide", "&fe.g. charbon, bois, etc..."),

    /**
     * This {@link ProgrammableAndroid} runs on liquid fuel, e.g. Fuel, Oil or Lava
     */
    LIQUID("", "&fCet Androïde fonctionna vec du carburant liquide", "&fe.g. Lave, pétrole, carburants, etc..."),

    /**
     * This {@link ProgrammableAndroid} runs on nuclear fuel, e.g. Uranium
     */
    NUCLEAR("", "&fCet Androïde fonctionne avec du carburant radioactif", "&fe.g. Uranium, neptunium ou uranium enrichi");

    private final String[] lore;

    AndroidFuelSource(@Nonnull String... lore) {
        this.lore = lore;
    }

    /**
     * This returns a display {@link ItemStack} for this {@link AndroidFuelSource}.
     * 
     * @return An {@link ItemStack} to display
     */
    @Nonnull
    public ItemStack getItem() {
        return new CustomItemStack(HeadTexture.GENERATOR.getAsItemStack(), "&8\u21E9 &cAlimentation en carburant&8\u21E9", lore);
    }

}
