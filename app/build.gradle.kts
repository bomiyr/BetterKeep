plugins {
    id("com.android.application")
    id("kotlin-android")
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.kotlin.ksp)
    id("com.github.bomiyr.bkeep")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.github.bomiyr.betterkeep.example"
        minSdk = 16
        targetSdk = 33
        versionCode = 1

        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    flavorDimensions += "version"

    productFlavors {
        create("dev") {
            dimension = "version"
        }
        create("prod") {
            dimension = "version"

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    namespace = "com.github.bomiyr.betterkeep.example"
}

dependencies {
    implementation(project(":annotations"))

    ksp(project(":processor"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
