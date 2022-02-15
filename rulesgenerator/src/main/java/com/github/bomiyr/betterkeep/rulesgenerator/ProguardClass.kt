package com.github.bomiyr.betterkeep.rulesgenerator


class ClassDef {
    var annotation: String? = null

    var modifiers: MutableSet<ClassModifier> = mutableSetOf()

    var type: ClassType = ClassType.Class

    var names: MutableSet<ClassNameSpec> = mutableSetOf()

    fun addName(function: ClassNameSpec.Builder.() -> Unit) {
        names += ClassNameSpec.Builder()
            .apply {
                function(this)
            }
            .build()
    }

    private var parentSpec: ParentSpec? = null

    fun extends(className: ClassNameSpec, annotation: String? = null) {
        parentSpec = ParentSpec().apply {
            this.annotation = annotation
            this.inheritanceType = InheritanceType.Extends
            this.classNameSpec = className
        }
    }

    fun implements(className: ClassNameSpec, annotation: String? = null) {
        parentSpec = ParentSpec().apply {
            this.annotation = annotation
            this.inheritanceType = InheritanceType.Implements
            this.classNameSpec = className
        }
    }

    var members: MutableSet<MemberSpec> = mutableSetOf()

    fun addField(function: FieldSpec.FieldNameSpec.Builder.() -> Unit) {
        members += FieldSpec.FieldNameSpec.Builder()
            .apply {
                function(this)
            }
            .build()
    }

    fun addMethod(function: MethodSpec.MethodNameSpec.Builder.() -> Unit) {
        members += MethodSpec.MethodNameSpec.Builder()
            .apply {
                function(this)
            }
            .build()
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        annotation?.let {
            if (!it.startsWith("@")) {
                stringBuilder.append("@")
            }
            stringBuilder.append(annotation).append(" ")
        }

        if (modifiers.isNotEmpty()) {
            val modifiersValue = modifiers.joinToString(separator = " ") { it.toString() }
            stringBuilder.append(modifiersValue).append(" ")
        }

        stringBuilder.append(type.toString()).append(" ")

        if (names.isNotEmpty()) {
            val classNames = names.joinToString(separator = ",") { it.pattern }
            stringBuilder.append(classNames)
        }

        parentSpec?.let {
            stringBuilder.append(" ").append(it.toString())
        }

        if (members.isNotEmpty()) {
            stringBuilder
                .append(" {").append("\n")

            stringBuilder.append(
                members.joinToString(separator = "\n\n") { "    $it;" }
            )
            stringBuilder.append("\n")
                .append("}")
        }
        return stringBuilder.toString()
    }
}


fun classDef(function: ClassDef.() -> Unit): ClassDef {
    return ClassDef()
        .apply {
            function(this)
        }
}

/**
 * Class and members will not be removed or renamed
 */
fun keep(classDef: ClassDef): String{
    return "-keep $classDef"
}

/**
 * Class and members will not be renamed, but can be removed if unused
 */
fun keepClassMembers(classDef: ClassDef): String{
    return "-keepclassmembers $classDef"
}

fun test() {
    classDef {
        annotation = ""
        modifiers += ClassModifier.Public
        type = ClassType.Enum
        addName {
            this + ClassNameSpec.Builder.ClassNameTemplate.AnyChar
        }

        addField {
            annotation = ""
            modifiers += FieldSpec.FieldModifier.Public
        }
    }
}
