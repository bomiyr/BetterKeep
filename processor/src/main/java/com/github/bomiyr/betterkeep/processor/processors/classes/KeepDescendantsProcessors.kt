package com.github.bomiyr.betterkeep.processor.processors.classes

import com.github.bomiyr.betterkeep.annotations.KeepAllMembersInDescendantsIfClassKept
import com.github.bomiyr.betterkeep.annotations.KeepDescendants
import com.github.bomiyr.betterkeep.annotations.KeepDescendantsWithAllMembers
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment

class KeepDescendantsProcessor(
    environment: SymbolProcessorEnvironment
) : DescendantsProcessor(
    environment,
    KeepDescendants::class,
    keepAllMembers = false,
    keepClass = true,
)

class KeepDescendantsWithAllMembersProcessor(
    environment: SymbolProcessorEnvironment
) : DescendantsProcessor(
    environment,
    KeepDescendantsWithAllMembers::class,
    keepAllMembers = true,
    keepClass = true,
)


class KeepAllMembersInDescendantsIfClassKeptProcessor(
    environment: SymbolProcessorEnvironment
) : DescendantsProcessor(
    environment,
    KeepAllMembersInDescendantsIfClassKept::class,
    keepAllMembers = true,
    keepClass = false
)
