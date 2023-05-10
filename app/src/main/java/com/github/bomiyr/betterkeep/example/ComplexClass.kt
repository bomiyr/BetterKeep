package com.github.bomiyr.betterkeep.example

import com.github.bomiyr.betterkeep.annotations.KeepAllClassMembersIfClassKept
import com.github.bomiyr.betterkeep.annotations.KeepAllMembersInDescendantsIfClassKept
import com.github.bomiyr.betterkeep.annotations.KeepClass
import com.github.bomiyr.betterkeep.annotations.KeepClassAndAllMembers
import com.github.bomiyr.betterkeep.annotations.KeepDescendants
import com.github.bomiyr.betterkeep.annotations.KeepDescendantsWithAllMembers
import com.github.bomiyr.betterkeep.annotations.KeepMember
import com.github.bomiyr.betterkeep.annotations.KeepMemberAndClass
import kotlin.random.Random

@KeepClass
@KeepClassAndAllMembers
@KeepAllClassMembersIfClassKept
@KeepDescendants
@KeepDescendantsWithAllMembers
@KeepAllMembersInDescendantsIfClassKept
open class ComplexClass @KeepMember @KeepMemberAndClass constructor() {

    @KeepMember
    @KeepMemberAndClass
    constructor(o: Int, b: String) : this()

    val obfuscatedField: Int? = null

    @get:KeepMember
    @get:KeepMemberAndClass
    val nonObfuscatedField: Int? = null

    @KeepMember
    @KeepMemberAndClass
    var member1: Int? = null

    @set:KeepMember
    @set:KeepMemberAndClass
    var member2: Int? = null

    @KeepMember
    @KeepMemberAndClass
    fun function1(a: Int, b: String) = Unit

    @KeepMember
    @KeepMemberAndClass
    var member3: Int
        @KeepMember
        @KeepMemberAndClass
        get() = Random(10).nextInt()
        @KeepMember
        @KeepMemberAndClass
        set(value) {
            Random(value)
        }

    @KeepMember
    @KeepMemberAndClass
    var member4: Int = 0
        @KeepMember
        @KeepMemberAndClass
        get() = field / 2
        @KeepMember
        @KeepMemberAndClass
        set(value) {
            field = value * 2
        }

    @KeepMember
    @KeepMemberAndClass
    var member5: Boolean = false
        @KeepMember
        @KeepMemberAndClass
        get() = !field
        @KeepMember
        @KeepMemberAndClass
        set(value) {
            field = !value
        }

    @KeepClass
    @KeepClassAndAllMembers
    @KeepAllClassMembersIfClassKept
    companion object {
        @KeepMember
        @KeepMemberAndClass
        const val CONSTANT = 1
    }
}
