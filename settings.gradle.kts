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
rootProject.name = "Slimefun"
