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

val props = rootDir.loadProperties("signing.properties")

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
        kotlinCompilerExtensionVersion = Versions.COMPOSE
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    flavorDimensions.add("version")
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

    implementation("androidx.core:core-ktx:${Versions.CORE_KTX}")
    implementation("androidx.compose.ui:ui:${Versions.COMPOSE}")
    implementation("androidx.compose.material:material:${Versions.COMPOSE}")
    implementation("androidx.compose.ui:ui-tooling:${Versions.COMPOSE}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE_RUNTIME}")
    implementation("androidx.activity:activity-compose:${Versions.ACTIVITY_COMPOSE}")
    implementation("androidx.constraintlayout:constraintlayout-compose:${Versions.CONSTRAINT_COMPOSE}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.COMPOSE_VIEWMODEL}")
    implementation("com.google.android.material:material:${Versions.MATERIAL}")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:30.3.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")

    // Coil
    implementation("io.coil-kt:coil-compose:${Versions.COIL}")

    // Compose Destination
    implementation("io.github.raamcosta.compose-destinations:core:${Versions.COMPOSE_DESTINATION}")
    ksp("io.github.raamcosta.compose-destinations:ksp:${Versions.COMPOSE_DESTINATION}")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:${Versions.RETROFIT}")
    implementation("com.squareup.moshi:moshi:${Versions.MOSHI}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Versions.MOSHI}")
    implementation("com.squareup.moshi:moshi-kotlin:${Versions.MOSHI}")
    implementation("com.squareup.retrofit2:converter-moshi:${Versions.RETROFIT}")
    implementation("com.squareup.moshi:moshi-adapters:${Versions.MOSHI_ADAPTERS}")

    // Okhttp
    implementation("com.squareup.okhttp3:okhttp:${Versions.OKHTTP}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP_LOGGING}")

    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:${Versions.HILT}")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.HILT}")
    implementation("androidx.hilt:hilt-navigation-compose:${Versions.HILT_COMPOSE}")

    // Timber
    implementation("com.jakewharton.timber:timber:${Versions.TIMBER}")

    // Chucker
    debugImplementation("com.github.chuckerteam.chucker:library:${Versions.CHUCKER}")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:${Versions.CHUCKER}")

    // Accompanist
    implementation("com.google.accompanist:accompanist-swiperefresh:${Versions.ACCOMPANIST}")
    implementation("com.google.accompanist:accompanist-pager:${Versions.ACCOMPANIST}")

    // Room
    implementation("androidx.room:room-runtime:${Versions.ROOM}")
    kapt("androidx.room:room-compiler:${Versions.ROOM}")
    implementation("androidx.room:room-ktx:${Versions.ROOM}")

    // Lottie
    implementation("com.airbnb.android:lottie-compose:${Versions.LOTTIE}")

    // Mockk
    testImplementation("io.mockk:mockk:${Versions.MOCKK}")
    testImplementation("io.mockk:mockk-agent-jvm:${Versions.MOCKK}")
    androidTestImplementation("io.mockk:mockk-android:${Versions.MOCKK}")
    androidTestImplementation("io.mockk:mockk-agent-jvm:${Versions.MOCKK}")

    testImplementation("junit:junit:4.13.2")
    // For runBlockingTest, CoroutineDispatcher etc.
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINE_TEST}")
    // For InstantTaskExecutorRule
    testImplementation("androidx.arch.core:core-testing:${Versions.CORE_TESTING}")
    // Turbine
    testImplementation("app.cash.turbine:turbine:${Versions.TURBINE}")

    // Google Truth
    testImplementation("com.google.truth:truth:${Versions.TRUTH}")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.COMPOSE}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.COMPOSE}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${Versions.COMPOSE}")

    androidTestImplementation("com.google.dagger:hilt-android-testing:${Versions.HILT_TEST}")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:${Versions.HILT_TEST}")
}
