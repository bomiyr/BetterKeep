pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "BetterKeep"
include(":app")
include(":annotations")
include(":processor")
include(":rulesgenerator")
include(":GradlePlugin")
