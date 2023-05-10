package com.github.bomiyr.betterkeep.annotations

@Target(AnnotationTarget.CLASS)
annotation class KeepClass(val flags: Int = KeepModifiers.DEFAULT)

@Target(AnnotationTarget.CLASS)
annotation class KeepClassAndAllMembers(val flags: Int = KeepModifiers.DEFAULT)

@Target(AnnotationTarget.CLASS)
annotation class KeepAllClassMembersIfClassKept(val flags: Int = KeepModifiers.DEFAULT)

@Target(AnnotationTarget.CLASS)
annotation class KeepDescendants(val flags: Int = KeepModifiers.DEFAULT)

@Target(AnnotationTarget.CLASS)
annotation class KeepDescendantsWithAllMembers(val flags: Int = KeepModifiers.DEFAULT)

@Target(AnnotationTarget.CLASS)
annotation class KeepAllMembersInDescendantsIfClassKept(val flags: Int = KeepModifiers.DEFAULT)

@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.CONSTRUCTOR,
)
annotation class KeepMember(val flags: Int = KeepModifiers.DEFAULT)

@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.CONSTRUCTOR,
)
annotation class KeepMemberAndClass(val flags: Int = KeepModifiers.DEFAULT)


