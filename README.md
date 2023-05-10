# BetterKeep
Proguard rules with annotations

Keep annotations for classes and interfaces:
```
@KeepClass ->
-keep class classname

@KeepClassAndAllMembers ->
-keep class classname {
    *;
}

@KeepAllClassMembersIfClassKept ->
-keepclassmembers class classname {
    *;
}

@KeepDescendants ->
-keep class ** extends classname
or
-keep class ** implements interfacename

@KeepDescendantsWithAllMembers ->
-keep class ** extends classname {
    *;
}
or
-keep class ** implements interfacename {
    *;
}

@KeepAllMembersInDescendantsIfClassKept ->
-keepclassmembers class ** extends classname {
    *;
}
or
-keepclassmembers class ** implements interfacename {
    *;
}
```

Keep annotations for members:
```
@KeepMember ->

on constructor:
-keepclassmembers class classname {
    <init>(...);
}

on function:
-keepclassmembers class classname {
    *** functionName(...);
}

on field:
-keepclassmembers class classname {
    *** fieldName;
}

@KeepMemberAndClass

on constructor:
-keep class classname {
    <init>(...);
}

on function:
-keep class classname {
    *** functionName(...);
}

on field:
-keep class classname {
    *** fieldName;
}
```

Modifiers can be added for ANY rule with annotation argument. Example:
```
@KeepClass(SHRINK or OBFUSCATE or OPTIMIZE) ->

-keep,allowshrinking,allowobfuscation,allowoptimization class classname
```

Default modifiers are `EMPTY` = no modifiers. If you want to set default modifiers for all rules generated by BetterKeep, it is possible to add with ksp parameter in your build.gradle:
```
ksp {
    arg(bkeep.ARG_DEFAULT_FLAGS, (bkeep.SHRINK or bkeep.OBFUSCATE or bkeep.OPTIMIZE).toString())
}
```