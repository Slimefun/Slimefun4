plugins {
    java
    id("com.gradleup.shadow") version "9.3.2"
    id("io.github.intisy.github-gradle") version "1.3.8"
}

group = "com.github.slimefun"
version = "5.0.0-UNOFFICIAL"
description = "Slimefun is a Paper plugin that simulates a modpack-like atmosphere by adding over 500 new items and recipes to your Minecraft Server."

github {
    accessToken = System.getenv("GITHUB_TOKEN") ?: ""
}

publishGithub {
    tag = System.getenv("GITHUB_REF_NAME")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")      // Spigot
    maven("https://repo.papermc.io/repository/maven-public/")                   // Paper
    maven("https://jitpack.io")                                                 // dough, MockBukkit, ItemsAdder
    maven("https://maven.enginehub.org/repo/")                                  // WorldEdit
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")  // PlaceholderAPI
    maven("https://nexus.neetgames.com/repository/maven-public")                // mcMMO
    maven("https://repo.walshy.dev/public")                                     // ClearLag
    maven("https://repo.codemc.io/repository/maven-public/")                    // Orebfuscator
}

dependencies {
    // Shaded dependencies (bundled into the final jar)
    implementation("com.github.Slimefun.dough:dough-api:cb22e71335")
    implementation("io.papermc:paperlib:1.0.8")
    implementation("commons-lang:commons-lang:2.6")

    // Compile-only (provided at runtime by the server)
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("com.mojang:authlib:6.0.52") { isTransitive = false }

    // Third-party plugin integrations (soft dependencies)
    compileOnly("com.sk89q.worldedit:worldedit-core:7.3.9") { isTransitive = false }
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.3.9") { isTransitive = false }
    compileOnly("com.gmail.nossr50.mcMMO:mcMMO:2.2.029") { isTransitive = false }
    compileOnly("me.clip:placeholderapi:2.11.6") { isTransitive = false }
    compileOnly("me.minebuilders:clearlag-core:3.1.6") { isTransitive = false }
    compileOnly("com.github.LoneDev6:itemsadder-api:3.6.1") { isTransitive = false }
    compileOnly("net.imprex:orebfuscator-api:5.4.0") { isTransitive = false }

    // Testing
    // Note: paper-api version must match what MockBukkit was built against (1.21.11).
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.15.2")
    testImplementation("org.slf4j:slf4j-simple:2.0.16")
    testImplementation("org.mockbukkit.mockbukkit:mockbukkit-v1.21:4.107.0") {
        exclude(group = "org.jetbrains", module = "annotations")
    }
}

// Make compileOnly dependencies available on the test classpath
// (mirrors Maven's 'provided' scope behavior — tests need paper-api, jsr305, etc.)
configurations {
    testImplementation {
        extendsFrom(configurations.compileOnly.get())
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-Xlint:-deprecation")
        exclude("**/package-info.java")
    }

    compileTestJava {
        options.encoding = "UTF-8"
    }

    processResources {
        // Replace ${version} in plugin.yml with the project version
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }

    jar {
        // Disable the default jar — only shadowJar should be produced
        enabled = false
    }

    shadowJar {
        // Output: "Slimefun v5.0.0.jar"
        archiveFileName.set("Slimefun v${project.version}.jar")

        // Relocate shaded dependencies to avoid classpath conflicts
        relocate("io.github.bakedlibs.dough", "io.github.thebusybiscuit.slimefun4.libraries.dough")
        relocate("io.papermc.lib", "io.github.thebusybiscuit.slimefun4.libraries.paperlib")
        relocate("org.apache.commons.lang", "io.github.thebusybiscuit.slimefun4.libraries.commons.lang")

        // Exclude META-INF from all shaded dependencies
        exclude("META-INF/**")

        // Exclude dough skins package - replaced by VersionedPlayerHead
        // to avoid IncompatibleClassChangeError with final GameProfile in MC 1.21.5+
        exclude("io/github/bakedlibs/dough/skins/**")

        // Include LICENSE
        from(rootProject.projectDir) {
            include("LICENSE")
        }
    }

    test {
        useJUnitPlatform()
    }

    // Make 'build' produce the shaded jar
    build {
        dependsOn(shadowJar)
    }
}
