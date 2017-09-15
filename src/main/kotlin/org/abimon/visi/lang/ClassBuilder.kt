package org.abimon.visi.lang

import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

/** While called [ClassBuilder], it is also capable of building the arguments to any function */
open class ClassBuilder<T: Any>(val constructor: KFunction<T>) {
    private val parameters = HashMap<KParameter, Any?>()

    constructor(clazz: KClass<T>) : this(clazz.constructors.first())

    init {
        parameters.putAll(constructor.parameters.map { param -> param to null })
    }

    fun put(parameterName: String, value: Any) = set(parameterName, value)

    operator fun set(parameterName: String, value: Any) {
        val parameter = parameters.keys.firstOrNull { parameter -> parameter.name ?: "" == parameterName } ?: return
        if (parameter.type.classifier == value::class) {
            parameters[parameter] = value
        } else
            throw IllegalArgumentException("$value is an invalid value for $parameterName (Expected ${parameter.type.classifier}, was given ${value::class})")
    }

    fun build(): T {
        if(parameters.filter { (key) -> !key.isOptional && !key.type.isMarkedNullable }.any { (_, value) -> value == null })
            throw IllegalStateException("Not all required parameters have associated values! Still missing ${parameters.keys.filter { key -> !key.isOptional }.joinToString()}")
        return constructor.callBy(parameters.filter { (key, value) -> !(!key.type.isMarkedNullable && value == null) })
    }

    operator fun invoke(): T = build()
}