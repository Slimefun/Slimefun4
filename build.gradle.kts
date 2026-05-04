import java.util.concurrent.TimeUnit
plugins {
    java
    id("com.gradleup.shadow") version "9.3.2"
    id("io.github.intisy.github-gradle") version "1.8.2.1"
    id("xyz.jpenilla.run-paper") version "2.2.3"
}

group = "com.github.slimefun"
version = "5.0.0-UNOFFICIAL"
description = "Slimefun is a Paper plugin that simulates a modpack-like atmosphere by adding over 500 new items and recipes to your Minecraft Server."

github {
    accessToken = System.getenv("GITHUB_TOKEN") ?: ""
    publish {
        tag = System.getenv("GITHUB_REF_NAME")
    }
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
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
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

val cloneAndBuildAddons by tasks.registering {
    group = "slimefun"
    description = "Clones or pulls and compiles specified addons from GitHub"

    doLast {
        var addonsProp = project.findProperty("addons") as String? ?: ""
        
        if (addonsProp.isBlank()) {
            val defaultAddonsFile = file("default-addons.txt")
            if (defaultAddonsFile.exists()) {
                addonsProp = defaultAddonsFile.readLines()
                    .filter { it.isNotBlank() && !it.startsWith("#") }
                    .joinToString(",")
            }
        }

        if (addonsProp.isBlank()) {
            println("No addons specified in -Paddons or default-addons.txt")
            return@doLast
        }

        val addonsSrcDir = project.layout.buildDirectory.dir("addons-src").get().asFile
        addonsSrcDir.mkdirs()

        val pluginsDir = project.layout.projectDirectory.dir("run/plugins").asFile
        pluginsDir.mkdirs()

        val addons = addonsProp.split(",")

        fun getGitHash(dir: File): String {
            try {
                val proc = ProcessBuilder("git", "rev-parse", "HEAD")
                    .directory(dir)
                    .redirectErrorStream(true)
                    .start()
                proc.waitFor()
                return proc.inputStream.bufferedReader().readText().trim()
            } catch (e: Exception) {
                return ""
            }
        }

        val buildTimeoutMinutes = 5L

        fun runProcess(pb: ProcessBuilder, timeoutMinutes: Long = buildTimeoutMinutes): Int {
            pb.redirectErrorStream(true)
            pb.redirectInput(ProcessBuilder.Redirect.from(
                if (org.gradle.internal.os.OperatingSystem.current().isWindows) File("NUL") else File("/dev/null")
            ))
            val proc = pb.start()
            val output = StringBuilder()
            val reader = proc.inputStream.bufferedReader()
            val readerThread = Thread {
                reader.forEachLine { output.appendLine(it) }
            }
            readerThread.start()
            val finished = proc.waitFor(timeoutMinutes, TimeUnit.MINUTES)
            if (!finished) {
                proc.destroyForcibly()
                readerThread.join(2000)
                println(output)
                println("ERROR: Process timed out after $timeoutMinutes minutes.")
                return -1
            }
            readerThread.join(2000)
            val exitCode = proc.exitValue()
            if (exitCode != 0) {
                println(output)
            }
            return exitCode
        }

        for (addon in addons) {
            val parts = addon.split("/")
            if (parts.size != 2) {
                println("Invalid addon format: $addon. Expected Owner/Repo")
                continue
            }
            val repo = parts[1]

            val repoDir = File(addonsSrcDir, repo)
            val isWindows = org.gradle.internal.os.OperatingSystem.current().isWindows

            val oldHash = if (repoDir.exists()) getGitHash(repoDir) else ""

            if (repoDir.exists()) {
                println("Pulling latest for $addon...")
                runProcess(ProcessBuilder("git", "fetch", "--all").directory(repoDir), 2)
                runProcess(ProcessBuilder("git", "remote", "set-head", "origin", "-a").directory(repoDir), 1)
                // Only reset hard if we have no local commits ahead of origin.
                // Local fix commits (not yet pushed) must survive runServer restarts.
                val aheadProc = ProcessBuilder("git", "rev-list", "--count", "origin/HEAD..HEAD")
                    .directory(repoDir).redirectErrorStream(true).start()
                aheadProc.waitFor()
                val aheadCount = aheadProc.inputStream.bufferedReader().readText().trim().toIntOrNull() ?: 0
                if (aheadCount > 0) {
                    println("  Local branch is $aheadCount commit(s) ahead of origin — preserving local fixes.")
                } else {
                    runProcess(ProcessBuilder("git", "reset", "--hard", "origin/HEAD").directory(repoDir), 1)
                }
            } else {
                println("Cloning $addon...")
                runProcess(ProcessBuilder("git", "clone", "https://github.com/$addon.git").directory(addonsSrcDir), 5)
            }

            val newHash = getGitHash(repoDir)
            val libsDir = File(repoDir, "build/libs")
            val jars = libsDir.listFiles { file: File -> file.name.endsWith(".jar") && !file.name.endsWith("-javadoc.jar") && !file.name.endsWith("-sources.jar") }
            val hasCompiledJar = jars != null && jars.isNotEmpty()

            // If local branch is ahead of origin, we have unpushed fixes — always rebuild.
            val aheadCheck = ProcessBuilder("git", "rev-list", "--count", "origin/HEAD..HEAD")
                .directory(repoDir).redirectErrorStream(true).start()
            aheadCheck.waitFor()
            val localAhead = aheadCheck.inputStream.bufferedReader().readText().trim().toIntOrNull() ?: 0

            if (oldHash == newHash && oldHash.isNotBlank() && hasCompiledJar && localAhead == 0) {
                println("No updates found for $addon. Skipping build.")
                val targetJar = jars!!.firstOrNull { it.name.contains("v") || it.name.contains("shadow") } ?: jars!![0]
                println("Copying ${targetJar.name} to plugins folder...")
                targetJar.copyTo(File(pluginsDir, targetJar.name), overwrite = true)
                continue
            }

            println("Building $addon...")
            val gradlewCmd = if (isWindows) "gradlew.bat" else "./gradlew"
            val buildPb = if (isWindows) {
                ProcessBuilder("cmd", "/c", "$gradlewCmd shadowJar")
            } else {
                ProcessBuilder("sh", "-c", "$gradlewCmd shadowJar")
            }
            buildPb.directory(repoDir)
            val exitCode = runProcess(buildPb, buildTimeoutMinutes)

            if (exitCode != 0) {
                println("WARNING: Build failed for $addon (Exit Code: $exitCode). Skipping.")
                continue
            }

            val newJars = libsDir.listFiles { file: File -> file.name.endsWith(".jar") && !file.name.endsWith("-javadoc.jar") && !file.name.endsWith("-sources.jar") }
            if (newJars != null && newJars.isNotEmpty()) {
                val targetJar = newJars.firstOrNull { it.name.contains("v") || it.name.contains("shadow") } ?: newJars[0]
                println("Copying ${targetJar.name} to plugins folder...")
                targetJar.copyTo(File(pluginsDir, targetJar.name), overwrite = true)
            } else {
                println("WARNING: No compiled jar found for $addon")
            }
        }
    }
}

tasks.runServer {
    dependsOn(tasks.shadowJar, cloneAndBuildAddons)
    minecraftVersion("1.21.4")

    doFirst {
        val sfJar = tasks.shadowJar.get().archiveFile.get().asFile
        val pluginsDir = project.layout.projectDirectory.dir("run/plugins").asFile
        pluginsDir.mkdirs()
        sfJar.copyTo(File(pluginsDir, sfJar.name), overwrite = true)
    }
}
