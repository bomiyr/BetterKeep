package com.github.bomiyr.betterkeep.rulesgenerator

enum class InheritanceType {
    Extends {
        override fun toString(): String {
            return "extends"
        }
    },
    Implements {
        override fun toString(): String {
            return "implements"
        }
    }
}

class ParentSpec {
    var annotation: String? = null
    var inheritanceType: InheritanceType = InheritanceType.Extends
    var classNameSpec: ClassNameSpec? = null

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(inheritanceType.toString())
            .append(" ")

        annotation?.let {
            if (!it.startsWith("@")) {
                stringBuilder.append("@")
            }
            stringBuilder.append(annotation)
        }

        stringBuilder.append(classNameSpec!!.pattern)
        return stringBuilder.toString()
    }
}