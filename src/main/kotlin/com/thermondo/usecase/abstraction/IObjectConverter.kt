package com.thermondo.usecase.abstraction

/**
 * Marker interface for any object conversion
 * @param T1 conversion type operand 1
 * @param T2 conversion type operand 2
 */
interface IObjectConverter<T1, T2> {
    fun createFromT1(source: T1) : T2
    fun createFromT2(source: T2) : T1
}

