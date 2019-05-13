package net.grandcentrix.phraseapp

import org.gradle.api.GradleException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

class DownloadPhraseAppStringsTaskTest {

    private val task =
        ProjectBuilder.builder().build().tasks.create("download", DownloadPhraseAppStringsTask::class.java)

    @Test
    fun `throws without userToken`() {
        val exception = assertThrows<GradleException> {
            task.downloadStrings()
        }

        assert(exception.message == "Missing userToken. Needs to be set...")
    }

    @Test
    fun `throws without projectId`() {
        task.userToken.set("token")

        val exception = assertThrows<GradleException> {
            task.downloadStrings()
        }

        assert(exception.message == "Missing projectId. Needs to be set...")
    }

    @Test
    fun `throws without resDir`() {
        task.userToken.set("token")
        task.projectId.set("projectId")

        val exception = assertThrows<GradleException> {
            task.downloadStrings()
        }

        assert(exception.message == "Missing resDir. Needs to be set...")
    }

    @Test
    fun `throws without languageId`() {
        task.userToken.set("token")
        task.projectId.set("projectId")
        task.resDir.set("resDir")

        val exception = assertThrows<GradleException> {
            task.downloadStrings()
        }

        assert(exception.message == "Missing languageId. Needs to be set...")
    }

    @Test
    fun `everything set up correctly create stringsFile`() {
        task.userToken.set("token")
        task.projectId.set("projectId")
        task.resDir.set("resDir")
        task.languageId.set("language")

        task.downloadStrings()

        assert(File("${task.project.buildDir}/phraseAppStrings/resDir").exists())
    }
}