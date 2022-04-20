package io.github.thebusybiscuit.slimefun4.test.providers;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(NamespacedKeyProvider.class)
public @interface NamespacedKeySource {

    String namespace();

    String[] keys();

}
