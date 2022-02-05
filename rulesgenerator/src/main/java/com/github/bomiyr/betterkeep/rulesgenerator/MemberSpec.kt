package com.github.bomiyr.betterkeep.rulesgenerator

sealed class MemberSpec {
    abstract val value: String

    override fun toString(): String {
        return value
    }
}

object AnyFieldOrMethod : MemberSpec() {
    override val value: String = "*"
}

class FieldSpec(override val value: String) : MemberSpec() {


    enum class FieldModifier {
        Public {
            override fun toString(): String {
                return "public"
            }
        },
        NotPublic {
            override fun toString(): String {
                return "!public"
            }
        },

        Private {
            override fun toString(): String {
                return "private"
            }
        },
        NotPrivate {
            override fun toString(): String {
                return "!private"
            }
        },

        Protected {
            override fun toString(): String {
                return "protected"
            }
        },
        NotProtected {
            override fun toString(): String {
                return "!protected"
            }
        },

        Static {
            override fun toString(): String {
                return "static"
            }
        },
        NotStatic {
            override fun toString(): String {
                return "!static"
            }
        },

        Volatile {
            override fun toString(): String {
                return "volatile"
            }
        },
        NotVolatile {
            override fun toString(): String {
                return "!volatile"
            }
        },

        Transient {
            override fun toString(): String {
                return "transient"
            }
        },
        NotTransient {
            override fun toString(): String {
                return "!transient"
            }
        },
    }

    class FieldNameSpec(val value: String) {
        class Builder {

            var annotation: String? = null
            var modifiers: MutableList<FieldModifier> = mutableListOf()
            var fieldDefinition: String? = null

            fun any(): Builder {
                fieldDefinition = "<fields>"
                return this
            }

            fun field(type: TypeSpec, memberNameSpec: MemberNameSpec): Builder {
                fieldDefinition = "${type.value} $memberNameSpec"
                return this
            }

            fun build(): FieldSpec {
                if (fieldDefinition == null) {
                    any()
                }

                val stringBuilder = StringBuilder()
                if (annotation != null) {
                    stringBuilder.append("@").append(annotation).append("\n")
                }

                if (modifiers.isNotEmpty()) {
                    stringBuilder.append(modifiers.joinToString(separator = " ") { it.toString() })
                        .append("\n")
                }

                stringBuilder.append(fieldDefinition).append("\n")
                return FieldSpec(stringBuilder.toString())
            }
        }
    }

}

class MethodSpec(override val value: String) : MemberSpec() {

    enum class MethodModifier {
        Public {
            override fun toString(): String {
                return "public"
            }
        },
        NotPublic {
            override fun toString(): String {
                return "!public"
            }
        },

        Private {
            override fun toString(): String {
                return "private"
            }
        },
        NotPrivate {
            override fun toString(): String {
                return "!private"
            }
        },

        Protected {
            override fun toString(): String {
                return "protected"
            }
        },
        NotProtected {
            override fun toString(): String {
                return "!protected"
            }
        },

        Static {
            override fun toString(): String {
                return "static"
            }
        },
        NotStatic {
            override fun toString(): String {
                return "!static"
            }
        },

        Synchronized {
            override fun toString(): String {
                return "synchronized"
            }
        },
        NotSynchronized {
            override fun toString(): String {
                return "!synchronized"
            }
        },

        Native {
            override fun toString(): String {
                return "native"
            }
        },
        NotNative {
            override fun toString(): String {
                return "!native"
            }
        },

        Abstract {
            override fun toString(): String {
                return "abstract"
            }
        },
        NotAbstract {
            override fun toString(): String {
                return "!abstract"
            }
        },

        Strictfp {
            override fun toString(): String {
                return "strictfp"
            }
        },
        NotStrictfp {
            override fun toString(): String {
                return "!strictfp"
            }
        };
    }

    class MethodNameSpec(val value: String) {
        class Builder {

            var annotation: String? = null
            var modifiers: MutableList<MethodModifier> = mutableListOf()
            var methodDefinition: String? = null

            fun any(): Builder {
                methodDefinition = "<methods>"
                return this
            }

            fun anyConstructor(): Builder {
                methodDefinition = "<init>(...)"
                return this
            }

            fun constructor(vararg arguments: TypeSpec): Builder {
                methodDefinition = "<init>(${arguments.joinToString(separator = ",") { it.value }})"
                return this
            }

            fun method(returnType: TypeSpec, name: MemberNameSpec): Builder {
                methodDefinition = "${returnType.value} $name(...)"
                return this
            }

            fun method(
                returnType: TypeSpec,
                name: MemberNameSpec,
                vararg arguments: TypeSpec
            ): Builder {
                methodDefinition =
                    "${returnType.value} $name(${arguments.joinToString(separator = ",") { it.value }})"
                return this
            }

            fun build(): MethodSpec {
                if (methodDefinition == null) {
                    any()
                }

                val stringBuilder = StringBuilder()
                if (annotation != null) {
                    stringBuilder.append("@").append(annotation).append("\n")
                }

                if (modifiers.isNotEmpty()) {
                    stringBuilder.append(modifiers.joinToString(separator = " ") { it.toString() })
                        .append("\n")
                }

                stringBuilder.append(methodDefinition).append("\n")
                return MethodSpec(stringBuilder.toString())
            }
        }
    }


}