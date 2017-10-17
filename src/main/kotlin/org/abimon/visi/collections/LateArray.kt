package org.abimon.visi.collections

class LateArray<T : Any> constructor(private val backing: Array<T?>, override val size: Int): List<T> {
    companion object {
        inline operator fun <reified T : Any> invoke(size: Int): LateArray<T> = LateArray(Array(size, { null as T? }), size)
    }

    override fun contains(element: T): Boolean = element in backing

    override fun containsAll(elements: Collection<T>): Boolean = elements.all { t -> t in backing }

    override fun get(index: Int): T = backing[index] ?: throw UninitializedPropertyAccessException()

    override fun indexOf(element: T): Int = backing.indexOf(element)

    override fun isEmpty(): Boolean = size == 0

    override fun iterator(): Iterator<T> =
            object : Iterator<T> {
                var index: Int = 0
                override fun hasNext(): Boolean = index < size

                override fun next(): T = this@LateArray[index++]
            }

    override fun lastIndexOf(element: T): Int = backing.lastIndexOf(element)

    override fun listIterator(): ListIterator<T> = listIterator(0)

    override fun listIterator(index: Int): ListIterator<T> =
            object : ListIterator<T> {
                var currIndex: Int = index

                override fun previousIndex(): Int = currIndex - 1

                override fun hasNext(): Boolean = currIndex < size

                override fun hasPrevious(): Boolean = currIndex > 0

                override fun next(): T = this@LateArray[currIndex++]

                override fun previous(): T = this@LateArray[currIndex--]

                override fun nextIndex(): Int = currIndex + 1
            }

    override fun subList(fromIndex: Int, toIndex: Int): List<T> = backing.copyOfRange(fromIndex, toIndex).map { t -> t ?: throw UninitializedPropertyAccessException() }

    operator fun set(index: Int, element: T): Unit = backing.set(index, element)

    fun finalise(): Array<T> {
        if(backing.any { t -> t == null })
            throw UninitializedPropertyAccessException()

        return backing.requireNoNulls()
    }
}