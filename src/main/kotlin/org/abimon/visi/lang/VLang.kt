package org.abimon.visi.lang

import org.abimon.visi.collections.equalsBy
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.*
import java.util.regex.PatternSyntaxException
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.primaryConstructor

enum class StringGroup(val start: String, val end: String) {
        SPEECH("\"", "\""),
        SINGLE_SPEECH("'", "'"),
        TILDE("`", "`"),
        PARENTHESES("(", ")"),
        BRACKETS("\"", "\""),
        BRACES("{", "}")
}

fun String.splitOutsideGroup(delimiter: String = "\\s+", cap: Int = 0, group: StringGroup = StringGroup.SPEECH): Array<String> {
    val strings = LinkedList<String>()

    for (string in split("$delimiter(?=([^\\Q${group.start}\\E]*\\Q${group.start}\\E[^\\Q${group.end}\\E]*\\Q${group.end}\\E)*[^\\Q${group.end}\\E]*$)".toRegex(), cap.coerceAtLeast(0)).toTypedArray()) {
        var str = string
        if (str.startsWith("\""))
            str = str.substring(1)
        if (str.endsWith("\""))
            str = str.substring(0, str.length - 1)
        strings.add(str)
    }

    return strings.toTypedArray()
}

fun String.numberOfOccurences(str: String): Int = split(str).size - 1
fun String.replaceLast(replace: String, replacing: String): String {
    if(lastIndexOf(replace) == -1)
        return this
    val before = substring(0, lastIndexOf(replace))
    val after = substring(lastIndexOf(replace) + replace.length, length)
    return before + replacing + after
}

operator fun String.times(num: Int): String {
    var str = ""
    for(i in 0..num-1)
        str += this
    return str
}

fun Any.toPaddedString(paddedLength: Int) : String {
    return "${" " * (paddedLength - toString().length)}$this"
}

fun <T> T.asOptional(): Optional<T> = Optional.of(this)
operator fun <T> Optional<T>.invoke(): T = get()
val <T> Optional<T>.isEmpty: Boolean
    get() = !isPresent

/** Returns this optional if a value is present, or the optional returned by the provided function if empty. */
fun <T> Optional<T>.orElseMaybe(opt: () -> Optional<T>): Optional<T> = if(this.isPresent) this else opt()
fun <T> Optional<T>.orElseMaybe(opt: Optional<T>): Optional<T> = if(this.isPresent) this else opt

fun Runtime.usedMemory(): Long = (totalMemory() - freeMemory())

fun Long.square(): Long = this * this
fun Int.square(): Long = this.toLong().square()
fun Short.square(): Int = this * this
fun Byte.square(): Int = this * this

fun Double.square(): Double = this * this
fun Float.square(): Float = this * this

fun Long.toBinaryString(): String = java.lang.Long.toBinaryString(this)

fun String.toLong(base: Int): Long = java.lang.Long.parseLong(this, base)
fun String.toFloat(base: Int): Float = java.lang.Float.intBitsToFloat(java.lang.Long.parseLong(this, base).toInt())

fun setHeadless() = System.setProperty("java.awt.headless", "true")

fun <T: Enum<*>> KClass<T>.isValue(name: String): Boolean = java.enumConstants.map { enum -> enum.name }.any { enumName -> enumName.toUpperCase() == name.toUpperCase()}

fun <T> T.toString(toString: (T) -> String): String = toString.invoke(this)

fun <T> Iterable<T>.firstOrEmpty(predicate: (T) -> Boolean = { true }): Optional<T> = firstOrNull(predicate)?.asOptional() ?: Optional.empty()

fun Throwable.exportStackTrace(): String {
    val baos = ByteArrayOutputStream()
    printStackTrace(PrintStream(baos, true, Charsets.UTF_8.name()))
    return String(baos.toByteArray(), Charsets.UTF_8)
}

fun <T: Any> KClass<T>.build(): ClassBuilder<T> = ClassBuilder(this)

fun <T: Any> KClass<T>.build(begin: ClassBuilder<T>.() -> Unit): ClassBuilder<T> {
    val builder = ClassBuilder(this)
    builder.begin()
    return builder
}

/** Time an action in ms */
fun time(action: () -> Unit): Long {
    val start = System.currentTimeMillis()
    action()
    val stop = System.currentTimeMillis()
    return stop - start
}

fun String.isRegex(): Boolean {
    try {
        toRegex()
        return true
    }
    catch(notValid: PatternSyntaxException) {}

    return false
}

fun Int.plural(singular: String, plural: String = "${singular}s") = if(this == 1 || this == -1) "$this $singular" else "$this $plural"
fun Long.plural(singular: String, plural: String = "${singular}s") = if(this == 1L || this == -1L) "$this $singular" else "$this $plural"

inline fun <reified T: Any> make(vararg args: Any, init: T.() -> Unit): T {
    var constructor = T::class.primaryConstructor ?: T::class.constructors.firstOrEmpty { con -> con.parameters.size == args.size && con.parameters.equalsBy { index, param -> param.type.canAssign(args[index]::class) } }.orElseThrow { IllegalArgumentException("${T::class.qualifiedName} has no constructors with ${args.size} parameters!") }
    if(constructor.parameters.isNotEmpty())
        constructor = T::class.constructors.firstOrEmpty { con -> con.parameters.size == args.size && con.parameters.equalsBy { index, param -> param.type.canAssign(args[index]::class) } }.orElseThrow { IllegalArgumentException("${T::class.qualifiedName} has no constructors with ${args.size} parameters!") }
    val t = constructor.call(*args)
    t.init()
    return t
}

fun KType.canAssign(clazz: KClass<*>): Boolean = this.classifier is KClass<*> && (this.classifier as KClass<*>).isSuperclassOf(clazz)