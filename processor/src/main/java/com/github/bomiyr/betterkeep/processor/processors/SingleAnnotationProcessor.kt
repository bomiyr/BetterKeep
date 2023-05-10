package com.github.bomiyr.betterkeep.processor.processors

import com.github.bomiyr.betterkeep.processor.getAnnotation
import com.github.bomiyr.betterkeep.processor.getIntArgument
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import kotlin.reflect.KClass

abstract class SingleAnnotationProcessor(
    private val environment: SymbolProcessorEnvironment
) {
    abstract val annotationClass: KClass<*>

    protected val log = environment.logger

    private val annotationName
        get() = checkNotNull(annotationClass.qualifiedName)

    abstract fun process(
        resolver: Resolver,
        annotated: Sequence<KSAnnotated>
    ): Sequence<ProcessorResult>

    fun process(resolver: Resolver): Sequence<ProcessorResult> {
        log.logging("${this::class.simpleName}.process")
        val annotated = resolver.getSymbolsWithAnnotation(annotationName)

        val result = process(resolver, annotated)
        log.logging("${this::class.simpleName}.process finished")
        return result
    }

    protected fun Sequence<KSAnnotated>.filterClasses(): Sequence<KSClassDeclaration> {
        return this
            .filter {
                if (it is KSClassDeclaration) {
                    true
                } else {
                    log.error(
                        "@${annotationClass.simpleName} annotation should be used only on classes",
                        it
                    )
                    false
                }
            }
            .filterIsInstance<KSClassDeclaration>()
    }

    protected fun getFlagsFromAnnotation(declaration: KSAnnotated): Int? {
        return declaration
            .getAnnotation(annotationClass)
            ?.getIntArgument("flags")
    }
}
