plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp") version "1.7.0-1.0.6"
    kotlin("kapt")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("dagger.hilt.android.plugin")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kover")
}

val props = rootDir.loadGradleProperties("signing.properties")

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.kks.nimblesurveyjetpackcompose"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "0.1.0"

        testInstrumentationRunner = "com.kks.nimblesurveyjetpackcompose.di.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0"
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    flavorDimensions("version")
    productFlavors {
        create("prod") {
            dimension = "version"
            applicationId = "com.kks.nimblesurveyjetpackcompose"
            buildConfigField("String", "BASE_URL", "\"https://survey-api.nimblehq.co/\"")
        }

        create("dev") {
            dimension = "version"
            // Staging AppId should be different from the real production.
            applicationId = "com.kks.nimblesurveyjetpackcomposestaging"
            buildConfigField("String", "BASE_URL", "\"https://survey-api.nimblehq.co/\"")
        }
    }


    signingConfigs {
        //keytool -genkey -v -keystore debug.keystore -alias debug-key-alias -keyalg RSA -keysize 2048 -validity 10000
        getByName("debug") {
            storeFile = file("../config/debug.keystore")
            storePassword = "123456"
            keyAlias = "debug-key-alias"
            keyPassword = "123456"
        }
        create("release") {
            try {
                storeFile = file("../config/nimblesurveycompose.keystore")
                storePassword = props.getProperty("KEYSTORE_PASSWORD") as String
                keyAlias = props.getProperty("KEY_ALIAS") as String
                keyPassword = props.getProperty("KEY_PASSWORD") as String
            } catch (ex: Exception) {
                throw InvalidUserDataException("You should define KEYSTORE_PASSWORD and KEY_PASSWORD in gradle.properties.")
            }
        }
    }

    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions.jvmTarget = "1.8"

    lintOptions {
        isAbortOnError = false
    }

    configurations {
        androidTestImplementation {
            exclude(group = "io.mockk", module = "mockk-agent-jvm")
        }
    }

    kapt {
        arguments {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    testOptions {
        animationsDisabled = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.compose.ui:ui:1.2.0")
    implementation("androidx.compose.material:material:1.2.0")
    implementation("androidx.compose.ui:ui-tooling:1.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1")
    implementation("com.google.android.material:material:1.6.1")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:30.3.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")

    // Coil
    implementation("io.coil-kt:coil-compose:2.2.0")

    // Compose Destination
    implementation("io.github.raamcosta.compose-destinations:core:1.6.16-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.6.16-beta")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.moshi:moshi:1.13.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.13.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.13.0")
    implementation("com.squareup.moshi:moshi-adapters:1.12.0")

    // Okhttp
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.42")
    kapt("com.google.dagger:hilt-android-compiler:2.42")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Chucker
    debugImplementation("com.github.chuckerteam.chucker:library:3.5.2")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:3.5.2")

    // Accompanist
    implementation("com.google.accompanist:accompanist-swiperefresh:0.25.1")
    implementation("com.google.accompanist:accompanist-pager:0.25.1")

    // Room
    implementation("androidx.room:room-runtime:2.4.3")
    kapt("androidx.room:room-compiler:2.4.3")
    implementation("androidx.room:room-ktx:2.4.3")

    // Lottie
    implementation("com.airbnb.android:lottie-compose:5.2.0")

    // Mockk
    testImplementation("io.mockk:mockk:1.12.5")
    testImplementation("io.mockk:mockk-agent-jvm:1.12.5")
    androidTestImplementation("io.mockk:mockk-android:1.12.5")
    androidTestImplementation("io.mockk:mockk-agent-jvm:1.12.5")

    testImplementation("junit:junit:4.13.2")
    // For runBlockingTest, CoroutineDispatcher etc.
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    // For InstantTaskExecutorRule
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    // Turbine
    testImplementation("app.cash.turbine:turbine:0.9.0")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.2.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.2.0")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.2.0")

    // For instrumented tests.
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.38.1")
    // ...with Kotlin.
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.38.1")
}
