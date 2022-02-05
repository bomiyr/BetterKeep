package com.github.bomiyr.betterkeep.annotations

@Target(AnnotationTarget.CLASS)
annotation class BetterKeep(
    val keepAllMembers: Boolean = true
)