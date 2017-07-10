package org.abimon.visi.lang

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSuperclassOf

fun <T: Enum<*>> KClass<T>.isValue(name: String): Boolean = java.enumConstants.map { enum -> enum.name }.any { enumName -> enumName.toUpperCase() == name.toUpperCase()}

fun <T: Any> KClass<T>.build(): ClassBuilder<T> = ClassBuilder(this)

fun <T: Any> KClass<T>.build(begin: ClassBuilder<T>.() -> Unit): ClassBuilder<T> {
    val builder = ClassBuilder(this)
    builder.begin()
    return builder
}

fun KType.canAssign(clazz: KClass<*>): Boolean = this.classifier is KClass<*> && (this.classifier as KClass<*>).isSuperclassOf(clazz)