package io.github.thebusybiscuit.slimefun4.test.mocks;

import javax.annotation.Nonnull;

import org.bukkit.OfflinePlayer;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;

public class MockProfile extends PlayerProfile {

    public MockProfile(@Nonnull OfflinePlayer p) {
        super(p);
    }

}
