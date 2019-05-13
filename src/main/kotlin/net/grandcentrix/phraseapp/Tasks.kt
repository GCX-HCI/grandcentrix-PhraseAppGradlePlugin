package net.grandcentrix.phraseapp

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

/**
 * This will just "dump" copy the strings from the `$buildDir/phraseAppStrings`
 * into the given **destinationProperty** .
 *
 * If the output is empty it will copy nothing of course.
 */
abstract class UpdateStrings @Inject constructor(
    phraseAppExtension: PhraseAppExtension
) : Copy() {

    init {
        group = "help"
        description = "Updates the strings with the latest version from PhraseApp"
        from("${project.buildDir}/phraseAppStrings")
        into(phraseAppExtension.destinationDir)

        doLast {
            println("Strings updated...")
        }
    }
}

/**
 * A [org.gradle.api.Task] which will update all strings which are stored inside the [PhraseAppExtension.languages].
 *
 * > Note:  This is a heavy tasks! It will download all strings from PhraseApp. Without checking
 *          if the strings are up to date or not. Run it carefully!
 */
abstract class DownloadAllString @Inject constructor(
    val phraseAppExtension: PhraseAppExtension
) : DefaultTask() {

    init {
        phraseAppExtension.languages.get().forEach { (langId, resourceDir) ->
            // Create the tasks
            val downloadStrings = project.tasks.register(
                "downloadStringsForResources${resourceDir.capitalize()}",
                DownloadPhraseAppStringsTask::class.java
            ).apply {
                configure {
                    it.projectId.set(phraseAppExtension.projectId)
                    it.userToken.set(phraseAppExtension.userToken)
                    it.tags.set(phraseAppExtension.tags)
                    it.languageId.set(langId)
                    it.resDir.set(resourceDir)
                }
            }

            // Let *this* task depending on the downloadStringsForResources[XYZ]
            dependsOn(downloadStrings)
        }
    }

    @TaskAction
    fun printDownloadSuccessfully() {
        println("Downloaded strings for ${phraseAppExtension.languages.get().map { it.resBucket }} successfully")
    }
}

/**
 * A [org.gradle.api.Task] which downloads strings from [PhraseApp](phraseapp.com) from their API.
 *
 * [languageId] the PhraseApp bucket id for that language
 * [resDir] the folder in `app/src/main/res` where the `strings.xml` should be stored
 *
 */
abstract class DownloadPhraseAppStringsTask : DefaultTask() {

    val userToken: Property<String> = project.objects.property(String::class.java)

    val projectId: Property<String> = project.objects.property(String::class.java)

    val languageId: Property<String> = project.objects.property(String::class.java)

    val resDir: Property<String> = project.objects.property(String::class.java)

    val tags: ListProperty<String> = project.objects.listProperty(String::class.java)

    @TaskAction
    fun downloadStrings() {
        if (userToken.getOrElse("").isEmpty()) throw GradleException("Missing userToken. Needs to be set...")
        if (projectId.getOrElse("").isEmpty()) throw GradleException("Missing projectId. Needs to be set...")
        if (resDir.getOrElse("").isEmpty()) throw GradleException("Missing resDir. Needs to be set...")
        if (languageId.getOrElse("").isEmpty()) throw GradleException("Missing languageId. Needs to be set...")

        val tagsList = tags.getOrNull() ?: emptyList()
        val tagsAsString = tagsList.toString().substring(1, tagsList.toString().length - 1)
        val api =
            "https://api.phraseapp.com/api/v2/projects/${projectId.get()}/locales/${languageId.get()}/download?file_format=xml&format_options\\[convert_placeholder\\]=true&tags=$tagsAsString"
        val stringsFile = project.file("${project.buildDir}/phraseAppStrings/${resDir.get()}/strings.xml").apply {
            parentFile.mkdirs()
        }

        logger.debug(
            """
                --------------------
                projectId: ${projectId.get()},
                languageId: ${languageId.get()},
                resDir: ${resDir.get()},
                tags: ${tags.get()},
                api: $api
                --------------------
            """.trimIndent()
        )

        project.exec {
            it.commandLine("curl", api, "-s", "-u", "${userToken.get()}:", "-o", stringsFile.absolutePath)
        }
    }
}
