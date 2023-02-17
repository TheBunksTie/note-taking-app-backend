package com.thermondo.domain.model

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

class SimpleValueObjectTests {

    @Test
    fun equals_sameObject_returnsTrue() {
        val testValueObject = TestValueObject1("value object")

        assertEquals(testValueObject, testValueObject)
    }

    @Test
    fun equals_otherObjectIsNull_returnsFalse() {
        val testValueObject = TestValueObject1("value object")

        assertFalse { testValueObject.equals(null) }
    }

    @Test
    fun equals_objectsSameValues_returnsTrue() {
        val testValueObject1 = TestValueObject1("value object")
        val testValueObject2 = TestValueObject1("value object")

        assertEquals(testValueObject1, testValueObject2)
    }

    @Test
    fun equals_objectsHaveDifferentValues_returnsFalse() {
        val testValueObject1 = TestValueObject1("value object1")
        val testValueObject2 = TestValueObject1("value object2")

        assertNotEquals(testValueObject1, testValueObject2)
    }

    @Test
    fun equals_objectsOfDifferentClasses_returnsFalse() {
        val testValueObject1 = TestValueObject1("value object")
        val testValueObject2 = TestValueObject2("value object")

        assertFalse { testValueObject1.equals(testValueObject2) }
    }

    @Test
    fun toString_returnsStringRepresentation() {
        val testValueObject1 = TestValueObject1("value object")
        val expectedStringRepresentation = "value object"

        assertEquals(expectedStringRepresentation, testValueObject1.toString())
    }

    private class TestValueObject1(value: String) : SimpleValueObject<String>(value)
    private class TestValueObject2(value: String) : SimpleValueObject<String>(value)
}
