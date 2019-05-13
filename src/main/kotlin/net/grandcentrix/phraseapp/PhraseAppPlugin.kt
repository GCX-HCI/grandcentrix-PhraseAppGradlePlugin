package net.grandcentrix.phraseapp

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

/**
 * The PhraseApp plugin can be used to update all your strings
 * inside an Android project.
 *
 * This plugin will create a [`phraseApp extension`][PhraseAppExtension] and
 * the `updateStrings` task.
 */
class PhraseAppPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create("phraseApp", PhraseAppExtension::class.java)
        val downloadAllStrings = project.tasks.register("downloadAllString", DownloadAllString::class.java, extension)
        project.tasks.register("updateStrings", UpdateStrings::class.java, extension).configure {
            it.dependsOn(downloadAllStrings)
        }
    }
}

/**
 * A [Language] is build from an [localeId] (from PhraseApp)
 * and the [resBucket] (to location).
 *
 * You can use it with `o5a8ea9826e28ayb9805d48o1601e8b6` for the `localeId`
 * and `values` or `values-en` for the [resBucket] for instance.
 */
data class Language(val localeId: String, val resBucket: String)

abstract class PhraseAppExtension @Inject constructor(objectFactory: ObjectFactory) {

    /**
     * This list contains the [Language].
     *
     * @see Language
     */
    val languages: ListProperty<Language> = objectFactory.listProperty(Language::class.java)

    /**
     * The projectId from PhraseApp.
     */
    val projectId: Property<String> = objectFactory.property(String::class.java)

    /**
     * The token is required to communicate over the PhraseApp API.
     */
    val userToken: Property<String> = objectFactory.property(String::class.java)

    /**
     * This is the destination where we **copy** the strings into.
     * If you apply this plugin in your **top-level** buildscript it should be something like
     * ```
     * app/src/main/res/
     * ```
     * But if you apply this to your **application** buildscript it should be
     * ```
     * src/main/res/
     * ```
     */
    val destinationDir: DirectoryProperty = objectFactory.directoryProperty()

    /**
     * **Optional** tags you want to download.
     * Could also be empty or "not setup".
     */
    val tags: ListProperty<String> = objectFactory.listProperty(String::class.java)
}