package com.github.bomiyr.betterkeep.processor

import com.github.bomiyr.betterkeep.annotations.KeepModifiers
import com.github.bomiyr.betterkeep.processor.processors.Arguments
import com.github.bomiyr.betterkeep.processor.processors.ClassInfo
import com.github.bomiyr.betterkeep.processor.processors.KeepClassResult
import com.github.bomiyr.betterkeep.processor.processors.KeepConstructorResult
import com.github.bomiyr.betterkeep.processor.processors.KeepDescendantsResult
import com.github.bomiyr.betterkeep.processor.processors.KeepFieldResult
import com.github.bomiyr.betterkeep.processor.processors.KeepFunctionResult
import com.github.bomiyr.betterkeep.processor.processors.KeepPropertyResult
import com.github.bomiyr.betterkeep.processor.processors.ProcessorResult
import com.github.bomiyr.betterkeep.processor.processors.classes.KeepAllClassMembersIfClassKeptProcessor
import com.github.bomiyr.betterkeep.processor.processors.classes.KeepAllMembersInDescendantsIfClassKeptProcessor
import com.github.bomiyr.betterkeep.processor.processors.classes.KeepClassAndAllMembersProcessor
import com.github.bomiyr.betterkeep.processor.processors.classes.KeepClassProcessor
import com.github.bomiyr.betterkeep.processor.processors.classes.KeepDescendantsProcessor
import com.github.bomiyr.betterkeep.processor.processors.classes.KeepDescendantsWithAllMembersProcessor
import com.github.bomiyr.betterkeep.processor.processors.members.KeepMemberAndClassProcessor
import com.github.bomiyr.betterkeep.processor.processors.members.KeepMemberProcessor
import com.github.bomiyr.betterkeep.rulesgenerator.AnyFieldOrMethod
import com.github.bomiyr.betterkeep.rulesgenerator.ClassDef
import com.github.bomiyr.betterkeep.rulesgenerator.ClassNameSpec
import com.github.bomiyr.betterkeep.rulesgenerator.MemberNameSpec
import com.github.bomiyr.betterkeep.rulesgenerator.Type
import com.github.bomiyr.betterkeep.rulesgenerator.classDef
import com.github.bomiyr.betterkeep.rulesgenerator.keep
import com.github.bomiyr.betterkeep.rulesgenerator.keepClassMembers
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.github.bomiyr.betterkeep.rulesgenerator.KeepModifiers as RuleModifiers

class BetterKeepProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    private val log = environment.logger

    private val processors = arrayOf(
        KeepClassProcessor(environment),
        KeepClassAndAllMembersProcessor(environment),
        KeepAllClassMembersIfClassKeptProcessor(environment),
        KeepDescendantsProcessor(environment),
        KeepDescendantsWithAllMembersProcessor(environment),
        KeepAllMembersInDescendantsIfClassKeptProcessor(environment),
        KeepMemberProcessor(environment),
        KeepMemberAndClassProcessor(environment),
    )

    private val rules: MutableSet<GeneratedFileInfo> = mutableSetOf()

    private val defaultFlags = environment
        .options["DefaultFlags"]
        ?.toIntOrNull()
        ?: KeepModifiers.EMPTY

    override fun process(resolver: Resolver): List<KSAnnotated> {

        log.logging("BetterKeepProcessor.process")

        val results = processors.flatMap {
            it.process(resolver)
        }


        results.forEach {

            val classInfo = it.classInfo

            when (it) {
                is KeepClassResult -> {
                    val rule = createClassRule(it)
                    rules += it.toGeneratedFileInfo(rule)
                }

                is KeepDescendantsResult -> {
                    val rule = createDescendantsRule(it)
                    rules += it.toGeneratedFileInfo(rule)
                }

                is KeepConstructorResult -> {
                    val rule = createConstructorRule(it)
                    rules += it.toGeneratedFileInfo(rule)
                }

                is KeepFieldResult -> {
                    val rule = createFieldRule(it)
                    rules += it.toGeneratedFileInfo(rule)
                }

                is KeepFunctionResult -> {
                    val rule = createFunctionRule(it)
                    rules += it.toGeneratedFileInfo(rule)
                }

                is KeepPropertyResult -> {
                    val rule = createCombinedRule(it)
                    rules += it.toGeneratedFileInfo(rule)
                }
            }
        }

        return emptyList()
    }

    override fun finish() {
        val codeGenerator = environment.codeGenerator

        rules.groupBy {
            it.packageName + it.fileName
        }.flatMap { (_, singleFileRules) ->
            val dep = singleFileRules
                .map { it.dependency }
                .reduce { acc, fileInfo ->
                    if (acc != fileInfo) {
                        throw Exception("Different dependendency files for the same class. WTF?")
                    }
                    acc
                }

            val packageName = singleFileRules.first().packageName
            val fileName = singleFileRules.first().fileName

            codeGenerator
                .createNewFile(
                    if (dep != null) {
                        Dependencies(aggregating = false, dep)
                    } else {
                        Dependencies(aggregating = false)
                    },
                    packageName,
                    fileName,
                    "BetterKeep.pro"
                )
                .bufferedWriter()
                .use { writer ->
                    singleFileRules.forEach {
                        writer.write(it.rule)
                        writer.newLine()
                        writer.newLine()
                    }
                    writer.flush()
                }

            if (singleFileRules.size > 1) {
                singleFileRules.mapIndexed { index, fileInfo ->
                    GeneratedFileInfo(
                        fileInfo.dependency,
                        fileInfo.packageName,
                        fileInfo.fileName + index,
                        fileInfo.rule
                    )
                }
            } else {
                singleFileRules
            }
        }

        val allDependencies = rules.mapNotNull { it.dependency }.toTypedArray()

        val allGeneratedFiles = environment.codeGenerator
            .generatedFile
            .filter { it.name.endsWith("BetterKeep.pro") }


        val configFile = environment.codeGenerator.createNewFile(
            Dependencies(true, *allDependencies),
            "",
            "betterkeep-config",
            "BetterKeep.pro"
        )


        configFile.bufferedWriter().use { writer ->
            allGeneratedFiles.forEach {
                writer.append("-include ${it.absolutePath}")
                writer.newLine()
            }
            writer.flush()
        }
    }


    private fun createCombinedRule(
        it: KeepPropertyResult,
    ): String {
        val classInfo = it.classInfo

        val modifiers = classInfo.modifiers

        val classDef = classDef {
            applyClassInfo(classInfo)

            it.fields.forEach {
                addField {
                    this.field(Type.pattern(it.type), MemberNameSpec(it.name))
                }
            }

            it.functions.forEach {
                addMethod {
                    when (it.arguments) {
                        is Arguments.ActualArguments -> {
                            val types = it.arguments.args.map { Type.pattern(it) }
                            method(
                                Type.pattern(it.returnType),
                                MemberNameSpec.Builder().append(it.name).build(),
                                *types.toTypedArray()
                            )
                        }

                        Arguments.AllOverrides ->
                            methodWithAnyArguments(
                                Type.pattern(it.returnType),
                                MemberNameSpec.Builder().append(it.name).build()
                            )

                        Arguments.Empty ->
                            method(
                                Type.pattern(it.returnType),
                                MemberNameSpec.Builder().append(it.name).build()
                            )
                    }
                }
            }
        }

        return if (classInfo.keepClass) {
            keep(classDef, modifiers)
        } else {
            keepClassMembers(classDef, modifiers)
        }
    }

    private fun createConstructorRule(
        it: KeepConstructorResult,
    ): String {
        val classInfo = it.classInfo
        val modifiers = classInfo.modifiers

        val classDef = classDef {
            applyClassInfo(classInfo)

            when (val arguments = it.constructorInfo.arguments) {

                is Arguments.ActualArguments -> {
                    val types = arguments.args.map { argument ->
                        Type.pattern(argument)
                    }
                    addMethod { constructor(*types.toTypedArray()) }
                }

                Arguments.AllOverrides -> {
                    addMethod {
                        anyConstructor()
                    }
                }

                Arguments.Empty -> {
                    addMethod {
                        constructor()
                    }
                }
            }
        }

        val rule = if (classInfo.keepClass) {
            keep(classDef, modifiers)
        } else {
            keepClassMembers(classDef, modifiers)
        }
        return rule
    }

    private fun createDescendantsRule(
        it: KeepDescendantsResult,
    ): String {
        val classInfo = it.classInfo
        val modifiers = classInfo.modifiers

        val classDef = classDef {
            addName {
                appendAnyPackage()
            }

            if (it.parentIsInterface) {
                implements(ClassNameSpec(classInfo.className))
            } else {
                extends(ClassNameSpec(classInfo.className))
            }
            if (classInfo.keepAllMembers) {
                members += AnyFieldOrMethod
            }
        }


        return if (classInfo.keepClass) {
            keep(classDef, modifiers)
        } else {
            keepClassMembers(classDef, modifiers)
        }
    }

    private fun createClassRule(
        it: KeepClassResult,
    ): String {
        val classInfo = it.classInfo
        val modifiers = classInfo.modifiers

        val classDef = classDef {
            applyClassInfo(classInfo)
        }

        val rule = if (classInfo.keepClass) {
            keep(classDef, modifiers)
        } else {
            keepClassMembers(classDef, modifiers)
        }
        return rule
    }

    private fun createFieldRule(
        it: KeepFieldResult,
    ): String {
        val classInfo = it.classInfo
        val fieldInfo = it.fieldInfo
        val modifiers = classInfo.modifiers

        val classDef = classDef {
            applyClassInfo(classInfo)

            addField {
                this.field(Type.pattern(fieldInfo.type), MemberNameSpec(fieldInfo.name))
            }
        }

        val rule = if (classInfo.keepClass) {
            keep(classDef, modifiers)
        } else {
            keepClassMembers(classDef, modifiers)
        }
        return rule
    }

    private fun createFunctionRule(
        it: KeepFunctionResult,
    ): String {
        val classInfo = it.classInfo
        val funInfo = it.functionInfo

        val modifiers = classInfo.modifiers

        val classDef = classDef {
            applyClassInfo(classInfo)

            addMethod {
                when (funInfo.arguments) {
                    is Arguments.ActualArguments -> {
                        val types = funInfo.arguments.args.map { Type.pattern(it) }
                        method(
                            Type.pattern(funInfo.returnType),
                            MemberNameSpec.Builder().append(funInfo.name).build(),
                            *types.toTypedArray()
                        )
                    }

                    Arguments.AllOverrides ->
                        this.methodWithAnyArguments(
                            Type.pattern(funInfo.returnType),
                            MemberNameSpec.Builder().append(funInfo.name).build()
                        )

                    Arguments.Empty -> this.method(
                        Type.pattern(funInfo.returnType),
                        MemberNameSpec.Builder().append(funInfo.name).build()
                    )
                }
            }
        }

        return if (classInfo.keepClass) {
            keep(classDef, modifiers)
        } else {
            keepClassMembers(classDef, modifiers)
        }
    }

    private fun flagsToModifiers(flags: Int): MutableSet<com.github.bomiyr.betterkeep.rulesgenerator.KeepModifiers> {
        val modifiers = mutableSetOf<RuleModifiers>()
        if (flags and KeepModifiers.SHRINK > 0) {
            modifiers += RuleModifiers.AllowShrinking
        }
        if (flags and KeepModifiers.OBFUSCATE > 0) {
            modifiers += RuleModifiers.AllowObfuscation
        }
        if (flags and KeepModifiers.OPTIMIZE > 0) {
            modifiers += RuleModifiers.AllowOptimisation
        }
        if (flags and KeepModifiers.INCLUDE_CODE > 0) {
            modifiers += RuleModifiers.IncludeCode
        }
        if (flags and KeepModifiers.INCLUDE_DESCRIPTOR_CLASSES > 0) {
            modifiers += RuleModifiers.IncludeDescriptorClasses
        }
        return modifiers
    }

    private fun ClassDef.applyClassInfo(classInfo: ClassInfo) {
        addName {
            setClassName(classInfo.className)
        }

        if (classInfo.keepAllMembers) {
            members += AnyFieldOrMethod
        }
    }

    private val ClassInfo.modifiers: Set<com.github.bomiyr.betterkeep.rulesgenerator.KeepModifiers>
        get() {
            return if (flags == KeepModifiers.DEFAULT) {
                flagsToModifiers(defaultFlags)
            } else {
                flagsToModifiers(flags)
            }
        }

    private fun ProcessorResult.toGeneratedFileInfo(rule: String): GeneratedFileInfo {
        val fileName = classInfo.className
            .substringAfterLast(".")
            .replace("$", "_")

        return GeneratedFileInfo(
            classInfo.dependencyFile,
            classInfo.packageName,
            fileName,
            rule
        )
    }

    private fun argsPart(arguments: Arguments) =
        when (arguments) {
            is Arguments.ActualArguments -> arguments.args.joinToString("_")
            Arguments.AllOverrides -> "all"
            Arguments.Empty -> "empty"
        }
}