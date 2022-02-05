package com.github.bomiyr.betterkeep.rulesgenerator

import org.junit.Assert
import org.junit.Test

class TypeTest {
    @Test
    fun verifyAnyPrimitive() {
        val anyPrimitive = Type.anyPrimitive()
        Assert.assertEquals("%", anyPrimitive.value)
    }

    @Test
    fun verifyPattern() {
        val pattern = Type.pattern("**Example*")
        Assert.assertEquals("**Example*", pattern.value)
    }

    @Test
    fun verifyAny() {
        val pattern = Type.any()
        Assert.assertEquals("***", pattern.value)
    }

    @Test
    fun builderThrowsWhenAppendTripleAsterisk() {
        val builder = Type.builder()
        Assert.assertThrows(IllegalArgumentException::class.java) {
            builder.append("***")
        }
    }

    @Test
    fun builderThrowsWhenAppendDoubleAsterisk() {
        val builder = Type.builder()
        Assert.assertThrows(IllegalArgumentException::class.java) {
            builder.append("**")
        }
    }

    @Test
    fun builderThrowsWhenAppendAsterisk() {
        val builder = Type.builder()
        Assert.assertThrows(IllegalArgumentException::class.java) {
            builder.append("*")
        }
    }

    @Test
    fun builderThrowsWhenAppendQuestionMark() {
        val builder = Type.builder()
        Assert.assertThrows(IllegalArgumentException::class.java) {
            builder.append("?")
        }
    }

    @Test
    fun builderThrowsWhenAppendPercentSign() {
        val builder = Type.builder()
        Assert.assertThrows(IllegalArgumentException::class.java) {
            builder.append("%")
        }
    }

    @Test
    fun builderAppendSingleChar() {
        val pattern = Type.builder()
            .appendSingleChar()
            .build()
            .value

        Assert.assertEquals("?", pattern)
    }

    @Test
    fun builderAppendSingleCharNumbered() {
        val builder = Type.builder()
        val firstNumber = builder.appendSingleCharNumbered()
        Assert.assertEquals(1, firstNumber)

        val secondNumber = builder.appendSingleCharNumbered()
        Assert.assertEquals(2, secondNumber)

        val pattern = builder.build().value

        Assert.assertEquals("??", pattern)
    }

    @Test
    fun builderAppendNamePart() {
        val pattern = Type.builder()
            .appendNamePart()
            .build()
            .value

        Assert.assertEquals("*", pattern)
    }

    @Test
    fun builderAppendNamePartNumbered() {
        val builder = Type.builder()
        val firstNumber = builder.appendNamePartNumbered()
        Assert.assertEquals(1, firstNumber)
        builder.append(".")
        val secondNumber = builder.appendNamePartNumbered()
        Assert.assertEquals(2, secondNumber)

        val pattern = builder.build().value

        Assert.assertEquals("*.*", pattern)
    }

    @Test
    fun builderAppendPackagePart() {
        val pattern = Type.builder()
            .appendPackagePart()
            .build()
            .value

        Assert.assertEquals("**", pattern)
    }

    @Test
    fun builderAppendPackagePartNumbered() {
        val builder = Type.builder()
        val firstNumber = builder.appendPackagePartNumbered()
        Assert.assertEquals(1, firstNumber)
        builder.append("_")
        val secondNumber = builder.appendPackagePartNumbered()
        Assert.assertEquals(2, secondNumber)

        val pattern = builder.build().value

        Assert.assertEquals("**_**", pattern)
    }

    @Test
    fun builderRepeatPatternPart() {
        val builder = Type.builder()
        val firstNumber = builder.appendSingleCharNumbered()
        builder.append("_")
        builder.repeatPatternPart(firstNumber)
        val pattern = builder.build().value

        Assert.assertEquals("?_<1>", pattern)
    }

}