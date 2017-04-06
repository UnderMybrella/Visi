package org.abimon.visi.collections

import java.util.*

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

fun <T> Collection<T>.joinToPrefixedString(separator: String, elementPrefix: String = "", elementSuffix: String = "", transform: T.() -> String = { this.toString() }) = joinToString(separator) { element -> "$elementPrefix${element.transform()}$elementSuffix"}