plugins {
    `java-library`
    `maven-publish`
    jacoco
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.sonarqube") version "5.0.0.4638"
}

// Needed for SonarScanner to work properly. 5.x requires java 17

val targetJavaVersion: Int = property("java.version").toString().toInt()
val encoding: String = property("project.encoding").toString()
val minecraftVersion: String = property("minecraft.version").toString()

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")
    maven("https://nexus.neetgames.com/repository/maven-public")
    maven("https://repo.walshy.dev/public")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://libraries.minecraft.net")
}

dependencies {
    // Api is similar to "implementation" but that these deps are also exposed transitively
    api("com.github.baked-libs.dough:dough-api:baf2d79f62")
    api("io.papermc:paperlib:1.0.8")
    api("commons-lang:commons-lang:2.6")

    compileOnly("org.spigotmc:spigot-api:$minecraftVersion-R0.1-SNAPSHOT")
    compileOnly("com.mojang:authlib:6.0.52")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")

    // Plugin dependencies
    compileOnly("com.sk89q.worldedit:worldedit-core:7.2.19")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.19")
    compileOnly("com.gmail.nossr50.mcMMO:mcMMO:2.1.230")
    compileOnly("me.clip:placeholderapi:2.11.5")
    compileOnly("me.minebuilders:clearlag-core:3.1.6")
    compileOnly("com.github.LoneDev6:itemsadder-api:3.6.1")
    compileOnly("net.imprex:orebfuscator-api:5.4.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.mockito:mockito-core:5.10.0")
    testImplementation("org.slf4j:slf4j-simple:2.0.9")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.65.0")

    // Override MockBukkit's Paper to a pinned slightly older version
    // This is because MockBukkit currently fails after this PR: https://github.com/PaperMC/Paper/pull/9629
    testImplementation("io.papermc.paper:paper-api:1.20.4-R0.1-20240205.114523-90")
}

group = "com.github.slimefun"
version = "4.9-UNOFFICIAL"
description = "Slimefun"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))

java {
    withSourcesJar()
}

tasks {
    withType<JavaCompile>().configureEach {
        options.release = targetJavaVersion
        options.encoding = encoding

        // package info files are only important for Javadocs
        // We can safely exclude them from the final jar -->
        exclude("**/package-info.java")
    }
    withType<Javadoc>().configureEach {
        options.encoding = encoding
    }
    javadoc {
        setDestinationDir(file("javadocs"))
        options {
            title = "Slimefun4 - Javadocs"
            windowTitle = "Slimefun4 - Javadocs"
            // doclet = "org.gradle.external.javadoc.StandardJavadocDocletOptions"
            // FIXME offline links, groups, and additional options.
        }

    }
    assemble {
        dependsOn(shadowJar)

    }
    jar {
        archiveClassifier.set("original")
        archiveVersion.set("v${project.version}")
    }
    shadowJar {
        archiveClassifier.set("")
        archiveVersion.set("v${project.version}")

        // Relocations
        val destination = "io.github.thebusybiscuit.slimefun4.libraries"
        relocate("io.github.bakedlibs.dough", "$destination.dough")
        relocate("io.papermc.lib", "$destination.paperlib")
        relocate("org.apache.commons.lang", "$destination.commons.lang")
        // Merge the META-INF files
        mergeServiceFiles()
    }
    processResources {

        include("plugin.yml")
        include("config.yml")
        include("item-models.yml")
        include("wiki.json")
        include("languages/translators.json")
        include("tags/*.json")
        include("biome-maps/*.json")
        include("languages/**/*.yml")
        filesMatching("plugin.yml") {
            expand("project.version" to project.version)
        }
    }
    test {
        useJUnitPlatform()
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
        pom {
            scm {
                inceptionYear = "2013"
                packaging = "jar"
                issueManagement {
                    system = "GitHub Issues"
                    url = "https://github.com/Slimefun/Slimefun4/issues"
                }
                licenses {
                    license {
                        name = "GNU General Public License v3.0"
                        url = "https://github.com/Slimefun/Slimefun4/blob/master/LICENSE"
                        distribution = "repo"
                    }
                }

            }
        }
    }
}