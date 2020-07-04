package io.github.thebusybiscuit.slimefun4.core.handlers;

import io.github.thebusybiscuit.slimefun4.api.exceptions.IncompatibleItemHandlerException;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlock;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * This {@link ItemHandler} is called whenever a {@link Player} interacts with
 * this {@link MultiBlock}.
 * Note that this {@link MultiBlockInteractionHandler} should be assigned to
 * a class that inherits from {@link MultiBlockMachine}.
 *
 * @author TheBusyBiscuit
 * @see ItemHandler
 * @see MultiBlock
 * @see MultiBlockMachine
 */
@FunctionalInterface
public interface MultiBlockInteractionHandler extends ItemHandler {

    boolean onInteract(Player p, MultiBlock mb, Block b);

    @Override
    default Class<? extends ItemHandler> getIdentifier() {
        return MultiBlockInteractionHandler.class;
    }

    @Override
    default Optional<IncompatibleItemHandlerException> validate(SlimefunItem item) {
        if (!(item instanceof MultiBlockMachine)) {
            return Optional.of(new IncompatibleItemHandlerException("Only class inheriting 'MultiBlockMachine' can have a MultiBlockInteractionHandler", item, this));
        }

        return Optional.empty();
    }
}
