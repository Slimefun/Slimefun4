package io.github.thebusybiscuit.slimefun4.storage.backend.binary.serializers;

import io.github.bakedlibs.dough.nbt.tags.Tag;

public interface BinarySerialize<O, T extends Tag<?>> {

    public T serialize(O obj);

    public O deserialize(T obj);
}
