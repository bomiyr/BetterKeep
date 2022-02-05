plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.devtools.ksp") version ("1.5.31-1.0.0")
    id("com.github.bomiyr.betterkeep.gradleplugin")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.github.bomiyr.betterkeep.example"
        minSdk = 16
        targetSdk = 31
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":annotations"))

    ksp(project(":processor"))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}