package com.github.bomiyr.betterkeep.rulesgenerator

class TypeSpec internal constructor(var value: String)

class Type {
    companion object {
        /**
         * Matches any primitive type (byte, short, int, long, boolean, float, double, char)
         */
        const val ANY_PRIMITIVE_TYPE = "%"

        /**
         * Matches any single character in a class name.
         *
         * Note that the ?, *, and ** wildcards will never match primitive types.
         * Furthermore, only the *** wildcards will match array types of any dimension.
         */
        const val SINGLE_CHAR = "?"

        /**
         * Matches any part of a class name not containing the package separator.
         *
         * Note that the ?, *, and ** wildcards will never match primitive types.
         * Furthermore, only the *** wildcards will match array types of any dimension.
         */
        const val NAME_PART = "*"

        /**
         * Matches any part of a class name, possibly containing any number of package separators.
         *
         * Note that the ?, *, and ** wildcards will never match primitive types.
         * Furthermore, only the *** wildcards will match array types of any dimension.
         */
        const val PACKAGE_PART = "**"

        /**
         * Matches any type (primitive or non-primitive, array or non-array).
         */
        const val TYPE_ANY = "***"

        fun anyPrimitive() = TypeSpec(ANY_PRIMITIVE_TYPE)
        fun pattern(typeName: String) = TypeSpec(typeName)
        fun any() = TypeSpec(TYPE_ANY)
        fun builder() = Builder()
    }

    class Builder {
        private val stringBuilder = StringBuilder()
        private var patternCount = 0

        fun append(string: String): Builder {

            if (string.contains(TYPE_ANY) ||
                string.contains(PACKAGE_PART) ||
                string.contains(NAME_PART) ||
                string.contains(SINGLE_CHAR) ||
                string.contains(ANY_PRIMITIVE_TYPE)
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

        fun appendPackagePart(): Builder {
            appendPackagePartNumbered()
            return this
        }

        fun appendPackagePartNumbered(): Int {
            stringBuilder.append(PACKAGE_PART)
            return ++patternCount
        }

        fun repeatPatternPart(number: Int): Builder {
            stringBuilder.append("<").append(number).append(">")
            return this
        }

        fun build(): TypeSpec {
            return TypeSpec(stringBuilder.toString())
        }
    }
}