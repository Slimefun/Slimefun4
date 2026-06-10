import java.util.concurrent.TimeUnit
import java.io.ByteArrayOutputStream
import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.Files
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
        // Java 8 so the universal jar's bytecode loads on legacy (1.8+) servers running Java 8.
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

repositories {
    mavenLocal() // Java-8 dough modules (io.github.baked-libs:*:8.0.0-j8) built into the local Maven repo
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
    implementation(project(":compat-api"))
    // Java-8 dough (forked to core/dough, rebuilt locally as io.github.baked-libs:*:8.0.0-j8, shaded in).
    // The dough-api aggregator is unbuildable (legacy dough-protection deps), so consume modules directly.
    val doughVersion = "8.0.0-j8"
    implementation("io.github.baked-libs:dough-common:$doughVersion")
    implementation("io.github.baked-libs:dough-reflection:$doughVersion")
    implementation("io.github.baked-libs:dough-config:$doughVersion")
    implementation("io.github.baked-libs:dough-chat:$doughVersion")
    implementation("io.github.baked-libs:dough-data:$doughVersion")
    implementation("io.github.baked-libs:dough-skins:$doughVersion")
    implementation("io.github.baked-libs:dough-items:$doughVersion")
    implementation("io.github.baked-libs:dough-inventories:$doughVersion")
    implementation("io.github.baked-libs:dough-protection:$doughVersion")
    implementation("io.github.baked-libs:dough-recipes:$doughVersion")
    implementation("io.github.baked-libs:dough-updater:$doughVersion")
    implementation("io.github.baked-libs:dough-scheduling:$doughVersion")

    implementation("io.papermc:paperlib:1.0.8")
    implementation("commons-lang:commons-lang:2.6")
    // XSeries: cross-version Material/Sound/Particle resolution for the 1.8.8 universal jar.
    // Maps modern (1.13+ flattened) names to the version-appropriate Material/data. Java-8 compatible, shaded.
    implementation("com.github.cryptomorin:XSeries:9.10.0")

    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    // Java-8 universal floor: compile core against the oldest Bukkit API (1.8.8). Anything newer is
    // routed through compat-api / NMS modules. (Was: paper-api 26.1.2.)
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT") // transitive: gson, guava, bungeechat, snakeyaml (all Java-8, server-provided at runtime)
    // Compile-only shadow stubs of post-1.8 org.bukkit types (NamespacedKey, Keyed). NOT shaded into
    // the jar; the server's real classes are used on 1.12+. Legacy paths are version-guarded. See compat-stubs.
    compileOnly(project(":compat-stubs"))

    // Optional integration libs (authlib/worldedit/mcMMO/placeholderapi/clearlag/itemsadder/orebfuscator)
    // require modern JVMs and are deferred during the Java-8 port. Their hook classes are excluded from
    // compilation (see compileJava below) and will be re-added via reflection.

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
        // Raise javac's default 100-error cap so the Java-8 port can see the full remaining surface.
        options.compilerArgs.addAll(listOf("-Xmaxerrs", "2000", "-Xmaxwarns", "2000"))
        exclude("**/package-info.java")
        // Java-8 port: ClearLag/mcMMO/Orebfuscator hooks were rewritten to call their plugin APIs
        // purely via reflection (no third-party type on the compile classpath), so they compile and
        // re-activate at runtime when their plugin is present. WorldEdit and PlaceholderAPI remain
        // excluded: their hooks must subclass a third-party class (AbstractDelegateExtent /
        // PlaceholderExpansion), which is impossible without the Java-17 API on the Java-8 classpath.
        exclude(
            "**/integrations/WorldEditIntegration.java",
            "**/integrations/PlaceholderAPIIntegration.java"
        )
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
        relocate("com.cryptomorin.xseries", "io.github.thebusybiscuit.slimefun5.libraries.xseries")

        exclude("META-INF/**")

        exclude("io/github/bakedlibs/dough/skins/**")

        from(rootProject.projectDir) {
            include("LICENSE")
        }

        // Java-8 universal jar: the shaded XSeries 9.10.0 parses the server version with the regex
        // "MC: \d\.(\d+)", which hard-codes a single-digit major (the "1." of "1.x") and throws
        // "Failed to parse server version" on a 26.x major. Newer XSeries that handle arbitrary
        // majors require Java 11+, so instead we binary-patch this one constant in the relocated
        // XMaterial$Data class to "MC: (?:1\.)?(\d+)": for "1.x" group(1) is still the minor (e.g. 16),
        // for "26.x" it is the major (26 >= 13 -> flattened-material mode). Matching includes the
        // 2-byte UTF-8 length prefix so it targets the constant-pool entry precisely and is idempotent.
        doLast {
            val jarFile = archiveFile.get().asFile
            val entryName = "io/github/thebusybiscuit/slimefun5/libraries/xseries/XMaterial\$Data.class"

            fun u2(value: ByteArray): ByteArray = byteArrayOf((value.size shr 8 and 0xFF).toByte(), (value.size and 0xFF).toByte())
            val oldConst = u2("MC: \\d\\.(\\d+)".toByteArray(Charsets.UTF_8)) + "MC: \\d\\.(\\d+)".toByteArray(Charsets.UTF_8)
            val newConst = u2("MC: (?:1\\.)?(\\d+)".toByteArray(Charsets.UTF_8)) + "MC: (?:1\\.)?(\\d+)".toByteArray(Charsets.UTF_8)

            fun indexOf(haystack: ByteArray, needle: ByteArray): Int {
                outer@ for (i in 0..haystack.size - needle.size) {
                    for (j in needle.indices) {
                        if (haystack[i + j] != needle[j]) continue@outer
                    }
                    return i
                }
                return -1
            }

            val uri = URI.create("jar:" + jarFile.toURI())
            FileSystems.newFileSystem(uri, mapOf<String, String>()).use { fs ->
                val path = fs.getPath(entryName)
                if (!Files.exists(path)) {
                    logger.warn("XSeries 26.x patch: $entryName not found in jar")
                } else {
                    val content = Files.readAllBytes(path)
                    if (indexOf(content, newConst) >= 0) {
                        logger.lifecycle("XSeries 26.x patch: already applied")
                    } else {
                        val idx = indexOf(content, oldConst)
                        if (idx < 0) {
                            logger.warn("XSeries 26.x patch: version regex constant not found (XSeries version changed?)")
                        } else {
                            val patched = content.copyOfRange(0, idx) + newConst + content.copyOfRange(idx + oldConst.size, content.size)
                            Files.write(path, patched)
                            logger.lifecycle("XSeries 26.x patch: rewrote XMaterial\$Data version regex for non-1.x majors")
                        }
                    }
                }
            }
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

        // The interactive runServer picker publishes its selection here.
        if (addonsProp.isBlank()) {
            addonsProp = project.findProperty("resolvedAddons") as String? ?: ""
        }

        if (addonsProp.isBlank()) {
            addonsProp = project.findProperty("slimefunAddons") as String? ?: ""
        }

        if (addonsProp.isBlank()) {
            println("No addons specified (use -Paddons=Owner/Repo,... or the interactive runServer menu)")
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

// runServer is configured entirely from properties; the interactive version/addon picker lives in
// run.ps1 (Gradle's daemon has no attached console, so an in-build menu cannot read the keyboard and
// just falls back to defaults). Defaults here: latest version, core only.
//   ./run.ps1                                              interactive picker (recommended)
//   ./gradlew runServer "-PmcVersion=1.16.5"               (quote -P args in PowerShell 5.1!)
//   ./gradlew runServer "-PmcVersion=1.8.8" "-Paddons=Owner/Repo,Owner/Repo"
val runServerMcVer = (project.findProperty("mcVersion") as String?)?.takeIf { it.isNotBlank() } ?: "26.1.2"

// Addons to build for runServer: -Paddons (comma-separated Owner/Repo) selects them; otherwise none
// (core only). -PskipAddons is still accepted as an explicit "no addons".
val runServerAddons = if (project.hasProperty("skipAddons")) "" else (project.findProperty("addons") as String? ?: "")
project.extra.set("resolvedAddons", runServerAddons)

// The plugin jar is Java-8 bytecode (loads on every server), but the SERVER JVM must match the
// Minecraft version's own Java requirement. Map the selected MC version to the right launcher JDK.
fun requiredJavaFor(mc: String): Int {
    val parts = mc.split(".")
    val major = parts.getOrNull(0)?.toIntOrNull() ?: 0
    val minor = parts.getOrNull(1)?.toIntOrNull() ?: 0
    val patch = parts.getOrNull(2)?.toIntOrNull() ?: 0
    return when {
        major >= 26 -> 25
        major == 1 && minor >= 21 -> 21
        major == 1 && minor == 20 && patch >= 5 -> 21
        major == 1 && minor >= 18 -> 17
        major == 1 && minor == 17 -> 16
        else -> 8
    }
}

tasks.runServer {
    dependsOn(tasks.shadowJar)
    // Build addons only when the picker (or -Paddons) selected at least one. Selecting none in the
    // menu, or passing -PskipAddons, or a non-interactive invocation = core-only boot.
    if (runServerAddons.isNotBlank()) {
        dependsOn(cloneAndBuildAddons)
    }
    minecraftVersion(runServerMcVer)
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(requiredJavaFor(runServerMcVer)))
    })

    // Per-version run directory: Paper's world data and config schema are not backward-compatible
    // across MC versions, so each version gets an isolated dir (avoids cross-version world/config crashes).
    val perVersionRunDir = layout.projectDirectory.dir("run/$runServerMcVer")
    runDirectory.set(perVersionRunDir)
    doFirst {
        val runDirFile = perVersionRunDir.asFile
        runDirFile.mkdirs()
        runDirFile.resolve("eula.txt").writeText("eula=true\n")
    }
}


