package com.github.bomiyr.betterkeep.example

import com.github.bomiyr.betterkeep.annotations.BetterKeep

@BetterKeep(false)
class TestClass {

    @BetterKeep
    inner class InnerTestClass{
        @BetterKeep
        inner class InnerInnerTestClass
    }

    @BetterKeep
    class StaticInnerTestClass
}
