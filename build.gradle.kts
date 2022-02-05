// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
        classpath("com.github.bomiyr.betterkeep.gradleplugin:com.github.bomiyr.betterkeep.gradleplugin.gradle.plugin:0.1")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

tasks.register("clean").configure {
    delete("build")
}