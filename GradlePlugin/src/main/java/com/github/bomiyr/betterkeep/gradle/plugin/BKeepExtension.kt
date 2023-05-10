package com.github.bomiyr.betterkeep.gradle.plugin

object BKeepExtension {
    const val ARG_DEFAULT_FLAGS = "DefaultFlags"

    const val EMPTY = 0
    const val SHRINK = 1
    const val OBFUSCATE = 1 shl 1
    const val OPTIMIZE = 1 shl 2
    const val INCLUDE_CODE = 1 shl 3
    const val INCLUDE_DESCRIPTOR_CLASSES = 1 shl 4
}