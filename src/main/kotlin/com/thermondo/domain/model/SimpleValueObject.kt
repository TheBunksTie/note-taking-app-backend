package com.thermondo.domain.model

import com.thermondo.domain.abstraction.IValueObject

/**
 * Abstract base class for value objects containing only one [value] of one type
 * @param T type of the value that the value object is holding
 * @property value value that the value object is holding
 */
abstract class SimpleValueObject<T> protected constructor(private val value: T) : IValueObject {

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        if (value == null)
            return String()
        return value.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true

        if (other == null)
            return false

        if (this.javaClass != other.javaClass)
            return false

        val otherAsT = other as? SimpleValueObject<*> ?: return false

        return value?.equals(otherAsT.value) ?: return false
    }
}
