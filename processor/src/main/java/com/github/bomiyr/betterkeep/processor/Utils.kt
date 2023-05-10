package com.github.bomiyr.betterkeep.processor

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import kotlin.reflect.KClass

fun KSAnnotation.getIntArgument(name: String): Int? {
    return arguments
        .firstOrNull { argument ->
            argument.name?.asString() == name
        }
        ?.value as? Int
}

fun KSAnnotated.getAnnotation(annotationClass: KClass<*>): KSAnnotation? {
    return this.annotations.find { annotation ->
        annotation.shortName.getShortName() == annotationClass.simpleName &&
                annotation.annotationType.resolve()
                    .declaration.qualifiedName?.asString() == annotationClass.qualifiedName
    }
}

fun KSClassDeclaration.getClassName(innerClassName: String? = null): String {
    val qualifiedName = checkNotNull(qualifiedName).asString()
    val parentDeclaration = parentDeclaration
    return if (parentDeclaration is KSClassDeclaration) {
        val parentQualifiedName = checkNotNull(parentDeclaration.qualifiedName).asString()
        var className = qualifiedName.removePrefix("$parentQualifiedName.")
        if (innerClassName != null) {
            className = "$className$$innerClassName"
        }
        parentDeclaration.getClassName(className)
    } else if (innerClassName != null) {
        "$qualifiedName$$innerClassName"
    } else {
        qualifiedName
    }
}
