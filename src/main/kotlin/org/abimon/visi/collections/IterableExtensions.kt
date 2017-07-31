package org.abimon.visi.collections

fun <T> Iterable<T>.group(): Map<T, List<T>> = groupTo(HashMap())

fun <T, L: MutableMap<T, MutableList<T>>> Iterable<T>.groupTo(destination: L): L {
    for (element in this) {
        val list = destination.getOrPut(element) { ArrayList<T>() }
        list.add(element)
    }
    return destination
}