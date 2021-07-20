package io.github.thebusybiscuit.slimefun4.test.mocks;

import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;

public class MockItemHandler implements ItemHandler {

    @Override
    public Class<? extends ItemHandler> getIdentifier() {
        return getClass();
    }

}
