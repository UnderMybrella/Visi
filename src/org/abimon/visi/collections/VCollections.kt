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