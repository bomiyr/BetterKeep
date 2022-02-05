package com.github.bomiyr.betterkeep.example

import com.github.bomiyr.betterkeep.annotations.BetterKeep

@BetterKeep
class TestClass {

    @BetterKeep
    inner class InnerTestClass{
        @BetterKeep
        inner class InnerInnerTestClass
    }

    @BetterKeep
    class StaticInnerTestClass
}
