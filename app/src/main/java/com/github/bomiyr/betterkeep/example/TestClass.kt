package com.github.bomiyr.betterkeep.example

import com.github.bomiyr.betterkeep.annotations.KeepAllClassMembersIfClassKept
import com.github.bomiyr.betterkeep.annotations.KeepAllMembersInDescendantsIfClassKept
import com.github.bomiyr.betterkeep.annotations.KeepClass
import com.github.bomiyr.betterkeep.annotations.KeepClassAndAllMembers
import com.github.bomiyr.betterkeep.annotations.KeepDescendants
import com.github.bomiyr.betterkeep.annotations.KeepDescendantsWithAllMembers
import com.github.bomiyr.betterkeep.annotations.KeepModifiers

@KeepClass
@KeepClassAndAllMembers
@KeepAllClassMembersIfClassKept
@KeepDescendants
@KeepDescendantsWithAllMembers
@KeepAllMembersInDescendantsIfClassKept
open class TestClass {

    @KeepClass
    @KeepClassAndAllMembers
    @KeepAllClassMembersIfClassKept
    @KeepDescendants
    @KeepDescendantsWithAllMembers
    @KeepAllMembersInDescendantsIfClassKept
    open inner class InnerTestClass{
        @KeepClass
        @KeepClassAndAllMembers
        @KeepAllClassMembersIfClassKept
        @KeepDescendants
        @KeepDescendantsWithAllMembers
        @KeepAllMembersInDescendantsIfClassKept
        open inner class InnerInnerTestClass
    }

    @KeepClass
    @KeepClassAndAllMembers
    @KeepAllClassMembersIfClassKept
    @KeepDescendants
    @KeepDescendantsWithAllMembers
    @KeepAllMembersInDescendantsIfClassKept
    open class StaticInnerTestClass
}
