package org.abimon.visi.lang

import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

/** While called [ClassBuilder], it is also capable of building the arguments to any function */
open class ClassBuilder<T: Any>(val constructor: KFunction<T>) {
    private val parameters = HashMap<KParameter, Optional<Any>>()

    constructor(clazz: KClass<T>) : this(clazz.constructors.first())

    init {
        parameters.putAll(constructor.parameters.map { param -> Pair(param, Optional.empty<Any>()) })
    }

    fun put(parameterName: String, value: Any) = set(parameterName, value)

    operator fun set(parameterName: String, value: Any) {
        parameters.keys.firstOrEmpty { parameter -> parameter.name ?: "" == parameterName }.ifPresent { parameter ->
            if(parameter.type.classifier == value::class) {
                parameters[parameter] = value.asOptional()
            }
            else
                throw IllegalArgumentException("$value is an invalid value for $parameterName (Expected ${parameter.type.classifier}, was given ${value::class})")
        }
    }

    fun build(): T {
        if(parameters.filter { (key) -> !key.isOptional }.any { (_, value) -> value.isEmpty })
            throw IllegalStateException("Not all required parameters have associated values! Still missing ${parameters.keys.filter { key -> !key.isOptional }.joinToString()}")
        return constructor.callBy(parameters.filterValues(Optional<Any>::isPresent).mapValues { (_, value) -> value() })
    }

    operator fun invoke(): T = build()
}