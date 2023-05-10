package com.github.bomiyr.betterkeep.example;

import com.github.bomiyr.betterkeep.annotations.KeepAllClassMembersIfClassKept;
import com.github.bomiyr.betterkeep.annotations.KeepAllMembersInDescendantsIfClassKept;
import com.github.bomiyr.betterkeep.annotations.KeepClass;
import com.github.bomiyr.betterkeep.annotations.KeepClassAndAllMembers;
import com.github.bomiyr.betterkeep.annotations.KeepDescendants;
import com.github.bomiyr.betterkeep.annotations.KeepDescendantsWithAllMembers;
import com.github.bomiyr.betterkeep.annotations.KeepMember;
import com.github.bomiyr.betterkeep.annotations.KeepMemberAndClass;

@KeepClass
@KeepClassAndAllMembers
@KeepAllClassMembersIfClassKept
@KeepDescendants
@KeepDescendantsWithAllMembers
@KeepAllMembersInDescendantsIfClassKept
public class TestJavaClass {

    @KeepMember
    @KeepMemberAndClass
    TestJavaClass(int i, String s) {

    }

    @KeepMember
    @KeepMemberAndClass
    int field = 0;

    @KeepMember
    @KeepMemberAndClass
    int function(int i, String s) {
        return 0;
    }

    @KeepClass
    @KeepClassAndAllMembers
    @KeepAllClassMembersIfClassKept
    @KeepDescendants
    @KeepDescendantsWithAllMembers
    @KeepAllMembersInDescendantsIfClassKept
    class TestJavaInnerClass {
        @KeepClass
        @KeepClassAndAllMembers
        @KeepAllClassMembersIfClassKept
        @KeepDescendants
        @KeepDescendantsWithAllMembers
        @KeepAllMembersInDescendantsIfClassKept
        class TestJavaInnerInnerClass {

        }
    }

    @KeepClass
    @KeepClassAndAllMembers
    @KeepAllClassMembersIfClassKept
    @KeepDescendants
    @KeepDescendantsWithAllMembers
    @KeepAllMembersInDescendantsIfClassKept
    static class StaticTestJavaInnerClass {
        @KeepClass
        @KeepClassAndAllMembers
        @KeepAllClassMembersIfClassKept
        @KeepDescendants
        @KeepDescendantsWithAllMembers
        @KeepAllMembersInDescendantsIfClassKept
        class StaticTestJavaInnerInnerClass {

        }

        @KeepClass
        @KeepClassAndAllMembers
        @KeepAllClassMembersIfClassKept
        @KeepDescendants
        @KeepDescendantsWithAllMembers
        @KeepAllMembersInDescendantsIfClassKept
        static class StaticTestJavaInnerStatocInnerClass {
        }
    }
}
