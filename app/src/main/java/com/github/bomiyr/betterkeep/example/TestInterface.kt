package com.github.bomiyr.betterkeep.example

import com.github.bomiyr.betterkeep.annotations.KeepAllMembersInDescendantsIfClassKept
import com.github.bomiyr.betterkeep.annotations.KeepDescendants
import com.github.bomiyr.betterkeep.annotations.KeepDescendantsWithAllMembers

@KeepDescendants
@KeepDescendantsWithAllMembers
@KeepAllMembersInDescendantsIfClassKept
interface TestInterface {
}