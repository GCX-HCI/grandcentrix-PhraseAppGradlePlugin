# PhraseAppGradlePlugin
A Gradle plugin for [PhraseApp](phraseapp.com) to receive/download/update strings
in your Android application.

## Usage
You have to apply this plugin into your **top-level** `build.gradle[.kts]` file and 
set up the `phraseApp` extension:
```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'net.grandcentrix:PhraseAppGradlePlugin:1.0.0'
    }
}

appply plugin: 'net.grandcentrix.phraseapp'

phraseApp {
    projectId = "projectId"
    userToken = "userToken"
    destinationDir = file("app/src/main/res/")
    languages = [new Language("id", "values"), new Language("anotherId", "values-en")]
    tags = ["android", "common"]
}
```

<details>
<summary>Kotlin DSL:</summary>
   
```kotlin
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("net.grandcentrix:PhraseAppGradlePlugin:1.0.0")
    }
}
    
appply(plugin = "net.grandcentrix.phraseapp")
    
phraseApp {
    projectId.set("projectId")
    userToken.set("userToken")
    destinationDir.set(file("app/src/main/res/"))
    languages.set(listOf(Language("id", "values"), Language("anotherId", "values-en")))
    tags.set(listOf("android", "common"))
}
```

</details>

Beside of the **tags** all properties are **required**!

You can then update your strings with the `updateStrings` tasks.

> **Note:** This task does **always** a network call an can't be cached! 
            Therefore make sure you use it only if required. 
