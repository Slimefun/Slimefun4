pluginManagement {
    val shadowVersion: String by settings
    val githubGradleVersion: String by settings
    val runPaperVersion: String by settings
    plugins {
        id("com.gradleup.shadow") version shadowVersion
        id("io.github.intisy.github-gradle") version githubGradleVersion
        id("xyz.jpenilla.run-paper") version runPaperVersion
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "Slimefun"

include("core")
include("compat-api")
include("compat-stubs")
include("nms:v1_8_R3")
include("nms:v1_21_R1")
