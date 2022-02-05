package com.github.bomiyr.betterkeep.rulesgenerator

enum class ClassType {
    Interface {
        override fun toString(): String {
            return "interface"
        }
    },
    Class {
        override fun toString(): String {
            return "class"
        }
    },
    Enum {
        override fun toString(): String {
            return "enum"
        }
    },
    NotInterface {
        override fun toString(): String {
            return "!interface"
        }
    },
    NotEnum {
        override fun toString(): String {
            return "!enum"
        }
    }
}

enum class ClassModifier() {
    Public {
        override fun toString(): String {
            return "public"
        }
    },
    Final {
        override fun toString(): String {
            return "final"
        }
    },
    Abstract {
        override fun toString(): String {
            return "abstract"
        }
    },
    NotPublic {
        override fun toString(): String {
            return "!public"
        }
    },
    NotFinal {
        override fun toString(): String {
            return "!final"
        }
    },
    NotAbstract {
        override fun toString(): String {
            return "!abstract"
        }
    }

}

/**
 * [@annotationtype] [[!]public|final|abstract|@ ...] [!]interface|class|enum classname
 *     [extends|implements [@annotationtype] classname]
 * [{
 *     [@annotationtype]
 *     [[!]public|private|protected|static|volatile|transient ...]
 *     <fields> | (fieldtype fieldname [= values]);
 *
 *     [@annotationtype]
 *     [[!]public|private|protected|static|synchronized|native|abstract|strictfp ...]
 *     <methods> | <init>(argumenttype,...) | classname(argumenttype,...) | (returntype methodname(argumenttype,...) [return values]);
 * }]
 */

class ClassSpec() {
    var annotatedWith: String? = null
    fun annotatedWith(annotation: String) {
        annotatedWith = annotation
    }

    var modifiers: List<ClassModifier> = emptyList()

    var type: ClassType = ClassType.Class

    var classNames: ClassNameSpec? = null

    var parentSpec: ParentSpec? = null
}
