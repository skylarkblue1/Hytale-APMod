import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.mavenCentral
import org.gradle.kotlin.dsl.repositories

rootProject.name = "HytaleArchipelago"

plugins {
    // See documentation on https://scaffoldit.dev
    id("dev.scaffoldit") version "0.2.+"
}

// Would you like to do a split project?
// Create a folder named "common", then configure details with `common { }`

hytale {
    usePatchline("release")
    useVersion("latest")


    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("io.github.archipelagomw:Java-Client:0.2.1")
    }

    manifest {
        Group = "skylarkblue1"
        Name = "HytaleAP"
        Main = "io.github.skylarkblue1.HytaleAP"
    }
}