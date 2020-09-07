package io.github.thebusybiscuit.slimefun4.core.guide;

import javax.annotation.Nonnull;

class GuideEntry<T> {

    private final T object;
    private int page;

    GuideEntry(@Nonnull T object, int page) {
        this.object = object;
        this.page = page;
    }

    @Nonnull
    public T getIndexedObject() {
        return object;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
