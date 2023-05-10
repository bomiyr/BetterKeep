package com.github.bomiyr.betterkeep.gradle.plugin

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import java.io.File

class BetterKeepPlugin : Plugin<Project> {

    companion object {
        private const val LOG_TAG = "BKeep"

        private fun Project.logD(msg: String) {
            logger.debug("$LOG_TAG: $msg")
        }

        private fun Project.logI(msg: String) {
            logger.info("$LOG_TAG: $msg")
        }
    }

    override fun apply(project: Project) {
        project.logD("BetterKeepPlugin#BEGIN")
        addGeneratedProguardFiles(project)
        project.extensions.add(BKeepExtension::class.java, "bkeep", BKeepExtension)
        project.logD("BetterKeepPlugin#END")
    }

    private fun addGeneratedProguardFiles(project: Project) {
        getAndroidComponentExtension(project)
            ?.let { androidComponents ->
                configureAndroidComponentExtension(project, androidComponents)
            }
    }

    private fun getAndroidComponentExtension(
        project: Project
    ): AndroidComponentsExtension<*, *, *>? {
        val extension = project
            .extensions
            .findByName("androidComponents") as? AndroidComponentsExtension<*, *, *>

        if (extension != null) {
            val projName = project.name
            val extClass = extension::class.simpleName
            project.logI("Found extension in project :$projName. Type: $extClass")
        }
        return extension
    }

    private fun configureAndroidComponentExtension(
        project: Project,
        androidComponentsExtension: AndroidComponentsExtension<*, *, *>
    ) {
        val androidLibExtension = (project.extensions.findByName("android") as? LibraryExtension)

        androidComponentsExtension.onVariants { variant ->
            val proguardFile = File(
                project.buildDir,
                "generated/ksp/${variant.name}/resources/betterkeep-config.BetterKeep.pro"
            )
            variant.proguardFiles.add(RegularFile { proguardFile })

            project.logI("Proguard file added: ${proguardFile.canonicalPath}")

            variant.packaging.resources.excludes.add("**.BetterKeep.pro")

            if (androidLibExtension != null) {
                val buildType = variant.buildType
                checkNotNull(buildType)

                androidLibExtension
                    .buildTypes
                    .getByName(buildType)
                    .consumerProguardFiles
                    .add(proguardFile)

                project.logI("Consumer proguard file added: ${proguardFile.canonicalPath}")
            }
        }
    }
}
