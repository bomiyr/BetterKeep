package com.github.bomiyr.betterkeep.processor

import com.google.devtools.ksp.symbol.KSFile

data class GeneratedFileInfo(
    val dependency: KSFile?,
    val packageName: String,
    val fileName: String,
    val rule: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GeneratedFileInfo

        if (dependency?.packageName != other.dependency?.packageName) return false
        if (dependency?.fileName != other.dependency?.fileName) return false
        if (packageName != other.packageName) return false
        if (fileName != other.fileName) return false
        if (rule != other.rule) return false

        return true
    }

    override fun hashCode(): Int {
        var result = 0
        if (dependency != null) {
            result = dependency.packageName.hashCode()
            result = 31 * result + dependency.fileName.hashCode()
        }

        result = 31 * result + packageName.hashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + rule.hashCode()
        return result
    }
}