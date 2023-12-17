plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    kotlin("kapt")
}

android {
    namespace = "com.demir.starter"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.demir.starter"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    signingConfigs {
        getByName("debug") {
            storeFile = file("main.keystore")
            storePassword = ("android")
            keyAlias =("androiddebugkey")
            keyPassword =("android")
        }

        create("release") {
            storeFile = file("main.keystore")
            storePassword = ("android")
            keyAlias =("androiddebugkey")
            keyPassword =("android")
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.firebase:firebase-firestore:24.10.0")
}