package io.github.thebusybiscuit.slimefun4.test.mocks;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;

public class MockProfile extends PlayerProfile {

    public MockProfile(@Nonnull OfflinePlayer p) {
        super(p);
    }

}
