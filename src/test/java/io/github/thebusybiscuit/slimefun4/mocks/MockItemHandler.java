package io.github.thebusybiscuit.slimefun4.mocks;

import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;

public class MockItemHandler implements ItemHandler {

    @Override
    public Class<? extends ItemHandler> getIdentifier() {
        return getClass();
    }

}
