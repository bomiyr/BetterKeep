package com.github.bomiyr.betterkeep.processor.processors.members

import com.github.bomiyr.betterkeep.annotations.KeepMemberAndClass
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment

class KeepMemberAndClassProcessor(
    environment: SymbolProcessorEnvironment
) : MemberProcessor(environment, KeepMemberAndClass::class, keepClass = true)
