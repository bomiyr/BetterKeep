package com.github.bomiyr.betterkeep.rulesgenerator

private const val INNER_CLASS_SEPARATOR = "$"
private const val SINGLE_CHAR = "?"
private const val NAME_PART = "*"
private const val PACKAGE_PART = "**"
private const val NEGATOR = '!'

class ClassNamesSpec {
    private val classSpecs = mutableListOf<ClassNameSpec>()

    fun addClassNameSpec(className: ClassNameSpec) {
        classSpecs += className
    }

    fun addClassName(className: String) {
        classSpecs += ClassNameSpec(className)
    }

    override fun toString(): String {
        return if (classSpecs.isEmpty()) {
            ""
        } else {
            classSpecs.joinToString(separator = ",") { it.pattern }
        }
    }
}

class ClassNameSpec(val pattern: String) {

    override fun toString(): String {
        return pattern
    }

    class Builder {

        private val stringBuilder: StringBuilder = StringBuilder()
        private var patternCount = 0

        fun appendString(string: String): Builder {
            stringBuilder.append(string)
            return this
        }

        fun setClassName(className: String): Builder {
            stringBuilder.clear()
            stringBuilder.append(className)
            return this
        }

        fun setInnerClassName(className: String, innerClassName: String): Builder {
            stringBuilder.clear()
            stringBuilder.append(className).append(INNER_CLASS_SEPARATOR).append(innerClassName)
            return this
        }

        fun append(template: ClassNameTemplate): Builder {
            when (template) {
                ClassNameTemplate.AnyChar -> appendAnyChar()
                ClassNameTemplate.NamePart -> appendAnyName()
                ClassNameTemplate.PackagePart -> appendAnyPackage()
            }
            return this
        }

        fun appendAnyChar(): Builder {
            appendAnyCharNumbered()
            return this
        }

        fun appendAnyCharNumbered(): Int {
            stringBuilder.append(SINGLE_CHAR)
            return ++patternCount
        }

        fun appendAnyName(): Builder {
            appendAnyNameNumbered()
            return this
        }

        fun appendAnyNameNumbered(): Int {
            stringBuilder.append(NAME_PART)
            return ++patternCount
        }

        fun appendAnyPackage(): Builder {
            appendAnyPackageNumbered()
            return this
        }

        fun appendAnyPackageNumbered(): Int {
            stringBuilder.append(PACKAGE_PART)
            return ++patternCount
        }

        fun appendPatternRepeat(number: Int): Builder {
            stringBuilder.append("<").append(number).append(">")
            return this
        }

        fun negative(): Builder {
            if (stringBuilder[0] != NEGATOR) {
                stringBuilder.insert(0, "!")
            }
            return this
        }

        fun build(): ClassNameSpec {
            return ClassNameSpec(stringBuilder.toString())
        }

        operator fun plus(string: String): Builder {
            return appendString(string)
        }

        operator fun plus(pattern: ClassNameTemplate): Builder {
            return append(pattern)
        }

        operator fun not(): Builder {
            return negative()
        }

        enum class ClassNameTemplate {
            AnyChar, NamePart, PackagePart
        }
    }
}

