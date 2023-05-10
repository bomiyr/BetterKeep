package com.github.bomiyr.betterkeep.processor.processors.classes

import com.github.bomiyr.betterkeep.annotations.KeepAllClassMembersIfClassKept
import com.github.bomiyr.betterkeep.annotations.KeepClass
import com.github.bomiyr.betterkeep.annotations.KeepClassAndAllMembers
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment

class KeepClassProcessor(
    environment: SymbolProcessorEnvironment
) : ClassProcessor(
    environment,
    KeepClass::class,
    keepClass = true,
    keepAllMembers = false,
)

class KeepClassAndAllMembersProcessor(
    environment: SymbolProcessorEnvironment
) : ClassProcessor(
    environment,
    KeepClassAndAllMembers::class,
    keepClass = true,
    keepAllMembers = true
)

class KeepAllClassMembersIfClassKeptProcessor(
    environment: SymbolProcessorEnvironment
) : ClassProcessor(
    environment,
    KeepAllClassMembersIfClassKept::class,
    keepClass = false,
    keepAllMembers = true
)

