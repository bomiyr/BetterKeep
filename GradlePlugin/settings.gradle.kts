pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencyResolutionManagement {
        versionCatalogs {
            create("libs") {
                from(files("../gradle/libs.versions.toml"))
            }
        }

        repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
        repositories {
            google()
            mavenCentral()
            gradlePluginPortal()
        }
    }
}

rootProject.name = "GradlePlugin"