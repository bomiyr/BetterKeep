plugins {
    id("java-gradle-plugin")
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.kotlin.jvm)
    id("maven-publish")
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

gradlePlugin {
    plugins {
        create("better-keep-plugin") {
            version = "0.1"
            id = "com.github.bomiyr.bkeep"
            implementationClass = "com.github.bomiyr.betterkeep.gradle.plugin.BetterKeepPlugin"
        }
    }
}

//publishing {
//    publications {
//        create<MavenPublication>("maven") {
//            groupId = "com.github.bomiyr.betterkeep"
//            artifactId = "better-keep-plugin"
//            version = "0.1"
//
//            from(components["java"])
//        }
//    }
//}

