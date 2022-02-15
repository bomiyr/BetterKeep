package com.github.bomiyr.betterkeep.gradle.plugin

import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import java.io.File

class BetterKeepPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("BetterKeepPlugin#BEGIN")
        addGeneratedProguardFiles(project)
        println("BetterKeepPlugin#END")
    }

    private fun addGeneratedProguardFiles(project: Project) {

        // TODO: Check if it's working on library module.
        // TODO: Add consumer proguard file in library module
        project.extensions.configure(
            AndroidComponentsExtension::class.java
        ) { appModuleExtension ->
            appModuleExtension.onVariants {
                val proguardFile = File(
                    project.buildDir,
                    "generated/ksp/${it.name}/resources/betterkeep-config.pro"
                )

                it.proguardFiles.add(RegularFile { proguardFile })

                it.packaging.resources.excludes.add("**.BetterKeepPro")
            }
        }
    }
}