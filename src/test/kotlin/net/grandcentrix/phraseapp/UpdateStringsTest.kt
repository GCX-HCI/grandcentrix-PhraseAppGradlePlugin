package net.grandcentrix.phraseapp

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

class UpdateStringsTest {

    @Test
    fun `UpdateStrings task should copy from $buildDir to destinationDir`(@TempDir tmpDir: Path) {
        val buildScript = tmpDir.resolve("build.gradle")
        val buildScriptContent = """
                plugins {
                    id("net.grandcentrix.phraseapp")
                }

                phraseApp {
                    destinationDir.set(file("well"))
                }

                tasks.register("testUpdateStrings", net.grandcentrix.phraseapp.UpdateStrings.class, phraseApp)
            """.trimIndent()
        Files.write(buildScript, buildScriptContent.toByteArray())
        with(tmpDir.resolve("build/phraseAppStrings/test.txt")) {
            Files.createDirectories(parent)
            Files.write(this, buildScriptContent.toByteArray())
        }

        val result = GradleRunner.create()
            .withArguments("testUpdateStrings")
            .withPluginClasspath()
            .withProjectDir(tmpDir.toFile())
            .build()

        println(result.output)
        assert(Files.exists(tmpDir.resolve("well/test.txt")))
    }
}