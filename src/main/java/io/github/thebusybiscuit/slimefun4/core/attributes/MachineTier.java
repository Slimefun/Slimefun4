package io.github.thebusybiscuit.slimefun4.core.attributes;

import javax.annotation.Nonnull;

public enum MachineTier {

    BASIC("&eBasique"),
    AVERAGE("&6Simple"),
    MEDIUM("&aMoyen"),
    GOOD("&2Bon"),
    ADVANCED("&6Avancé"),
    END_GAME("&4Extrême");

    private final String prefix;

    MachineTier(@Nonnull String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return prefix;
    }

}
