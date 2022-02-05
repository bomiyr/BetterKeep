package com.github.bomiyr.betterkeep.processor

import com.github.bomiyr.betterkeep.annotations.BetterKeep
import com.google.devtools.ksp.containingFile
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.validate

class BetterKeepProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    private val log = environment.logger

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotated = resolver.getSymbolsWithAnnotation(BetterKeep::class.qualifiedName!!)

        environment.logger.warn("BetterKeepProcessor.process")

        val processedClasses: MutableSet<KSFile> = mutableSetOf()

        annotated
            .filter { it.validate() }
            .filter { it is KSClassDeclaration }
            .forEach {
                it.accept(BetterKeepVisitor(environment), Unit)
                processedClasses += it.containingFile!!
            }

        if (processedClasses.isNotEmpty()) {
            environment.logger.warn("From Files " + processedClasses.toString())

            val generatedFiles = environment.codeGenerator.generatedFile.toList()

            generatedFiles.forEach {
                environment.logger.warn("GENERATED FILE " + it.absolutePath)
            }

            val configFile = environment.codeGenerator.createNewFile(
                Dependencies(false, processedClasses.first()),
                "",
                "betterkeep-config",
                "pro"
            )

            configFile.bufferedWriter().use { writer ->
                generatedFiles.forEach {
                    writer.append("-include ${it.absolutePath}")
                    writer.newLine()
                }
                writer.flush()
            }
        }

        val forNextRound = annotated.filterNot { it.validate() }
        log.info("goes to the next round: $forNextRound")
        return forNextRound.toList()
    }
}