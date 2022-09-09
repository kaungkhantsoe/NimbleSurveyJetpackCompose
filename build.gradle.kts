buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.13")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    id("com.android.application") version "7.2.1" apply false
    id("com.android.library") version "7.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.5.31" apply false
    id("org.jetbrains.kotlinx.kover") version "0.6.0"
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
    id("com.google.dagger.hilt.android") version "2.42" apply false
    id("org.jetbrains.kotlin.jvm") version "1.7.0" apply false
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
}

// https://detekt.dev/docs/gettingstarted/gradle
detekt {
    autoCorrect = true
    config = files("${rootDir}/config/detekt/detekt.yml")
    source = files("app/src/main/java")

    baseline = file("$rootDir/detekt-baseline.xml")

    // Android: Don't create tasks for the specified build types (e.g. "release")
    ignoredBuildTypes = listOf("release")

    // Android: Don't create tasks for the specified build flavor (e.g. "production")
    ignoredFlavors = listOf("prod")

    // Android: Don't create tasks for the specified build variants (e.g. "productionRelease")
    ignoredVariants = listOf("prodRelease")

    // Specify the base path for file paths in the formatted reports.
    // If not set, all file paths reported will be absolute file path.
    basePath = rootDir.path
}

koverMerged {
    enable()
    filters { // common filters for all default Kover tasks
        classes { // common class filter for all default Kover tasks
            excludes += listOf(
                "com.kks.nimblesurveyjetpackcompose.cache.*",
                "*_Factory",
                "*Kt",
                "*_HiltModules*",
                "com.kks.nimblesurveyjetpackcompose.di.*",
                "com.kks.nimblesurveyjetpackcompose.ui.*",
                "com.kks.nimblesurveyjetpackcompose.model.*",
                "dagger.hilt.internal.aggregatedroot.codegen.*",
                "hilt_aggregated_deps.*"
            ) // class exclusion rules
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
