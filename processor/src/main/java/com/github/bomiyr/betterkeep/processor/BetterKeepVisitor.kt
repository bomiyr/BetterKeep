package com.github.bomiyr.betterkeep.processor

import com.github.bomiyr.betterkeep.annotations.BetterKeep
import com.github.bomiyr.betterkeep.rulesgenerator.*
import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid

class BetterKeepVisitor(env: SymbolProcessorEnvironment) : KSVisitorVoid() {

    private val codeGenerator: CodeGenerator = env.codeGenerator
    private val log = env.logger

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val packageName = classDeclaration.packageName.asString()
        val fileName = classDeclaration.qualifiedName!!.asString().removePrefix("$packageName.")
        val srcFile = classDeclaration.containingFile!!
        val className = getClassName(classDeclaration, null)

        log.warn("classDeclaration.qualifiedName:${classDeclaration.qualifiedName!!.asString()}")
        log.warn("pkg:${packageName}")
        log.warn("fileName:${fileName}")
        log.warn("srcFile:${srcFile}")
        log.warn("classDeclaration.docString:${classDeclaration.docString}")
        log.warn("parentDeclaration:${classDeclaration.parentDeclaration?.qualifiedName?.asString()}")
        log.warn("innerClass:$className")

        classDeclaration.annotations
            .filter {
                it.annotationType.resolve().declaration.qualifiedName?.asString() == BetterKeep::class.qualifiedName
            }
            .forEach {

                val keepClass = it.arguments.firstOrNull { argument ->
                    argument.name?.asString() == "keepClass"
                }?.value == true

                val keepAllMembers = it.arguments.firstOrNull { argument ->
                    argument.name?.asString() == "keepAllMembers"
                }?.value == true

                val proguardClassDef = classDef {
                    names += ClassNameSpec(className)
                    if (keepAllMembers) {
                        members += AnyFieldOrMethod
                    }
                }
                val proguardRule = if (keepClass) {
                    keep(proguardClassDef)
                } else {
                    keepClassMembers(proguardClassDef)
                }
                codeGenerator.createNewFile(
                    Dependencies(aggregating = false, srcFile),
                    packageName,
                    fileName,
                    "BetterKeepPro"
                ).writer()
                    .use {
                        it.write(proguardRule)
                        it.flush()
                    }
            }
    }

    fun getClassName(classDeclaration: KSClassDeclaration, innerClassName: String?): String {
        val parentDeclaration = classDeclaration.parentDeclaration
        val qualifiedName = classDeclaration.qualifiedName!!.asString()
        return if (parentDeclaration != null) {
            val parentQualifiedName = parentDeclaration.qualifiedName!!.asString()
            var className = qualifiedName.replace("$parentQualifiedName.", "")
            if (innerClassName != null) {
                className = "$className$$innerClassName"
            }
            getClassName(parentDeclaration.closestClassDeclaration()!!, className)
        } else if (innerClassName != null) {
            "$qualifiedName$$innerClassName"
        } else {
            qualifiedName
        }
    }
}