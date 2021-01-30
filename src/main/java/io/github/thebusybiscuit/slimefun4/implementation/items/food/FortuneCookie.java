package io.github.thebusybiscuit.slimefun4.implementation.items.food;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemConsumptionHandler;
import io.github.thebusybiscuit.slimefun4.core.services.LocalizationService;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link FortuneCookie} is a rather simple {@link SlimefunItem}, it's a cookie which
 * sends the {@link Player} who ate it a random text message.
 * The messages can be defined in the {@link LocalizationService}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class FortuneCookie extends SimpleSlimefunItem<ItemConsumptionHandler> {

    @ParametersAreNonnullByDefault
    public FortuneCookie(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemConsumptionHandler getItemHandler() {
        return (e, p, item) -> {
            List<String> messages = SlimefunPlugin.getLocalization().getMessages(p, "messages.fortune-cookie");
            String message = messages.get(ThreadLocalRandom.current().nextInt(messages.size()));

            p.sendMessage(ChatColors.color(message));
        };
    }

}
