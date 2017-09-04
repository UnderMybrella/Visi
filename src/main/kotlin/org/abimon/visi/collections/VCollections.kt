package org.abimon.visi.collections

import java.util.*


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

fun <T> List<T>.random(rng: Random = Random()): T = get(rng.nextInt(size))
fun <T> Array<T>.random(rng: Random = Random()): T = get(rng.nextInt(size))

fun <T> MutableList<T>.remove(predicate: (T) -> Boolean): T? {
    val index = indexOfFirst(predicate)
    if(index == -1)
        return null
    return removeAt(index)
}
fun <T> MutableList<T>.tryRemove(predicate: (T) -> Boolean): Optional<T> = Optional.ofNullable(remove(predicate))


fun <T, E: MutableCollection<T>> E.addAllReturning(collection: Collection<T>): E {
    addAll(collection)
    return this
}

fun <T, E: MutableCollection<T>> E.addAll(num: Int, adding: (Int) -> T): Boolean {
    var added = false

    for(i in 0 until num)
        added = add(adding(i))
    return added
}

fun <K, V> HashMap<K, V>.flush(): HashMap<K, V> {
    val copy = HashMap(this)
    this.clear()
    return copy
}

fun <T> List<T>.random(): T = this[Random().nextInt(size)]

fun <T> Iterable<T>.coerceAtMost(size: Int): List<T> {
    val list = ArrayList<T>()
    for(element in this) {
        list.add(element)
        if(list.size >= size)
            break
    }

    return list
}

fun <T> List<T>.equalsBy(equalCheck: (Int, T) -> Boolean): Boolean {
    return indices.all { equalCheck(it, get(it)) }
}

inline fun <reified T> Iterable<T>.segment(sizes: Int): List<Array<T>> {
    val list = ArrayList<Array<T>>()
    val segment = ArrayList<T>()
    for(element in this) {
        segment.add(element)
        if(segment.size >= sizes) {
            list.add(segment.toTypedArray())
            segment.clear()
        }
    }
    return list
}

fun <T> Iterable<T>.pass(passing: (T, T) -> T): T {
    val list = this.toList()
    var t: T = list.first()
    for(i in (1 until list.size))
        t = passing(t, list[i])
    return t
}

fun <T> Collection<T>.joinToPrefixedString(separator: String, elementPrefix: String = "", elementSuffix: String = "", transform: T.() -> String = { this.toString() }) = joinToString(separator) { element -> "$elementPrefix${element.transform()}$elementSuffix"}

fun ByteArray.toArrayString(): String = Arrays.toString(this)
fun BooleanArray.toArrayString(): String = Arrays.toString(this)

fun Array<*>.toArrayString(): String = Arrays.toString(this)

fun IntArray.copyFrom(index: Int): IntArray = copyOfRange(index, size)
fun <T> Array<T>.copyFrom(index: Int): Array<T> = copyOfRange(index, size)

fun <T> List<T>.toSequentialString(separator: String = ", ", finalSeparator: String = ", and ", transform: T.() -> String = { this.toString() }): String {
    if(size == 0)
        return ""
    else if(size == 1)
        return this[0].transform()
    else if(size == 2)
        return this[0].transform() + finalSeparator + this[1].transform()

    var str = ""
    for(i in 0 until size - 2)
        str += this[i].transform() + separator
    str += this[size - 2].transform() + finalSeparator
    str += this[size - 1].transform()
    return str
}
fun <T> Array<T>.toSequentialString(separator: String = ", ", finalSeparator: String = ", and ", transform: T.() -> String = { this.toString() }): String {
    if(size == 0)
        return ""
    else if(size == 1)
        return this[0].transform()
    else if(size == 2)
        return this[0].transform() + finalSeparator + this[1].transform()

    var str = ""
    for(i in 0 until size - 2)
        str += this[i].transform() + separator
    str += this[size - 2].transform() + finalSeparator
    str += this[size - 1].transform()
    return str
}

fun byteArrayOf(vararg bytes: Int): ByteArray = bytes.map { it.toByte() }.toByteArray()

infix fun ByteArray.asBase(base: Int): String = this.joinToString(" ") { byte ->
    when(base) {
        2 -> "0b${byte.toString(2)}"
        16 -> "0x${byte.toString(16)}"
        else -> byte.toString(base)
    }
}

infix fun IntArray.asBase(base: Int): String = this.joinToString(" ") { byte ->
    when(base) {
        2 -> "0b${byte.toString(2)}"
        16 -> "0x${byte.toString(16)}"
        else -> byte.toString(base)
    }
}

operator fun <K, V> Map<K, V>.get(v: V): K? = getByValue(v)
fun <K, V> Map<K, V>.getByValue(v: V): K? = entries.firstOrNull { (_, value) -> value == v }?.key

operator fun <T> List<T>.get(index: Int, default: T): T = if(index < size) this[index] else default