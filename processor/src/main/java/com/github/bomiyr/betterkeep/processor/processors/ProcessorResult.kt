package com.github.bomiyr.betterkeep.processor.processors

import com.google.devtools.ksp.symbol.KSFile

data class ClassInfo(
    val flags: Int,
    val dependencyFile: KSFile?,
    val className: String,
    val packageName: String,
    val keepClass: Boolean,
    val keepAllMembers: Boolean = false,
)

data class FunctionInfo(
    val name: String,
    val returnType: String,
    val arguments: Arguments,
)

data class ConstructorInfo(
    val arguments: Arguments,
)

data class FieldInfo(
    val name: String,
    val type: String,
)

sealed interface ProcessorResult {
    val classInfo: ClassInfo
}

class KeepClassResult(
    override val classInfo: ClassInfo,
) : ProcessorResult

class KeepDescendantsResult(
    override val classInfo: ClassInfo,
    val parentIsInterface: Boolean,
) : ProcessorResult

class KeepConstructorResult(
    override val classInfo: ClassInfo,
    val constructorInfo: ConstructorInfo,
) : ProcessorResult

class KeepFunctionResult(
    override val classInfo: ClassInfo,
    val functionInfo: FunctionInfo,
) : ProcessorResult

class KeepFieldResult(
    override val classInfo: ClassInfo,
    val fieldInfo: FieldInfo,
) : ProcessorResult

class KeepPropertyResult(
    override val classInfo: ClassInfo,
    val name: String,
    val fields: List<FieldInfo>,
    val functions: List<FunctionInfo>,
) : ProcessorResult

sealed class Arguments {
    object AllOverrides : Arguments()
    object Empty : Arguments()

    class ActualArguments(val args: List<String>) : Arguments()
}
