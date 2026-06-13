import java.util.concurrent.TimeUnit
import java.io.ByteArrayOutputStream
import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.Files
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
    implementation(project(":compat-api"))

    githubImplementation("Slimefun5:dough:4.0.4:all")

    implementation("io.papermc:paperlib:1.0.8")
    implementation("commons-lang:commons-lang:2.6")
    // XSeries: cross-version Material/Sound/Particle resolution, shaded.
    implementation("com.github.cryptomorin:XSeries:9.10.0")

    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    // Compile against the oldest Bukkit API (1.8.8); newer APIs go through compat-api / NMS.
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    // Compile-only stubs of post-1.8 org.bukkit types; not shaded, real classes used at runtime.
    compileOnly(project(":compat-stubs"))

    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.15.2")
    testImplementation("org.slf4j:slf4j-simple:2.0.16")
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
        // WorldEdit/PlaceholderAPI hooks subclass Java-17 API types, so they can't compile on Java 8.
        exclude(
            "**/integrations/WorldEditIntegration.java",
            "**/integrations/PlaceholderAPIIntegration.java"
        )
    }

    // Tests need MockBukkit (Java 25+), incompatible with the Java 8 toolchain.
    compileTestJava { enabled = false }
    test { enabled = false }

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

        // Addon build files reference the core jar by a relative path valid only in the old layout;
        // rewrite it to the absolute jar path after each checkout (reset --hard reverts it every run).
        val coreJarFile = project.layout.buildDirectory.file("libs/Slimefun v${project.version}-MC26.1.2.jar").get().asFile
        val coreJarPath = coreJarFile.absolutePath.replace("\\", "/")
        if (!coreJarFile.exists()) {
            println("WARNING: Core jar not found at ${coreJarFile.absolutePath} - addon compiles will fail until :core:shadowJar produces it.")
        }
        // Bump to force a one-time rebuild when the patching below changes.
        val addonBuildRecipe = "4"
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

        // Equal-length byte swap of slimefun4 -> slimefun5 in a jar's .class entries (idempotent). Catches
        // shaded deps (e.g. a metrics module) compiled against the upstream slimefun4 package, which the
        // source-level patchSlimefun4Refs can't reach.
        fun relocateSlimefun4InJar(jar: File) {
            val from = "slimefun4".toByteArray(Charsets.UTF_8)
            val to = "slimefun5".toByteArray(Charsets.UTF_8)
            val temp = File(jar.parentFile, jar.name + ".tmp")
            var changed = false
            ZipInputStream(jar.inputStream()).use { zin ->
                ZipOutputStream(temp.outputStream()).use { zout ->
                    var entry = zin.nextEntry
                    while (entry != null) {
                        val data = zin.readBytes()
                        if (entry.name.endsWith(".class")) {
                            var i = 0
                            while (i <= data.size - from.size) {
                                var match = true
                                for (j in from.indices) if (data[i + j] != from[j]) { match = false; break }
                                if (match) { System.arraycopy(to, 0, data, i, to.size); changed = true }
                                i++
                            }
                        }
                        zout.putNextEntry(ZipEntry(entry.name))
                        zout.write(data)
                        zout.closeEntry()
                        entry = zin.nextEntry
                    }
                }
            }
            if (changed) {
                jar.delete()
                temp.renameTo(jar)
                println("Relocated slimefun4 -> slimefun5 in ${jar.name}")
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
            println("Copying ${jar.name} to plugins folder...")
            val dest = File(pluginsDir, jar.name)
            jar.copyTo(dest, overwrite = true)
            relocateSlimefun4InJar(dest)
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

        // Clear stale addon jars (mismatched names cause Bukkit "Ambiguous plugin name"); keep the core jar.
        pluginsDir.listFiles { f: File -> f.name.endsWith(".jar") && !f.name.contains("_RunServer_") }?.forEach { it.delete() }

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
            val upstreamRef = if (branch.isNotBlank()) "origin/$branch" else "origin/HEAD"
            val label = if (branch.isNotBlank()) "$ownerRepo ($branch)" else ownerRepo

            val repoDir = File(addonsSrcDir, repo)
            val isWindows = org.gradle.internal.os.OperatingSystem.current().isWindows

            val oldHash = if (repoDir.exists()) getGitHash(repoDir) else ""

            if (repoDir.exists()) {
                println("Pulling latest for $label...")
                runProcess(ProcessBuilder("git", "-c", "safe.directory=*", "fetch", "--all").directory(repoDir), 2)
                runProcess(ProcessBuilder("git", "-c", "safe.directory=*", "remote", "set-head", "origin", "-a").directory(repoDir), 1)
                if (branch.isNotBlank()) {
                    runProcess(ProcessBuilder("git", "-c", "safe.directory=*", "checkout", branch).directory(repoDir), 1)
                }
                // addons-src is a throwaway clone; always force it to match origin exactly.
                runProcess(ProcessBuilder("git", "-c", "safe.directory=*", "reset", "--hard", upstreamRef).directory(repoDir), 1)
            } else {
                println("Cloning $label...")
                val cloneCmd = mutableListOf("git", "-c", "safe.directory=*", "clone")
                if (branch.isNotBlank()) { cloneCmd.add("-b"); cloneCmd.add(branch) }
                cloneCmd.add("https://github.com/$ownerRepo.git")
                runProcess(ProcessBuilder(cloneCmd).directory(addonsSrcDir), 5)
            }

            // Re-point the (just-checked-out) core jar reference at the real built jar.
            patchCoreJarReference(repoDir)
            // Ensure bStats is relocated so SlimefunMetrics-using addons can enable.
            patchBstatsRelocation(repoDir)
            // Shade relocated InfinityLib + fix stray slimefun4 package references (both fail on every MC version).
            patchInfinityLibShading(repoDir)
            patchSlimefun4Refs(repoDir)

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
            if (oldHash == newHash && oldHash.isNotBlank() && existingJar != null && existingJarVersion != null && existingJarVersion <= 52 && recipeMatches) {
                println("No updates found for $addon. Skipping build.")
                copyAddonJar(existingJar)
                continue
            }

            // Clear stale jars so we copy this build's output, not an outdated artifact.
            libsDir.listFiles { file: File -> file.name.endsWith(".jar") }?.forEach { it.delete() }

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

tasks.runServer {
    dependsOn(tasks.shadowJar)
    if (runServerAddons.isNotBlank()) {
        dependsOn(cloneAndBuildAddons)
    }
    minecraftVersion(runServerMcVer)
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(requiredJavaFor(runServerMcVer)))
    })

    // Per-version run dir: Paper world/config aren't backward-compatible across MC versions.
    val perVersionRunDir = layout.projectDirectory.dir("run/$runServerMcVer")
    runDirectory.set(perVersionRunDir)
    doFirst {
        val runDirFile = perVersionRunDir.asFile
        runDirFile.mkdirs()
        runDirFile.resolve("eula.txt").writeText("eula=true\n")
    }
}


