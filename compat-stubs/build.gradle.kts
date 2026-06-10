plugins {
    java
}

java {
    toolchain {
        // Match the core Java 8 floor.
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
    // The NamespacedKey(Plugin, String) ctor references org.bukkit.plugin.Plugin, which exists in 1.8.8.
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT") {
        isTransitive = false
    }
}
