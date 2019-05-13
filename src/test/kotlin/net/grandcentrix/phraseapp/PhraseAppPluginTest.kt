package net.grandcentrix.phraseapp

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

class PhraseAppPluginTest {

    @Test
    fun `apply plugin execute help will only create one task`(@TempDir tmpDir: Path) {
        val buildScript = tmpDir.resolve("build.gradle")
        val buildScriptContent = """
                plugins {
                    id("net.grandcentrix.phraseapp")
                }
            """.trimIndent()
        Files.write(buildScript, buildScriptContent.toByteArray())

        val result = GradleRunner.create()
            .withArguments("help", "-Dorg.gradle.internal.tasks.stats")
            .withPluginClasspath()
            .withProjectDir(tmpDir.toFile())
            .build()

        println(result.output)
        assert(result.output.contains("Task counts: created 1"))
    }

    @Test
    fun `apply plugin execute updateStrings will create two tasks (and fail)`(@TempDir tmpDir: Path) {
        val buildScript = tmpDir.resolve("build.gradle")
        val buildScriptContent = """
                plugins {
                    id("net.grandcentrix.phraseapp")
                }
            """.trimIndent()
        Files.write(buildScript, buildScriptContent.toByteArray())

        val result = GradleRunner.create()
            .withArguments("updateStrings", "-Dorg.gradle.internal.tasks.stats")
            .withPluginClasspath()
            .withProjectDir(tmpDir.toFile())
            .buildAndFail()

        println(result.output)
        assert(result.output.contains("Task counts: created 2"))
    }
}