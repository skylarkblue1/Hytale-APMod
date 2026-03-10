/**
 * NOTE: This is entirely optional and basics can be done in `settings.gradle.kts`
 */

plugins {
    id("com.gradleup.shadow") version "9.3.2"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.archipelagomw:Java-Client:0.2.1")
}

tasks.shadowJar {
}