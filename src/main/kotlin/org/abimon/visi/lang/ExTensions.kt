package org.abimon.visi.lang

import org.abimon.visi.collections.equalsBy
import java.util.*
import kotlin.reflect.full.primaryConstructor

fun <T> T.asOptional(): Optional<T> = Optional.of(this)

fun <T: Any> T.toPaddedString(paddedLength: Int, makeAsString: T.() -> String = { toString() }) : String {
    val str = this.makeAsString()
    return "${" " * (paddedLength - str.length)}$str"
}

fun <T: Any> T.toString(makeAsString: T.() -> String): String = this.makeAsString()

inline fun <reified T: Any> make(vararg args: Any, init: T.() -> Unit): T {
    var constructor = T::class.primaryConstructor ?: T::class.constructors.firstOrNull { con -> con.parameters.size == args.size && con.parameters.equalsBy { index, param -> param.type.canAssign(args[index]::class) } } ?: throw IllegalArgumentException("${T::class.qualifiedName} has no constructors with ${args.size} parameters!")
    if(constructor.parameters.isNotEmpty())
        constructor = T::class.constructors.firstOrNull { con -> con.parameters.size == args.size && con.parameters.equalsBy { index, param -> param.type.canAssign(args[index]::class) } } ?: throw IllegalArgumentException("${T::class.qualifiedName} has no constructors with ${args.size} parameters!")
    val t = constructor.call(*args)
    t.init()
    return t
}