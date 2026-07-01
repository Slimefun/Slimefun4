package org.bukkit.inventory;

import java.util.List;
import java.util.function.Predicate;

import org.bukkit.Material;

/**
 * Compile-only stub for {@code org.bukkit.inventory.RecipeChoice} (Minecraft 1.13+) and its nested
 * {@code MaterialChoice}. Not shaded; on modern servers the real types are used at runtime. Slimefun
 * only casts to {@code MaterialChoice} and reads {@code getChoices()}, so only those members are
 * declared.
 *
 * <p>
 * {@code RecipeChoice} is a real interface, but {@code MaterialChoice} is a real concrete class — it
 * must be stubbed as a class (not an interface) so that {@code getChoices()} compiles to
 * {@code invokevirtual} and resolves against the real class at runtime.
 */
public interface RecipeChoice extends Predicate<ItemStack> {

    ItemStack getItemStack();

    abstract class MaterialChoice implements RecipeChoice {

        public abstract List<Material> getChoices();
    }
}
