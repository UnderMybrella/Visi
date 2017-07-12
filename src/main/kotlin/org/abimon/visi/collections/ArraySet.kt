package org.abimon.visi.collections

import java.util.*

class ArraySet<T>(): ArrayList<T>(), MutableSet<T> {
    constructor(collection: Collection<T>): this() { addAll(collection) }
    constructor(array: Array<T>): this() { addAll(array) }
    constructor(iterable: Iterable<T>): this() { addAll(iterable) }
    constructor(sequence: Sequence<T>): this() { addAll(sequence) }

    override fun spliterator(): Spliterator<T> = super<ArrayList>.spliterator()

    override fun add(element: T): Boolean {
        if(contains(element))
            return false
        return super.add(element)
    }

    companion object {
        fun <T> arraySetOf(vararg vars: T): ArraySet<out T> = ArraySet(vars)
    }
}