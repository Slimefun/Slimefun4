package io.github.thebusybiscuit.slimefun4.implementation.resources;

import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;

/**
 * This static setup class is used to register all default instances of
 * {@link GEOResource} that Slimefun includes out of the box.
 * 
 * @author TheBusyBiscuit
 *
 */
public final class GEOResourcesSetup {

    private GEOResourcesSetup() {}

    public static void setup() {
        new OilResource().register();
        new NetherIceResource().register();
        new UraniumResource().register();
        new SaltResource().register();
    }

}
