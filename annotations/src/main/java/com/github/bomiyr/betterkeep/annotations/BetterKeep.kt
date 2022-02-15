package com.github.bomiyr.betterkeep.annotations

@Target(AnnotationTarget.CLASS)
annotation class BetterKeep(
    val keepClass: Boolean = true,
    val keepAllMembers: Boolean = true
)