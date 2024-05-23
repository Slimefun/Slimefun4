package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * The give command cheat items for player, but does not send a message to the receiver.
 *
 * @author ybw0014
 */
class SilentGiveCommand extends AbstractGiveCommand {
    @ParametersAreNonnullByDefault
    SilentGiveCommand(Slimefun plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "silent_give", true);
    }
}
