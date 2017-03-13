package org.abimon.visi.collections

import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class Pool<T>(val poolSize: Int = -1) {
    private val poolables: MutableList<Poolable<T>> = ArrayList()

    /**
     * Get the first available [Poolable] instance, or null if none are free
     */
    fun get(): Poolable<T>? {
        synchronized(poolables) {
            return poolables.firstOrNull(Poolable<T>::isFree)
        }
    }
    /**
     * Waits for a [Poolable] instance to become free, and then returns it. Throws a [TimeOutException]
     */
    fun getOrWait(wait: Long, unit: TimeUnit): Poolable<T> {
        var passedMilli = 0L
        val waitingMilli = unit.toMillis(wait)
        while(passedMilli < waitingMilli) {
            val res = get()
            if(res != null)
                return res
            Thread.sleep(1000)
            passedMilli += 1000
        }
        throw TimeoutException("No Poolable instances free, waited ${unit.convert(passedMilli, TimeUnit.MILLISECONDS)} ${unit.name.capitalize()}")
    }

    /**
     * Get the first available [Poolable] instance which matches [matches], or null if none of the instances are free that match
     */
    fun getWhich(matches: (T) -> Boolean): Poolable<T>? {
        synchronized(poolables) {
            return poolables.filter { poolable -> matches(poolable()) }.firstOrNull(Poolable<T>::isFree)
        }
    }
    /**
     * Waits for a [Poolable] instance to become free, and then returns it. Will hang until one becomes available
     */
    fun getOrWaitUntil(wait: Long, unit: TimeUnit, matches: (T) -> Boolean): Poolable<T> {
        var passedMilli = 0L
        val waitingMilli = unit.toMillis(wait)
        while(passedMilli < waitingMilli) {
            val res = getWhich(matches)
            if(res != null)
                return res
            Thread.sleep(1000)
            passedMilli += 1000
        }
        throw TimeoutException("No Poolable instances free, waited ${unit.convert(passedMilli, TimeUnit.MILLISECONDS)} ${unit.name.capitalize()}")
    }

    fun getFirst(sort: (T, T) -> Int): Poolable<T>? {
        synchronized(poolables) {
            return poolables.filter(Poolable<T>::isFree).sortedWith(Comparator { one, two -> sort(one(), two())}).firstOrNull()
        }
    }
    /**
     * Waits for a [Poolable] instance to become free, and then returns it. Will hang until one becomes available
     */
    fun getFirstOrWaitUntil(wait: Long, unit: TimeUnit, sort: (T, T) -> Int): Poolable<T> {
        var passedMilli = 0L
        val waitingMilli = unit.toMillis(wait)
        while(passedMilli < waitingMilli) {
            val res = getFirst(sort)
            if(res != null)
                return res
            Thread.sleep(1000)
            passedMilli += 1000
        }
        throw TimeoutException("No Poolable instances free, waited ${unit.convert(passedMilli, TimeUnit.MILLISECONDS)} ${unit.name.capitalize()}")
    }

    /**
     * Gets the first available [Poolable] instance, or adds [add] to the pool if there is space. If there is no space and none are free, returns null
     */
    fun getOrAdd(add: () -> Poolable<T>): Poolable<T>? {
        synchronized(poolables) {
            if(poolables.size >= poolSize)
                return get()

            val poolable = poolables.firstOrNull(Poolable<T>::isFree)
            if(poolable == null) {
                val newPoolable = add()
                poolables.add(newPoolable)
                return newPoolable
            }

            return poolable
        }
    }

    /**
     * Gets the first available [Poolable] instance, or adds [add] to the pool if there is space. If there is no space and none are free, wait.
     */
    fun getOrAddOrWait(add: () -> Poolable<T>, wait: Long, unit: TimeUnit): Poolable<T>? {
        synchronized(poolables) {
            if(poolables.size >= poolSize)
                return getOrWait(wait, unit)

            val poolable = poolables.firstOrNull(Poolable<T>::isFree)
            if(poolable == null) {
                val newPoolable = add()
                poolables.add(newPoolable)
                return newPoolable
            }

            return poolable
        }
    }


    fun getFree(): List<Poolable<T>> = poolables.filter(Poolable<T>::isFree)

    fun retire(poolable: Poolable<T>): Boolean = poolables.remove(poolable)
    fun add(poolable: Poolable<T>): Boolean = if(poolables.size >= poolSize) false else poolables.add(poolable)
}

interface Poolable<out T> {
    fun get(): T
    fun isFree(): Boolean

    operator fun invoke(): T = get()
}

class PoolableObject<out T>(val obj: T) : Poolable<T> {
    var inUse = false

    override fun get(): T = synchronized(obj as Any) { obj }

    override fun isFree(): Boolean = !inUse

    fun use(run: (T) -> Unit) {
        inUse = true

        try {
            synchronized(obj as Any) {
                run(obj)
            }
        }
        finally {
            inUse = false
        }
    }
}