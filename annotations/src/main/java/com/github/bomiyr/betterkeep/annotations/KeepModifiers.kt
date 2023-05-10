package com.github.bomiyr.betterkeep.annotations

object KeepModifiers {
    /**
     * By default has the same behavior as EMPTY, but can be changed with argument "DefaultFlags" for KSP.
     */
    const val DEFAULT = -1
    const val EMPTY = 0
    const val SHRINK = 1
    const val OBFUSCATE = 1 shl 1
    const val OPTIMIZE = 1 shl 2
    const val INCLUDE_CODE = 1 shl 3
    const val INCLUDE_DESCRIPTOR_CLASSES = 1 shl 4

    const val MODIFY_ALL = SHRINK or OBFUSCATE or OPTIMIZE
}
