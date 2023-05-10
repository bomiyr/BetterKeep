package com.github.bomiyr.betterkeep.processor.processors.members

import com.github.bomiyr.betterkeep.annotations.KeepMember
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment

class KeepMemberProcessor(
    environment: SymbolProcessorEnvironment
) : MemberProcessor(environment, KeepMember::class, keepClass = false)
