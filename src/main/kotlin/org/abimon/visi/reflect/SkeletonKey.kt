package org.abimon.visi.reflect

import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

class SkeletonKey<T: Any>(val klass: KClass<out T>, val door: T?) {
    constructor(door: T): this(door::class, door)
    val properties = klass.java.declaredFields.map { field -> field.name to field.apply { isAccessible = true } }.toMap()
    val methods = klass.java.declaredMethods.map { method -> method.apply { isAccessible = true} }.groupBy { method -> method.name }

    operator fun get(variable: String): Any? = (properties[variable] ?: throw IllegalArgumentException("No property with the name $variable on $klass")).get(door)
    operator fun set(variable: String, value: Any?): Any? = (properties[variable] ?: throw IllegalArgumentException("No property with the name $variable on $klass")).set(door, value)
    operator fun invoke(name: String, vararg params: Any?): Any? {
        val method = (methods[name] ?: throw IllegalArgumentException("No function with the name $name on $klass")).firstOrNull { method ->
            method.parameterCount == params.size && method.parameterTypes.filterIndexed { index, parameter ->
                if(params[index] == null)
                    return@filterIndexed false
                return@filterIndexed (params[index]!!::class.java != parameter && params[index]!!::class.javaPrimitiveType != parameter && params[index]!!::class.javaObjectType != parameter)
            }.isEmpty() } ?: throw IllegalArgumentException("No function with the name $name and parameters ${params.map { (it ?: return@map "null")::class.jvmName }.joinToString()} on $klass")

        return method(door, *params)
    }
}