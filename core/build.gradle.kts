import java.util.concurrent.TimeUnit
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URI
import java.net.HttpURLConnection
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
plugins {
    java
    id("com.gradleup.shadow")
    id("io.github.intisy.github-gradle")
    id("xyz.jpenilla.run-paper")
}

group = "com.github.slimefun"
// The "-UNOFFICIAL" suffix is appended downstream (plugin.yml + jar name), so this is the bare number.
// Uses providers.exec (not ProcessBuilder) so reading git at configuration time stays compatible
// with Gradle's configuration cache.
// Version + displayVersion come from the shared Slimefun5/gradle snippet (the git-tag resolver + build
// suffix), reused by every addon so the logic lives in one place. Core's default fallback differs from
// the addons' (5.0.0 vs 1.0.0) - set it before applying.
extra["sfDefaultVersion"] = "5.0.0"
apply(from = "https://raw.githubusercontent.com/Slimefun5/gradle/stable/version.gradle")
val displayVersion = project.extra["displayVersion"].toString()
description = "Slimefun is a Paper plugin that simulates a modpack-like atmosphere by adding over 500 new items and recipes to your Minecraft Server."

github {
    auth {
        token = System.getenv("GITHUB_TOKEN") ?: ""
    }
    cli {
        isEnabled = true
        isFallback = true
    }
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
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")
    maven("https://nexus.neetgames.com/repository/maven-public")
    maven("https://repo.walshy.dev/public")
    maven("https://repo.codemc.io/repository/maven-public/")
}

