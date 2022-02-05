plugins {
    id ("java-gradle-plugin")
    id("kotlin")
    id ("maven-publish")
}

dependencies {
    implementation ("com.android.tools.build:gradle:7.0.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}


gradlePlugin {
    plugins {
        create("better-keep-plugin") {
            version = "0.1"
            id = "com.github.bomiyr.betterkeep.gradleplugin"
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

