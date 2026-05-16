import java.util.concurrent.TimeUnit
plugins {
    java
    id("com.gradleup.shadow")
    id("io.github.intisy.github-gradle")
    id("xyz.jpenilla.run-paper")
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
        languageVersion.set(JavaLanguageVersion.of(25))
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
    implementation("com.github.Slimefun.dough:dough-api:cb22e71335")
    implementation("io.papermc:paperlib:1.0.8")
    implementation("commons-lang:commons-lang:2.6")

    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    compileOnly("io.papermc.paper:paper-api:${property("paperApiVersion")}")
    compileOnly("com.mojang:authlib:6.0.52") { isTransitive = false }

    compileOnly("com.sk89q.worldedit:worldedit-core:7.3.9") { isTransitive = false }
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.3.9") { isTransitive = false }
    compileOnly("com.gmail.nossr50.mcMMO:mcMMO:2.2.029") { isTransitive = false }
    compileOnly("me.clip:placeholderapi:2.11.6") { isTransitive = false }
    compileOnly("me.minebuilders:clearlag-core:3.1.6") { isTransitive = false }
    compileOnly("com.github.LoneDev6:itemsadder-api:3.6.1") { isTransitive = false }
    compileOnly("net.imprex:orebfuscator-api:5.4.0") { isTransitive = false }

    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.15.2")
    testImplementation("org.slf4j:slf4j-simple:2.0.16")
    testImplementation("org.mockbukkit.mockbukkit:mockbukkit-v26.1.2:4.113.1") {
        exclude(group = "org.jetbrains", module = "annotations")
    }
}

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
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }

    jar {
        enabled = false
    }

    shadowJar {
        archiveFileName.set("Slimefun v${project.version}-MC26.1.2.jar")

        relocate("io.github.bakedlibs.dough", "io.github.thebusybiscuit.slimefun5.libraries.dough")
        relocate("io.papermc.lib", "io.github.thebusybiscuit.slimefun5.libraries.paperlib")
        relocate("org.apache.commons.lang", "io.github.thebusybiscuit.slimefun5.libraries.commons.lang")

        exclude("META-INF/**")

        exclude("io/github/bakedlibs/dough/skins/**")

        from(rootProject.projectDir) {
            include("LICENSE")
        }
    }

    test {
        useJUnitPlatform()
    }

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
            addonsProp = project.findProperty("slimefunAddons") as String? ?: ""
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
                val aheadProc = ProcessBuilder("git", "rev-list", "--count", "origin/HEAD..HEAD")
                    .directory(repoDir).redirectErrorStream(true).start()
                aheadProc.waitFor()
                val aheadCount = aheadProc.inputStream.bufferedReader().readText().trim().toIntOrNull() ?: 0
                if (aheadCount > 0) {
                    println("  Local branch is $aheadCount commit(s) ahead of origin - preserving local fixes.")
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
    minecraftVersion("26.1.2")
}


