package com.thermondo.usecase.common

import com.thermondo.usecase.abstraction.IObjectConverter

/**
 * Base class for object conversion
 * @param T1 conversion type operand 1
 * @param T2 conversion type operand 2
 */
abstract class ObjectConverterBase<T1, T2> : IObjectConverter<T1, T2> {

    override fun createFromT1(source: T1): T2 {
        return createInstanceOfT2ByT1(source)
    }

    override fun createFromT2(source: T2): T1 {
        return createInstanceOfT1ByT2(source)
    }

    protected abstract fun createInstanceOfT2ByT1(source: T1): T2

    protected abstract fun createInstanceOfT1ByT2(source: T2): T1
}
