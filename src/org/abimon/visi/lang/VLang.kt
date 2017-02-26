package org.abimon.visi.lang

import java.util.*

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

fun Runtime.usedMemory(): Long = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())

fun Long.square(): Long = this * this
fun Int.square(): Long = this.toLong().square()
fun Short.square(): Int = this * this
fun Byte.square(): Int = this * this

fun Double.square(): Double = this * this
fun Float.square(): Float = this * this

fun ByteArray.toArrayString(): String = Arrays.toString(this)
fun BooleanArray.toArrayString(): String = Arrays.toString(this)

fun Array<*>.toArrayString(): String = Arrays.toString(this)

fun Long.toBinaryString(): String = java.lang.Long.toBinaryString(this)

fun String.toLong(base: Int): Long = java.lang.Long.parseLong(this, base)
fun String.toFloat(base: Int): Float = java.lang.Float.intBitsToFloat(java.lang.Long.parseLong(this, base).toInt())

fun <T> Optional<T>.populateWithEmpty(int: Int): Optional<T> = Optional.empty()

fun setHeadless() = System.setProperty("java.awt.headless", "true")

fun <T, U, V> Map<Pair<T, U>, V>.containsFirstPair(t: T): Boolean = keys.any { pair -> pair.first == t }
fun <T, U, V> Map<Pair<T, U>, V>.getFirstOfPair(t: T): V? {
    return get(keys.firstOrNull { pair -> pair.first == t } ?: return null)
}
fun <T, U, V> Map<Pair<T, U>, V>.getSecondPair(t: T): U? {
    return (keys.firstOrNull { pair -> pair.first == t } ?: return null).second
}

fun <T, U, V> Map<Pair<T, U>, V>.containsSecondPair(u: U): Boolean = keys.any { pair -> pair.second == u }
fun <T, U, V> Map<Pair<T, U>, V>.getSecondOfPair(u: U): V? {
    return get(keys.firstOrNull { pair -> pair.second == u } ?: return null)
}
fun <T, U, V> Map<Pair<T, U>, V>.getFirstPair(u: U): T? {
    return (keys.firstOrNull { pair -> pair.second == u } ?: return null).first
}

fun <T, V> Map<Pair<T, T>, V>.contains(t: T): Boolean = keys.any { pair -> pair.first == t || pair.second == t}
fun <T, V> Map<Pair<T, T>, V>.getFirst(t: T): V? {
    return get(keys.firstOrNull { pair -> pair.first == t || pair.second == t } ?: return null)
}
fun <T, V> Map<Pair<T, T>, V>.getPair(t: T): Pair<T, T>? {
    return (keys.firstOrNull { pair -> pair.first == t || pair.second == t } ?: return null)
}
fun <T, V> Map<Pair<T, T>, V>.getOther(t: T): T? {
    return (keys.firstOrNull { pair -> pair.first == t } ?: return (keys.firstOrNull { pair -> pair.second == t } ?: return null).first).second
}

fun <T, V> Map<T, V>.getForValue(v: V): T? = keys.filter { key -> get(key) == v }.firstOrNull()

fun <T> List<T>.shuffle(): List<T> {
    val list = ArrayList<T>()
    val copyList = ArrayList<T>(this)
    val rng = Random()

    while(copyList.size > 0)
        list.add(copyList.removeAt(rng.nextInt(copyList.size)))

    return list
}