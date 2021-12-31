package com.afzaln.besttvlauncher.core

import kotlin.reflect.KProperty

inline operator fun <reified T : Any> BaseLocator.getValue(
    thisRef: Any?,
    property: KProperty<*>
): T = provide()

inline fun <reified T : Any> BaseLocator.provide(): T = provide(T::class.java)

interface BaseLocator {
    val locatorMap: MutableMap<Class<*>, Any>

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> provide(clz: Class<T>): T = locatorMap[clz] as T?
        ?: synchronized(locatorMap) { locatorMap.getOrPut(clz) { create(clz) } } as T

    fun <T : Any> create(clz: Class<T>): T
}