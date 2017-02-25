package org.abimon.visi.collections

import java.util.*

fun <T, E: MutableCollection<T>> E.addAllReturning(collection: Collection<T>): E {
    addAll(collection)
    return this
}

fun <K, V> HashMap<K, V>.flush(): HashMap<K, V> {
    val copy = HashMap(this)
    this.clear()
    return copy
}

fun <T> List<T>.random(): T = this[Random().nextInt(size)]