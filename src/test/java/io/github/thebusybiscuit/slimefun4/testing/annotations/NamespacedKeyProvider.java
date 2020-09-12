package io.github.thebusybiscuit.slimefun4.testing.annotations;

import java.util.Arrays;
import java.util.stream.Stream;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.mockito.Mockito;

public class NamespacedKeyProvider implements ArgumentsProvider, AnnotationConsumer<NamespacedKeySource> {

    private String namespace;
    private String[] keys;

    @Override
    public void accept(NamespacedKeySource source) {
        namespace = source.namespace();
        keys = source.keys();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        Plugin plugin = Mockito.mock(Plugin.class);
        Mockito.when(plugin.getName()).thenReturn(namespace);

        return Arrays.stream(keys).map(key -> new NamespacedKey(plugin, key)).map(Arguments::of);
    }
}