dependencies {
    githubImplementation("Slimefun5:dough:4.0.6:all")

    implementation("io.papermc:paperlib:1.0.8")
    implementation("commons-lang:commons-lang:2.6")
    // XSeries: cross-version Material/Sound/Particle resolution, shaded.
    implementation("com.github.cryptomorin:XSeries:9.10.0")

    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    // Compile against the oldest Bukkit API (1.8.8); newer APIs go through the stubs module + reflection.
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    // Compile-only stubs of post-1.8 org.bukkit types; not shaded, real classes used at runtime.
    compileOnly(project(":stubs"))
    // Netty for the packet-translation ChannelDuplexHandler. compileOnly (the server ships Netty at
    // runtime, so nothing is bundled — no new runtime dependency). Pinned to 4.0.23 (the version MC 1.8
    // ships) so the compiler rejects any 4.1-only API and the bytecode resolves on every 1.8→26.x server.
    compileOnly("io.netty:netty-all:4.0.23.Final")

    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.15.2")
    testImplementation("org.slf4j:slf4j-simple:2.0.16")
    // Headless boot tests: MockBukkit mocks MC 26.1.2 (a runtime target), so MockBukkit.load(Slimefun.class)
    // enables the plugin + registers every item without a real server - catching registration/lore NPEs at
    // build time. Runs on its own Java-25 toolchain (below); the main jar stays Java-8 bytecode. Tests
    // compile against MockBukkit's real 26.1.2 API (NOT the 1.8.8 compileOnly + stubs used for main).
    // Mock MC 1.21 (not the fork's 26.x runtime): the bundled XSeries 9.10.0 is only version-patched in the
    // shadowJar, so against the raw test classpath it can't parse "26.x" — but 1.21 parses fine. The boot
    // check is version-agnostic for the registration/lore regressions we're guarding against.
    testImplementation("org.mockbukkit.mockbukkit:mockbukkit-v1.21:4.110.0") {
        exclude(group = "org.jetbrains", module = "annotations")
    }
    testImplementation("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    testCompileOnly("com.google.code.findbugs:jsr305:3.0.2")
}

// Tests deliberately do NOT extend compileOnly: main compiles against spigot-api 1.8.8 + the stubs, but
// the tests run on MockBukkit's real Paper 26.1.2 API, so pulling 1.8.8 + stubs onto the test classpath
// would collide with MockBukkit's org.bukkit classes.

val testJavaCompiler = javaToolchains.compilerFor { languageVersion.set(JavaLanguageVersion.of(25)) }
val testJavaLauncher = javaToolchains.launcherFor { languageVersion.set(JavaLanguageVersion.of(25)) }

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-Xlint:-deprecation")
        // Raise javac's default 100-error cap so the Java-8 port can see the full remaining surface.
        options.compilerArgs.addAll(listOf("-Xmaxerrs", "2000", "-Xmaxwarns", "2000"))
        exclude("**/package-info.java")
        // WorldEdit/PlaceholderAPI hooks subclass Java-17 API types, so they can't compile on Java 8.
        exclude(
            "**/integrations/WorldEditIntegration.java",
            "**/integrations/PlaceholderAPIIntegration.java"
        )
    }

    // Tests run on their own Java-25 toolchain (MockBukkit needs it); the main jar stays Java-8 bytecode,
    // which runs fine under the newer test JVM. foojay auto-provisions the JDK.
    compileTestJava {
        enabled = true
        options.encoding = "UTF-8"
        javaCompiler.set(testJavaCompiler)
        // Only the headless fork tests for now; the 109 upstream tests predate the Java-8 port and
        // don't all compile against the fork yet. Widen this include as they're brought back.
        include("**/BootSmokeTest.java")
        include("**/BackpackIdentityTest.java")
        include("**/ItemFamilyTest.java")
        include("**/TestMenuListenerCollectGuard.java")
        include("**/TestViewedInventoryGuard.java")
        include("**/TranslationConfigTest.java")
        include("**/PacketRenderTest.java")
        include("**/PacketRenderTest*")
        include("**/GuideBookDisplayTest.java")
        include("**/GuideBookDisplayTest*")
        include("**/EnchantTranslationTest.java")
        include("**/EnchantTranslationTest*")
        include("**/WikiTextLanguageTest.java")
        include("**/WikiTextLanguageTest*")
        include("**/CoverageTest.java")
        include("**/CoverageTest*")
        include("**/PacketReflectTest.java")
        include("**/PacketReflectTest*")
    }
    test {
        enabled = true
        useJUnitPlatform()
        javaLauncher.set(testJavaLauncher)
        // Isolate each test class in its own JVM: SlimefunItemSetup.setup() guards itself with a
        // JVM-static "registered once" flag, so any two test classes that both boot the full item
        // catalogue (e.g. BootSmokeTest and PacketRenderTest) would collide if Gradle reused one JVM
        // across the whole task.
        forkEvery = 1
        include("**/BootSmokeTest*")
        include("**/BackpackIdentityTest*")
        include("**/ItemFamilyTest*")
        include("**/TestMenuListenerCollectGuard*")
        include("**/TestViewedInventoryGuard*")
        include("**/TranslationConfigTest*")
        include("**/PacketRenderTest*")
        include("**/GuideBookDisplayTest*")
        include("**/EnchantTranslationTest*")
        include("**/WikiTextLanguageTest*")
        include("**/CoverageTest*")
        include("**/PacketReflectTest*")
    }

    processResources {
        // Declare the version as an input so changing -Partifact_version (or the build context) re-expands
        // plugin.yml instead of reusing a stale cached copy (which once shipped 5.0.0-UNOFFICIAL). The
        // reported version carries the build-context suffix so it matches the jar name.
        val pluginVersion = displayVersion
        inputs.property("version", pluginVersion)
        filesMatching("plugin.yml") {
            expand("version" to pluginVersion)
        }
    }

    jar {
        enabled = false
    }

    shadowJar {
        archiveBaseName.set("Slimefun")
        archiveVersion.set(displayVersion)
        archiveClassifier.set("")

        relocate("io.github.bakedlibs.dough", "io.github.thebusybiscuit.slimefun5.libraries.dough")
        relocate("io.papermc.lib", "io.github.thebusybiscuit.slimefun5.libraries.paperlib")
        relocate("org.apache.commons.lang", "io.github.thebusybiscuit.slimefun5.libraries.commons.lang")
        relocate("com.cryptomorin.xseries", "io.github.thebusybiscuit.slimefun5.libraries.xseries")

        exclude("META-INF/**")

        from(rootProject.projectDir) {
            include("LICENSE")
        }

        // XSeries 9.10.0's regex "MC: \d\.(\d+)" can't parse a 26.x major; binary-patch the relocated
        // XMaterial$Data constant to "MC: (?:1\.)?(\d+)" (a newer XSeries would need Java 11+).
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

            // Rewrite the jar by streaming through a temp file rather than opening a jar FileSystem.
            // FileSystems.newFileSystem registers in the JVM-global zip FS provider, which is shared
            // across the Gradle daemon - a prior run that failed to close (e.g. the jar was locked by a
            // running server) leaves a stale handle and the next build dies with
            // FileSystemAlreadyExistsException. Streaming is daemon-safe and lock-tolerant.
            var found = false
            var alreadyApplied = false
            var didPatch = false
            val temp = File(jarFile.parentFile, jarFile.name + ".xseries.tmp")

            ZipInputStream(jarFile.inputStream()).use { zin ->
                ZipOutputStream(temp.outputStream()).use { zout ->
                    var entry = zin.nextEntry
                    while (entry != null) {
                        val data = zin.readBytes()
                        var outData = data

                        if (entry.name == entryName) {
                            found = true
                            if (indexOf(data, newConst) >= 0) {
                                alreadyApplied = true
                            } else {
                                val idx = indexOf(data, oldConst)
                                if (idx >= 0) {
                                    outData = data.copyOfRange(0, idx) + newConst + data.copyOfRange(idx + oldConst.size, data.size)
                                    didPatch = true
                                }
                            }
                        }

                        zout.putNextEntry(ZipEntry(entry.name))
                        zout.write(outData)
                        zout.closeEntry()
                        entry = zin.nextEntry
                    }
                }
            }

            if (didPatch) {
                // The move can transiently fail on Windows (jar locked by the daemon/AV); retry, and if
                // it still fails, FAIL the build rather than silently shipping an unpatched core jar
                // (which makes Slimefun - and therefore every addon - fail to enable on a 26.x server).
                var moved = false
                var lastError: Exception? = null

                for (attempt in 1..10) {
                    try {
                        Files.move(temp.toPath(), jarFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                        moved = true
                        break
                    } catch (e: Exception) {
                        lastError = e
                        try { Thread.sleep(300) } catch (ignored: InterruptedException) { Thread.currentThread().interrupt() }
                    }
                }

                if (!moved) {
                    temp.delete()
                    throw GradleException("XSeries 26.x patch: could not replace the core jar after 10 attempts (${lastError?.message}). "
                        + "This usually means a running server still holds the jar - stop it (and any stale Gradle daemon) and rebuild. "
                        + "Refusing to ship an unpatched core jar (it would fail to start on a 26.x server).")
                }

                logger.lifecycle("XSeries 26.x patch: rewrote XMaterial\$Data version regex for non-1.x majors")
            } else {
                temp.delete()
                when {
                    alreadyApplied -> logger.lifecycle("XSeries 26.x patch: already applied")
                    // Never ship an unpatched jar: a missing/changed constant means the 26.x version fix
                    // is absent, so fail loudly instead of producing a core that cannot start on 26.x.
                    !found -> throw GradleException("XSeries 26.x patch: $entryName not found in jar - cannot ship core without the version fix")
                    else -> throw GradleException("XSeries 26.x patch: version regex constant not found (did XSeries change?) - core would fail to parse a 26.x server version")
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

    // Addons compile against the freshly-built core jar, so it must exist first.
    dependsOn(tasks.named("shadowJar"))

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

        val mcVer = (project.findProperty("mcVersion") as String?)?.takeIf { it.isNotBlank() } ?: "26.1.2"
        val pluginsDir = project.layout.projectDirectory.dir("run/$mcVer/plugins").asFile
        pluginsDir.mkdirs()

        val addons = addonsProp.split(",")

        // -PlocalAddons builds the existing addons-src working copy as-is: skips the git fetch/reset
        // (so local edits survive) and forces a rebuild. For iterating addon source against a live boot
        // before committing. Does NOT introduce any hard-coded paths - it reuses addonsSrcDir/<repo>.
        val localAddons = project.hasProperty("localAddons")
        if (localAddons) println("[localAddons] building working copies as-is (no git fetch/reset)")

        // Addon build files reference the core jar by a relative path valid only in the old layout;
        // rewrite it to the absolute jar path after each checkout (reset --hard reverts it every run).
        val coreJarFile = project.layout.buildDirectory.file("libs/Slimefun-$displayVersion.jar").get().asFile
        val coreJarPath = coreJarFile.absolutePath.replace("\\", "/")
        if (!coreJarFile.exists()) {
            println("WARNING: Core jar not found at ${coreJarFile.absolutePath} - addon compiles will fail until :core:shadowJar produces it.")
        }
        // Bump to force a one-time rebuild when the patching below changes.
        val addonBuildRecipe = "6"
        val coreJarRefRegex = Regex("""files\((["'])\.\./\.\./core/Slimefun5/core/build/libs/[^"']*\.jar\1\)""")
        fun patchCoreJarReference(repoDir: File) {
            for (name in listOf("build.gradle.kts", "build.gradle")) {
                val buildFile = File(repoDir, name)
                if (!buildFile.exists()) continue
                val text = buildFile.readText()
                if (coreJarRefRegex.containsMatchIn(text)) {
                    val quote = coreJarRefRegex.find(text)!!.groupValues[1]
                    val patched = coreJarRefRegex.replace(text) { "files($quote$coreJarPath$quote)" }
                    if (patched != text) {
                        buildFile.writeText(patched)
                        println("Patched Slimefun core jar path in $name for ${repoDir.name}")
                    }
                }
            }
        }

        fun getGitHash(dir: File): String {
            try {
                val proc = ProcessBuilder("git", "-c", "safe.directory=*", "rev-parse", "HEAD")
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

        // Bytecode major version of a plugin jar's main class (Java 8 = 52); null for library jars.
        // Guards the skip-build optimization against stale higher-version jars from an earlier JDK.
        fun pluginMainClassVersion(jar: File): Int? {
            try {
                ZipFile(jar).use { zf ->
                    zf.getEntry("plugin.yml") ?: return null
                    val mainClass = zf.getInputStream(zf.getEntry("plugin.yml")).bufferedReader().use { reader ->
                        reader.lineSequence()
                            .firstOrNull { it.trimStart().startsWith("main:") }
                            ?.substringAfter("main:")?.trim()?.trim('"', '\'')
                    } ?: return null
                    val classEntry = zf.getEntry(mainClass.replace('.', '/') + ".class") ?: return null
                    zf.getInputStream(classEntry).use { ins ->
                        val header = ByteArray(8)
                        var off = 0
                        while (off < 8) {
                            val n = ins.read(header, off, 8 - off)
                            if (n < 0) break
                            off += n
                        }
                        if (off < 8) return null
                        return ((header[6].toInt() and 0xFF) shl 8) or (header[7].toInt() and 0xFF)
                    }
                }
            } catch (e: Exception) {
                return null
            }
        }

        fun jarHasPluginYml(jar: File): Boolean {
            return try {
                ZipFile(jar).use { it.getEntry("plugin.yml") != null }
            } catch (e: Exception) {
                false
            }
        }

        // The `version:` string embedded in a jar's plugin.yml (what the in-game installer displays),
        // or null if absent/unreadable. Used to detect a stale cached jar whose version has fallen behind
        // the addon's latest tag, so the skip-build optimization rebuilds instead of reusing it.
        fun jarPluginYmlVersion(jar: File): String? {
            return try {
                ZipFile(jar).use { zf ->
                    val entry = zf.getEntry("plugin.yml") ?: return null
                    zf.getInputStream(entry).bufferedReader().use { reader ->
                        reader.lineSequence()
                            .firstOrNull { it.trimStart().startsWith("version:") }
                            ?.substringAfter("version:")?.trim()?.trim('"', '\'')
                    }
                }
            } catch (e: Exception) {
                null
            }
        }

        // The set of .class entries the core jar provides. An addon must not ship duplicates of any of
        // them: a duplicate loaded by the addon's own classloader either has null static state (e.g.
        // core's Slimefun -> "Slimefun instance is null") or, when it appears in a shared core API
        // signature (e.g. the relocated slimefun5.libraries.keys.NamespacedKey or dough Config), triggers
        // a LinkageError "loader constraint violation". Core supplies every such class at runtime via the
        // addon's `depend: [Slimefun]` classloader link.
        val coreClassEntries: Set<String> = run {
            val names = HashSet<String>()
            if (coreJarFile.exists()) {
                try {
                    ZipFile(coreJarFile).use { zf ->
                        val en = zf.entries()
                        while (en.hasMoreElements()) {
                            val n = en.nextElement().name
                            if (n.endsWith(".class")) names.add(n)
                        }
                    }
                } catch (e: Exception) {
                    println("WARNING: could not read core jar entries for strip: ${e.message}")
                }
            }
            names
        }

        // Core bundles module jars as root-level resources (e.g. SlimefunMetrics.jar). github-gradle's
        // dependency metadata is scope-less, so `githubImplementation(InfinityLib)` drags the whole core
        // release jar into a consumer transitively - these nested jars ride along. They are NOT under
        // slimefun5/**, so the class strip (and the shadowJar exclude) miss them, and an OLD core's
        // SlimefunMetrics.jar still carries upstream slimefun4 refs. Core is runtime-provided, so an addon
        // must never ship them: strip any nested jar the core jar also provides.
        val coreNestedJarEntries: Set<String> = run {
            val names = HashSet<String>()
            if (coreJarFile.exists()) {
                try {
                    ZipFile(coreJarFile).use { zf ->
                        val en = zf.entries()
                        while (en.hasMoreElements()) {
                            val n = en.nextElement().name
                            if (n.endsWith(".jar")) names.add(n)
                        }
                    }
                } catch (e: Exception) {
                    println("WARNING: could not read core jar nested jars for strip: ${e.message}")
                }
            }
            names
        }

        // Strips from an addon jar every .class the core jar also provides (core's own classes + its
        // relocated libraries like keys/dough), while keeping addon-only classes and libs core does not
        // ship (e.g. the addon's relocated xseries). Never touches plugin.yml or other resources.
        fun stripBundledCoreClasses(jar: File) {
            if (coreClassEntries.isEmpty() && coreNestedJarEntries.isEmpty()) {
                return
            }
            val temp = File(jar.parentFile, jar.name + ".strip.tmp")
            var removed = 0
            ZipInputStream(jar.inputStream()).use { zin ->
                ZipOutputStream(temp.outputStream()).use { zout ->
                    var entry = zin.nextEntry
                    while (entry != null) {
                        val data = zin.readBytes()
                        val name = entry.name
                        val isBundledCoreClass = name.endsWith(".class") && coreClassEntries.contains(name)
                        val isBundledCoreNestedJar = name.endsWith(".jar") && coreNestedJarEntries.contains(name)
                        if (isBundledCoreClass || isBundledCoreNestedJar) {
                            removed++
                        } else {
                            zout.putNextEntry(ZipEntry(name))
                            zout.write(data)
                            zout.closeEntry()
                        }
                        entry = zin.nextEntry
                    }
                }
            }
            if (removed > 0) {
                jar.delete()
                temp.renameTo(jar)
                println("Stripped $removed bundled core artifact(s) from ${jar.name}")
            } else {
                temp.delete()
            }
        }

        // Only plugin jars (with plugin.yml) go in the plugins folder; library addons are shaded into consumers.
        fun copyAddonJar(jar: File) {
            if (!jarHasPluginYml(jar)) {
                println("Not copying ${jar.name} to plugins (library jar, no plugin.yml).")
                return
            }
            // Copy the build's jar as-is: each addon's own shadowJar names it "<PluginName>-<version>-UNOFFICIAL.jar".
            // Stale jars are cleared each run, so there is exactly one jar per addon (no "Ambiguous plugin name").
            println("Copying ${jar.name} to plugins folder...")
            val dest = File(pluginsDir, jar.name)
            // A stale jar may be locked by an orphaned server JVM from a previous run; copyTo(overwrite)
            // would then throw FileAlreadyExistsException and fail the whole build. Try a plain delete +
            // copy, and if the lock persists fall back to streaming over the existing file rather than
            // aborting - the addon is still updated and the next run starts clean.
            try {
                if (dest.exists() && !dest.delete()) {
                    dest.outputStream().use { out -> jar.inputStream().use { it.copyTo(out) } }
                } else {
                    jar.copyTo(dest, overwrite = true)
                }
            } catch (e: Exception) {
                println("WARNING: could not refresh ${dest.name} (locked by a stale server?): ${e.message}. Using existing copy.")
                if (!dest.exists()) throw e
                return
            }
            stripBundledCoreClasses(dest)
        }

        // bStats refuses to run unless org.bstats is relocated; the committed builds omit it, so inject it.
        fun patchBstatsRelocation(repoDir: File) {
            for (name in listOf("build.gradle.kts", "build.gradle")) {
                val buildFile = File(repoDir, name)
                if (!buildFile.exists()) continue
                var text = buildFile.readText()
                if (!text.contains("SlimefunMetrics")) continue
                if (text.contains("\"org.bstats\"") || text.contains("'org.bstats'")) continue
                val target = repoDir.name.lowercase().replace(Regex("[^a-z0-9]"), "") + ".libs.bstats"
                val anchor = when {
                    text.contains("shadowJar {") -> "shadowJar {"
                    text.contains("shadowJar{") -> "shadowJar{"
                    else -> null
                }
                if (anchor != null) {
                    text = text.replaceFirst(anchor, "$anchor\n        relocate(\"org.bstats\", \"$target\")")
                    buildFile.writeText(text)
                    println("Patched bStats relocation in $name for ${repoDir.name}")
                }
            }
        }

        // An addon relocating InfinityLib must also bundle it; promote compileOnly -> implementation.
        fun patchInfinityLibShading(repoDir: File) {
            for (name in listOf("build.gradle.kts", "build.gradle")) {
                val buildFile = File(repoDir, name)
                if (!buildFile.exists()) continue
                val text = buildFile.readText()
                if (!text.contains("infinitylib")) continue
                val regex = Regex("""compileOnly\((files\(["'][^"']*InfinityLib[^"']*\.jar["']\))\)""")
                if (regex.containsMatchIn(text)) {
                    val patched = regex.replace(text) { "implementation(${it.groupValues[1]})" }
                    if (patched != text) {
                        buildFile.writeText(patched)
                        println("Patched InfinityLib to be shaded (implementation) in $name for ${repoDir.name}")
                    }
                }
            }
        }

        // Some addons import the old slimefun4.* package; our core is slimefun5, so rewrite it in their sources.
        fun patchSlimefun4Refs(repoDir: File) {
            val srcDir = File(repoDir, "src")
            if (!srcDir.isDirectory) return
            var count = 0
            srcDir.walkTopDown().filter { it.isFile && it.name.endsWith(".java") }.forEach { javaFile ->
                val text = javaFile.readText()
                if (text.contains("io.github.thebusybiscuit.slimefun4")) {
                    javaFile.writeText(text.replace("io.github.thebusybiscuit.slimefun4", "io.github.thebusybiscuit.slimefun5"))
                    count++
                }
            }
            if (count > 0) println("Rewrote slimefun4 -> slimefun5 in $count source file(s) for ${repoDir.name}")
        }

        // Derive each addon's version from its own git tags (latest version-like tag + "-UNOFFICIAL"),
        // mirroring the core standard. Addons hardcode placeholder versions ("1.0.0"), which made the
        // in-game installer show meaningless versions; this rewrites them at build time. No-op without tags.
        fun patchAddonVersion(repoDir: File): String? {
            val rawTag = try {
                val proc = ProcessBuilder("git", "-c", "safe.directory=*", "tag", "--sort=-v:refname")
                    .directory(repoDir).redirectErrorStream(true).start()
                proc.waitFor()
                proc.inputStream.bufferedReader().readText().trim().lines()
                    .firstOrNull { line -> line.isNotBlank() && line.any { it.isDigit() } }
            } catch (e: Exception) { null } ?: return null

            var derivedVersion = rawTag.removePrefix("gh-").removePrefix("v").trim()
            if (derivedVersion.isBlank()) return null
            if (!derivedVersion.endsWith("-UNOFFICIAL")) derivedVersion = "$derivedVersion-UNOFFICIAL"

            for (name in listOf("build.gradle.kts", "build.gradle")) {
                val buildFile = File(repoDir, name)
                if (!buildFile.exists()) continue
                val original = buildFile.readText()
                var patched = Regex("""(?m)^version\s*=\s*"[^"]*"""").replace(original) { "version = \"$derivedVersion\"" }
                // Keep the hardcoded jar filename's version segment in sync (cosmetic, but avoids a mismatch).
                // Leave interpolated names ("Name-$displayVersion.jar") alone: pinning them to a literal
                // could diverge the jar filename from the addon's own plugin.yml version.
                patched = Regex("""archiveFileName\.set\("([A-Za-z0-9_]+)-[^"]*\.jar"\)""")
                    .replace(patched) { m ->
                        if (m.value.contains("$")) m.value
                        else "archiveFileName.set(\"${m.groupValues[1]}-$derivedVersion.jar\")"
                    }
                if (patched != original) {
                    buildFile.writeText(patched)
                    println("Set ${repoDir.name} version -> $derivedVersion (from tag $rawTag)")
                }
            }

            // processResources does not declare the version as an input, so a plugin.yml expanded in a
            // previous run (with an older version) stays up-to-date-cached in build/ - which git reset
            // --hard doesn't touch - and ships a stale version. The jar filename then updates but the
            // embedded plugin.yml doesn't, so the installer reads a false "update available". Delete the
            // cached output so this build re-expands plugin.yml from the current version.
            val stalePluginYml = File(repoDir, "build/resources/main/plugin.yml")
            if (stalePluginYml.exists() && stalePluginYml.delete()) {
                println("Cleared stale plugin.yml for ${repoDir.name} (forces version re-expansion)")
            }
            File(repoDir, "build/tmp/processResources").deleteRecursively()
            return derivedVersion
        }

        // Clear stale addon jars (mismatched names cause Bukkit "Ambiguous plugin name"); keep the core jar.
        // Optional: -PkeepPlugins (or =true/on) keeps whatever is already in the plugins folder AND skips
        // building addons entirely - you're deliberately reusing the existing jars, nothing to rebuild.
        // Read the VALUE, not just presence: Gradle's hasProperty() is true even for -PkeepPlugins=false,
        // which used to silently keep the jars when the user meant to turn it off.
        val keepPluginsValue = (project.findProperty("keepPlugins") as String?)?.trim()?.lowercase()
        val keepPlugins = keepPluginsValue != null && keepPluginsValue !in listOf("false", "off", "no", "0")
        if (keepPlugins) {
            println("[keepPlugins] keeping existing plugin jars; skipping addon clone/build/copy")
            return@doLast
        }
        pluginsDir.listFiles { f: File -> f.name.endsWith(".jar") && !f.name.contains("_RunServer_") }?.forEach {
            // A silent delete() failure here is why "old jars won't go away": the file is still locked by
            // an orphaned server JVM from a previous run. Surface it so it's actionable (stop the server).
            if (!it.delete() && it.exists()) {
                println("[plugins] WARNING: could not delete stale jar ${it.name} - is a previous server still running? Stop it and re-run.")
            }
        }

        for (addon in addons) {
            // Each entry is Owner/Repo or Owner/Repo@branch (run.ps1 appends the chosen branch).
            val ownerRepo = addon.substringBefore("@").trim()
            val branch = addon.substringAfter("@", "").trim()
            val parts = ownerRepo.split("/")
            if (parts.size != 2) {
                println("Invalid addon format: $addon. Expected Owner/Repo or Owner/Repo@branch")
                continue
            }
            val repo = parts[1]
            val label = if (branch.isNotBlank()) "$ownerRepo ($branch)" else ownerRepo

            val repoDir = File(addonsSrcDir, repo)
            val isWindows = org.gradle.internal.os.OperatingSystem.current().isWindows

            val oldHash = if (repoDir.exists()) getGitHash(repoDir) else ""

            if (localAddons && repoDir.exists()) {
                println("[localAddons] using working copy of $label as-is")
            } else if (repoDir.exists()) {
                println("Pulling latest for $label...")
                runProcess(ProcessBuilder("git", "-c", "safe.directory=*", "fetch", "--all").directory(repoDir), 2)
                runProcess(ProcessBuilder("git", "-c", "safe.directory=*", "remote", "set-head", "origin", "-a").directory(repoDir), 1)
                // Fork policy: all addon work (balance.yml, en/items.yml, ports) lives on `experimental`.
                // When run.ps1 doesn't pin a branch, default to experimental - NOT origin/HEAD, which is
                // `stable` and predates our commits (a stale clone stuck on stable is why pushed balance/lore
                // never reached the built jars). Fall back to origin/HEAD only for repos with no experimental.
                val effectiveBranch = when {
                    branch.isNotBlank() -> branch
                    runProcess(ProcessBuilder("git", "-c", "safe.directory=*", "rev-parse", "--verify", "--quiet", "origin/experimental").directory(repoDir), 1) == 0 -> "experimental"
                    else -> ""
                }
                val ref = if (effectiveBranch.isNotBlank()) "origin/$effectiveBranch" else "origin/HEAD"
                if (effectiveBranch.isNotBlank()) {
                    runProcess(ProcessBuilder("git", "-c", "safe.directory=*", "checkout", "-B", effectiveBranch, ref).directory(repoDir), 1)
                }
                // addons-src is a throwaway clone; always force it to match origin exactly.
                runProcess(ProcessBuilder("git", "-c", "safe.directory=*", "reset", "--hard", ref).directory(repoDir), 1)
            } else {
                println("Cloning $label...")
                val cloneBranch = if (branch.isNotBlank()) branch else "experimental"
                // Prefer the experimental branch; fall back to the repo default if it has no such branch.
                val cloned = runProcess(ProcessBuilder("git", "-c", "safe.directory=*", "clone", "-b", cloneBranch, "https://github.com/$ownerRepo.git").directory(addonsSrcDir), 5)
                if (cloned != 0) {
                    runProcess(ProcessBuilder("git", "-c", "safe.directory=*", "clone", "https://github.com/$ownerRepo.git").directory(addonsSrcDir), 5)
                }
            }

            patchCoreJarReference(repoDir)
            patchBstatsRelocation(repoDir)
            patchInfinityLibShading(repoDir)
            patchSlimefun4Refs(repoDir)
            val resolvedVersion = patchAddonVersion(repoDir)

            val newHash = getGitHash(repoDir)
            val libsDir = File(repoDir, "build/libs")
            val jars = libsDir.listFiles { file: File -> file.name.endsWith(".jar") && !file.name.endsWith("-javadoc.jar") && !file.name.endsWith("-sources.jar") }
            val hasCompiledJar = jars != null && jars.isNotEmpty()

            val existingJar = if (hasCompiledJar) {
                jars!!.firstOrNull { it.name.contains("v") || it.name.contains("shadow") } ?: jars!![0]
            } else null
            // Skip only when unchanged AND the cached jar is already Java-8 bytecode (<= 52).
            val existingJarVersion = existingJar?.let { pluginMainClassVersion(it) }
            val recipeMarker = File(libsDir, ".addon-recipe")
            val recipeMatches = recipeMarker.exists() && recipeMarker.readText().trim() == addonBuildRecipe
            // A new tag can appear without any new commit reachable from the built branch (git hash
            // unchanged), so the git-hash check alone would keep reusing a cached jar whose embedded
            // version has fallen behind the latest tag. Only skip when the cached jar's plugin.yml version
            // already equals the version we just resolved; otherwise rebuild so the version updates.
            val versionUpToDate = resolvedVersion == null || (existingJar != null && jarPluginYmlVersion(existingJar) == resolvedVersion)
            if (!localAddons && oldHash == newHash && oldHash.isNotBlank() && existingJar != null && existingJarVersion != null && existingJarVersion <= 52 && recipeMatches && versionUpToDate) {
                println("No updates found for $addon. Skipping build.")
                copyAddonJar(existingJar)
                continue
            }

            // Clear stale jars so we copy this build's output, not an outdated artifact.
            libsDir.listFiles { file: File -> file.name.endsWith(".jar") }?.forEach { it.delete() }

            println("Building $addon...")
            // Use the wrapper's absolute path; "gradlew.bat" alone relies on cmd resolving the cwd, which
            // intermittently fails with "'gradlew.bat' is not recognized" even when the file is present.
            val wrapperName = if (isWindows) "gradlew.bat" else "gradlew"
            val wrapperFile = File(repoDir, wrapperName)
            val gradlewCmd = if (wrapperFile.exists()) wrapperFile.absolutePath else if (isWindows) "gradlew.bat" else "./gradlew"
            // Stamp every addon with the same tag-derived "<version>-UNOFFICIAL" format so the boot log is
            // uniform (the shared snippet + MissileWarfare honor -Partifact_version; others ignore it harmlessly).
            val versionArg = resolvedVersion?.let { "-Partifact_version=$it" }
            val buildPb = if (isWindows) {
                if (versionArg != null) ProcessBuilder("cmd", "/c", gradlewCmd, "shadowJar", versionArg)
                else ProcessBuilder("cmd", "/c", gradlewCmd, "shadowJar")
            } else {
                if (versionArg != null) ProcessBuilder("sh", "-c", "\"$gradlewCmd\" shadowJar $versionArg")
                else ProcessBuilder("sh", "-c", "\"$gradlewCmd\" shadowJar")
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
                copyAddonJar(targetJar)
                recipeMarker.writeText(addonBuildRecipe)
            } else {
                println("WARNING: No compiled jar found for $addon")
            }
        }
    }
}

// runServer reads -PmcVersion / -Paddons; the interactive picker is run.ps1 (the daemon has no console).
val runServerMcVer = (project.findProperty("mcVersion") as String?)?.takeIf { it.isNotBlank() } ?: "26.1.2"

// Addons to build for runServer: -Paddons (comma-separated Owner/Repo) selects them; otherwise none
// (core only). -PskipAddons is still accepted as an explicit "no addons".
val runServerAddons = if (project.hasProperty("skipAddons")) "" else (project.findProperty("addons") as String? ?: "")
project.extra.set("resolvedAddons", runServerAddons)

// The server JVM must match the MC version's Java requirement (the plugin jar itself is Java 8).
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

// ViaVersion + ViaBackwards + ViaRewind let the test server accept clients of other versions.
// On by default for runServer (the scripts rely on it); disable with -PnoVia.
val installVia = !project.hasProperty("noVia")

// Deletes every jar for a given Via plugin slug from the plugins dir (so we never leave a stale or
// incompatible copy behind, and a dependent never loads without its dependency).
fun removeViaJars(pluginsDir: java.io.File, slug: String) {
    pluginsDir.listFiles()?.filter { it.name.startsWith(slug, ignoreCase = true) && it.name.endsWith(".jar") }?.forEach { it.delete() }
}

// Each Via plugin's Jenkins job that publishes the latest release "downgraded" to Java 8 bytecode.
// These run on EVERY server (Java 8 -> 25) and still support all modern Minecraft client versions, so
// they work on the legacy servers (MC <= 1.16.4, Java 8) this universal jar targets - unlike the Modrinth
// releases, which are Java 17+ and fail with UnsupportedClassVersionError on a Java-8 server.
val viaJava8Jobs = mapOf(
    "viaversion" to "ViaVersion-Java8",
    "viabackwards" to "ViaBackwards-Java8",
    "viarewind" to "ViaRewind-Java8"
)

// Downloads the latest Java-8 build of <slug> from the ViaVersion Jenkins CI into pluginsDir, replacing
// any older/stale copy. Returns true iff a jar is now installed. Best-effort: failures never block launch.
fun installViaPlugin(slug: String, pluginsDir: java.io.File): Boolean {
    val job = viaJava8Jobs[slug] ?: return false
    try {
        val base = "https://ci.viaversion.com/job/$job/lastSuccessfulBuild"
        val conn = URI.create("$base/api/json").toURL().openConnection() as HttpURLConnection
        conn.setRequestProperty("User-Agent", "Slimefun5-universal-build")
        conn.connectTimeout = 15000
        conn.readTimeout = 15000
        val body = conn.inputStream.bufferedReader().use { it.readText() }
        @Suppress("UNCHECKED_CAST")
        val json = groovy.json.JsonSlurper().parseText(body) as Map<String, Any?>
        @Suppress("UNCHECKED_CAST")
        val artifacts = json["artifacts"] as? List<Map<String, Any?>> ?: emptyList()
        val artifact = artifacts.firstOrNull { (it["fileName"] as? String)?.endsWith(".jar") == true }
        if (artifact == null) {
            logger.warn("[via] no jar artifact published by $job - skipping")
            removeViaJars(pluginsDir, slug)
            return false
        }
        val name = artifact["fileName"] as String
        val relPath = artifact["relativePath"] as String
        val dest = pluginsDir.resolve(name)
        if (dest.exists()) {
            logger.lifecycle("[via] $name already present")
            return true
        }
        val tmp = File.createTempFile(slug, ".jar")
        URI.create("$base/artifact/$relPath").toURL().openStream().use { input -> tmp.outputStream().use { input.copyTo(it) } }
        // Replace any older/stale copy (incl. an incompatible Modrinth jar from a previous run).
        removeViaJars(pluginsDir, slug)
        tmp.copyTo(dest, overwrite = true)
        tmp.delete()
        logger.lifecycle("[via] installed $name (Java 8 build)")
        return true
    } catch (e: Exception) {
        logger.warn("[via] failed to install $slug: ${e.message}")
        return false
    }
}

tasks.runServer {
    dependsOn(tasks.shadowJar)
    if (runServerAddons.isNotBlank()) {
        dependsOn(cloneAndBuildAddons)
    }
    minecraftVersion(runServerMcVer)
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(requiredJavaFor(runServerMcVer)))
    })

    // -PdumpItems: dump plugins/Slimefun/untranslated-items.yml (+ menus-baseline.yml) on boot, for
    // auditing translation coverage across every loaded addon. Passed as a JVM system property so it
    // works even though the plugins folder (and its config.yml) is recreated each run.
    if (project.hasProperty("dumpItems")) {
        jvmArgs("-Dslimefun.dumpMenuBaseline=true")
    }

    // Per-version run dir: Paper world/config aren't backward-compatible across MC versions.
    val perVersionRunDir = layout.projectDirectory.dir("run/$runServerMcVer")
    runDirectory.set(perVersionRunDir)
    doFirst {
        val runDirFile = perVersionRunDir.asFile
        runDirFile.mkdirs()
        runDirFile.resolve("eula.txt").writeText("eula=true\n")

        // Always pin the server to port 25566. Written before boot so Paper keeps it; other
        // properties are preserved (only the server-port line is set/replaced).
        val serverProps = runDirFile.resolve("server.properties")
        val pinnedPort = "server-port=25566"
        if (serverProps.exists()) {
            val lines = serverProps.readLines()
            val newLines = if (lines.any { it.startsWith("server-port=") }) {
                lines.map { if (it.startsWith("server-port=")) pinnedPort else it }
            } else {
                lines + pinnedPort
            }
            serverProps.writeText(newLines.joinToString("\n") + "\n")
        } else {
            serverProps.writeText("$pinnedPort\n")
        }

        if (installVia) {
            val pluginsDir = runDirFile.resolve("plugins").also { it.mkdirs() }
            // Dependency chain: ViaBackwards needs ViaVersion, ViaRewind needs ViaBackwards. Installing a
            // dependent without its dependency causes UnknownDependencyException at load, so only install
            // each once the one it depends on succeeded.
            val viaOk = installViaPlugin("viaversion", pluginsDir)
            val backwardsOk = viaOk && installViaPlugin("viabackwards", pluginsDir)
            val rewindOk = backwardsOk && installViaPlugin("viarewind", pluginsDir)
            // Remove any jar we did not (successfully) install, clearing orphans from earlier runs.
            if (!viaOk) removeViaJars(pluginsDir, "viaversion")
            if (!backwardsOk) removeViaJars(pluginsDir, "viabackwards")
            if (!rewindOk) removeViaJars(pluginsDir, "viarewind")
        }
    }
}


