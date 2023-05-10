package com.github.bomiyr.betterkeep.rulesgenerator

import java.lang.StringBuilder

enum class KeepType {
    Keep, KeepClassMembers
}

data class KeepRule(
    val type: KeepType,
    val modifiers: Set<KeepModifiers>,
    val classDef: ClassDef
) {
    fun build(): String {
        return StringBuilder("-")
            .append(
                when (type) {
                    KeepType.Keep -> "keep"
                    KeepType.KeepClassMembers -> "keepclassmembers"
                }
            )
            .append(modifiersToString(modifiers))
            .append(" ")
            .append(classDef.toString())
            .toString()
    }
}