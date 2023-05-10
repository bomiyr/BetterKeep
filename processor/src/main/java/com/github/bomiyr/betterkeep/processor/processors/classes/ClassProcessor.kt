package com.github.bomiyr.betterkeep.processor.processors.classes

import com.github.bomiyr.betterkeep.processor.getClassName
import com.github.bomiyr.betterkeep.processor.processors.ClassInfo
import com.github.bomiyr.betterkeep.processor.processors.KeepClassResult
import com.github.bomiyr.betterkeep.processor.processors.ProcessorResult
import com.github.bomiyr.betterkeep.processor.processors.SingleAnnotationProcessor
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import kotlin.reflect.KClass

abstract class ClassProcessor(
    environment: SymbolProcessorEnvironment,
    override val annotationClass: KClass<*>,
    val keepClass: Boolean,
    val keepAllMembers: Boolean,
) : SingleAnnotationProcessor(environment) {

    open fun createClassResult(
        classDeclaration: KSClassDeclaration,
        classInfo: ClassInfo,
    ): ProcessorResult = KeepClassResult(classInfo)

    override fun process(
        resolver: Resolver,
        annotated: Sequence<KSAnnotated>
    ): Sequence<ProcessorResult> {
        return annotated
            .filterClasses()
            .map { classDeclaration ->
                val fullName = classDeclaration.getClassName()
                val packageName = classDeclaration.packageName.asString()

                val flags = getFlagsFromAnnotation(classDeclaration)
                checkNotNull(flags)

                val classInfo = ClassInfo(
                    flags,
                    classDeclaration.containingFile,
                    fullName,
                    packageName,
                    keepClass,
                    keepAllMembers
                )

                createClassResult(classDeclaration, classInfo)
            }
    }
}
