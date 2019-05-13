package net.grandcentrix.phraseapp

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

class DownloadAllStringTest {

    private val project = ProjectBuilder.builder().build()

    private val extension = object : PhraseAppExtension(project.objects) {}

    @Test
    fun `init task should register as much tasks as languages are set`() {
        extension.languages.set(
            listOf(
                Language("locale", "res"),
                Language("locale2", "res2")
            )
        )

        project.tasks.create("download", DownloadAllString::class.java, extension)

        assert(project.tasks.size == 3)
    }

    @Test
    fun `init task should crate dependency between main task and created one`() {
        extension.languages.set(
            listOf(
                Language("locale", "res"),
                Language("locale2", "res2")
            )
        )
        extension.projectId.set("")
        extension.userToken.set("")
        extension.tags.set(listOf(""))

        val downloadAllStringTask = project.tasks.create("download", DownloadAllString::class.java, extension)

        project.tasks.forEach {
            if(it.name.startsWith("downloadStringsForResources")) {
                it.dependsOn.contains(downloadAllStringTask)
            }
        }
    }
}