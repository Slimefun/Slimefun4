package io.github.thebusybiscuit.slimefun4.test.providers;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(SlimefunItemsProvider.class)
public @interface SlimefunItemsSource {

    String[] items();

}
