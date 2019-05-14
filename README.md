[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Download](https://api.bintray.com/packages/grandcentrix/maven/PhraseAppGradlePlugin/images/download.svg) ](https://bintray.com/grandcentrix/maven/PhraseAppGradlePlugin/_latestVersion)
[![Build Status](https://travis-ci.org/grandcentrix/PhraseAppGradlePlugin.svg?branch=master)](https://travis-ci.org/grandcentrix/PhraseAppGradlePlugin)

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
    languages = [new Language("phraseapp-en-id-xxx", "values"), new Language("phraseapp-de-id-xxx", "values-de")]
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
    languages.set(listOf(Language("phraseapp-en-id-xxx", "values"), Language("phraseapp-de-id-xxx", "values-de")))
    tags.set(listOf("android", "common"))
}
```

</details>

Beside of the **tags** all properties are **required**!

You can then update your strings with the `updateStrings` tasks.

> **Note:** This task does **always** a network call an can't be cached! 
            Therefore make sure you use it only if required. 


# License

```
Copyright 2016 grandcentrix GmbH

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
