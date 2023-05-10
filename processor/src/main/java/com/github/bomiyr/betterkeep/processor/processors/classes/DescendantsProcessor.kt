package com.github.bomiyr.betterkeep.processor.processors.classes

import com.github.bomiyr.betterkeep.processor.processors.ClassInfo
import com.github.bomiyr.betterkeep.processor.processors.KeepDescendantsResult
import com.github.bomiyr.betterkeep.processor.processors.ProcessorResult
import com.google.devtools.ksp.isOpen
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import kotlin.reflect.KClass

abstract class DescendantsProcessor(
    environment: SymbolProcessorEnvironment,
    override val annotationClass: KClass<*>,
    keepAllMembers: Boolean,
    keepClass: Boolean,
) : ClassProcessor(environment, annotationClass, keepClass, keepAllMembers) {

    override fun createClassResult(
        classDeclaration: KSClassDeclaration,
        classInfo: ClassInfo,
    ): ProcessorResult {
        if (!classDeclaration.isOpen()) {
            log.error(
                "@${annotationClass.simpleName} should be used on open (non-final) classes or interfaces",
                classDeclaration
            )
        }

        val isInterface = classDeclaration.classKind == ClassKind.INTERFACE
        return KeepDescendantsResult(classInfo, isInterface)
    }

}
