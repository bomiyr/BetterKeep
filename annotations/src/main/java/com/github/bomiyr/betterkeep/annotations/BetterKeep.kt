package com.github.bomiyr.betterkeep.annotations

@Target(AnnotationTarget.CLASS)
annotation class BetterKeep(
    val keepClass: Boolean = true,
    val keepAllMembers: Boolean = true,

    val allowShrinking: Boolean = false,
    val allowObfuscation: Boolean = false,
    val allowOptimization: Boolean = false,
    val includeCode: Boolean = true,
    val includeDescriptorClasses: Boolean = true,
)