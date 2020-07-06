package io.github.thebusybiscuit.slimefun4.core.guide;

class GuideEntry<T> {

    private final T object;
    private int page;

    GuideEntry(T object, int page) {
        this.object = object;
        this.page = page;
    }

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
