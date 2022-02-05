package com.github.bomiyr.betterkeep.rulesgenerator

import java.lang.StringBuilder

private const val SINGLE_CHAR = "?"
private const val NAME_PART = "*"

//enum class


class MemberNameSpec(val value: String) {
    class Builder {
        private val stringBuilder = StringBuilder()
        private var patternCount = 0

        fun append(string: String): Builder {

            if (string.contains(NAME_PART) ||
                string.contains(SINGLE_CHAR)
            ) {
                throw IllegalArgumentException(
                    "Can't append values with pattern matchers here. " +
                            "Use specific methods in Builder or Type.pattern()"
                )
            }

            stringBuilder.append(string)
            return this
        }

        fun appendSingleChar(): Builder {
            appendSingleCharNumbered()
            return this
        }

        fun appendSingleCharNumbered(): Int {
            stringBuilder.append(SINGLE_CHAR)
            return ++patternCount
        }

        fun appendNamePart(): Builder {
            appendNamePartNumbered()
            return this
        }

        fun appendNamePartNumbered(): Int {
            stringBuilder.append(NAME_PART)
            return ++patternCount
        }

        fun repeatPatternPart(number: Int): Builder {
            stringBuilder.append("<").append(number).append(">")
            return this
        }

        fun build(): MemberNameSpec {
            return MemberNameSpec(stringBuilder.toString())
        }
    }
}