package com.github.bomiyr.betterkeep.processor.processors.members

import com.github.bomiyr.betterkeep.processor.getClassName
import com.github.bomiyr.betterkeep.processor.processors.Arguments
import com.github.bomiyr.betterkeep.processor.processors.ClassInfo
import com.github.bomiyr.betterkeep.processor.processors.ConstructorInfo
import com.github.bomiyr.betterkeep.processor.processors.FieldInfo
import com.github.bomiyr.betterkeep.processor.processors.FunctionInfo
import com.github.bomiyr.betterkeep.processor.processors.KeepConstructorResult
import com.github.bomiyr.betterkeep.processor.processors.KeepFunctionResult
import com.github.bomiyr.betterkeep.processor.processors.KeepPropertyResult
import com.github.bomiyr.betterkeep.processor.processors.ProcessorResult
import com.github.bomiyr.betterkeep.processor.processors.SingleAnnotationProcessor
import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.isConstructor
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyAccessor
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSPropertyGetter
import com.google.devtools.ksp.symbol.Modifier
import kotlin.reflect.KClass

open class MemberProcessor(
    environment: SymbolProcessorEnvironment,
    override val annotationClass: KClass<*>,
    private val keepClass: Boolean,
) : SingleAnnotationProcessor(environment) {

    override fun process(
        resolver: Resolver,
        annotated: Sequence<KSAnnotated>
    ): Sequence<ProcessorResult> {
        return annotated
            .map { ksAnnotated ->

                val classInfo = createClassInfo(ksAnnotated)

                when (ksAnnotated) {
                    is KSFunctionDeclaration -> {
                        if (ksAnnotated.isConstructor()) {
                            val constructorInfo = processConstructorDeclaration(ksAnnotated)
                            KeepConstructorResult(classInfo, constructorInfo)
                        } else {
                            val functionInfo = processFunctionDeclaration(ksAnnotated)
                            KeepFunctionResult(classInfo, functionInfo)
                        }
                    }

                    is KSPropertyDeclaration -> {
                        processPropertyDeclaration(ksAnnotated, classInfo)
                    }

                    is KSPropertyAccessor -> {
                        val functionInfo = createFunctionInfoFromPropertyAccessor(ksAnnotated)
                        KeepFunctionResult(classInfo, functionInfo)
                    }

                    else -> {
                        log.error(
                            "${annotationClass.simpleName} should be used only on fields, properties or functions (including constructors and property accessors)",
                            ksAnnotated
                        )
                        throw Exception()
                    }
                }
            }
    }

    private fun processFunctionDeclaration(
        declaration: KSFunctionDeclaration,
    ): FunctionInfo {

        val args = argumentsFromFunctionDeclaration(declaration)


        // TODO: Need to investigate how to map Kotlin types to Java reliable
        //  enough. So apply rule for all types for now
        val returnType = "***"
        val name = declaration.simpleName.asString()
        return FunctionInfo(name, returnType, args)
    }

    private fun processConstructorDeclaration(
        declaration: KSFunctionDeclaration,
    ): ConstructorInfo {

        val args = argumentsFromFunctionDeclaration(declaration)

        return ConstructorInfo(args)
    }

    private fun argumentsFromFunctionDeclaration(declaration: KSFunctionDeclaration) =
        if (declaration.parameters.isEmpty()) {
            Arguments.Empty
        } else {
            // TODO: Need to investigate how to map Kotlin types to Java reliable
            //  enough. So apply rule for all overrides for now
            Arguments.AllOverrides
        }


    private fun processPropertyDeclaration(
        declaration: KSPropertyDeclaration,
        classInfo: ClassInfo,
    ): KeepPropertyResult {
        val name = declaration.simpleName.asString()
        val fields = if (declaration.hasBackingField) {
            // TODO: Need to investigate how to map Kotlin types to Java reliable
            //  enough. So apply rule for all types for now
            val type = "***"
            listOf(
                FieldInfo(name, type)
            )
        } else {
            emptyList()
        }

        val functions = mutableListOf<FunctionInfo>()

        // Getters and setters are not created for compile-time constants
        if (!declaration.modifiers.contains(Modifier.CONST)) {

            declaration.getter?.let {
                functions += createFunctionInfoFromPropertyAccessor(it)
            }

            declaration.setter?.let {
                functions += createFunctionInfoFromPropertyAccessor(it)
            }
        }
        return KeepPropertyResult(classInfo, name, fields, functions)
    }

    private fun createFunctionInfoFromPropertyAccessor(
        declaration: KSPropertyAccessor,
    ): FunctionInfo {

        val propertyName = declaration.receiver
            .simpleName
            .asString()
            .replaceFirstChar {
                it.uppercase()
            }

        val isGetter = declaration is KSPropertyGetter
        val functionName = if (isGetter) "get$propertyName" else "set$propertyName"
        val type = "***"
        val args = if (isGetter) {
            Arguments.Empty
        } else {
            // TODO: Need to investigate how to map Kotlin types to Java reliable
            //  enough. So apply rule for all overrides for now
            Arguments.AllOverrides
        }

        return FunctionInfo(functionName, type, args)
    }

    private fun getContainingClass(declaration: KSDeclaration) =
        declaration.closestClassDeclaration()?.getClassName()!!

    private fun createClassInfo(annotated: KSAnnotated): ClassInfo {

        val dependencyFile = annotated.containingFile

        val flags: Int?
        val className: String
        val packageName: String

        when (annotated) {
            is KSDeclaration -> {
                flags = getFlagsFromAnnotation(annotated)
                className = getContainingClass(annotated)
                packageName = annotated.packageName.asString()

            }

            is KSPropertyAccessor -> {
                flags = getFlagsFromAnnotation(annotated)
                    ?: getFlagsFromAnnotation(annotated.receiver)
                className = getContainingClass(annotated.receiver)
                packageName = annotated.receiver.packageName.asString()
            }

            else -> {
                throw Exception("Unknown element type")
            }
        }

        checkNotNull(flags)

        return ClassInfo(flags, dependencyFile, className, packageName, keepClass)
    }
}
