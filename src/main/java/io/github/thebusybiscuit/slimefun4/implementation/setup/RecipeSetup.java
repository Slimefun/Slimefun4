package io.github.thebusybiscuit.slimefun4.implementation.setup;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

public final class RecipeSetup {

    public static void setup() {
        Slimefun.getRecipeService().loadAllRecipes();
    }
    
}
