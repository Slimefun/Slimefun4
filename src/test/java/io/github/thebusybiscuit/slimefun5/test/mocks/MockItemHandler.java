package io.github.thebusybiscuit.slimefun5.test.mocks;

import io.github.thebusybiscuit.slimefun5.api.items.ItemHandler;

public class MockItemHandler implements ItemHandler {

    @Override
    public Class<? extends ItemHandler> getIdentifier() {
        return getClass();
    }

}

