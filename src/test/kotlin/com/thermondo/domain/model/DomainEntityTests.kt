package com.thermondo.domain.model

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

class DomainEntityTests {

    @Test
    fun equals_sameObject_returnsTrue() {
        val testDomainEntity = TestDomainEntity1(Id.generate(), CreatedAt.now(), ChangedAt.now())

        assertEquals(testDomainEntity, testDomainEntity)
    }

    @Test
    fun equals_otherObjectIsNull_returnsFalse() {
        val testDomainEntity = TestDomainEntity1(Id.generate(), CreatedAt.now(), ChangedAt.now())

        assertFalse { testDomainEntity.equals(null) }
    }

    @Test
    fun equals_objectsHaveSameIds_returnsTrue() {
        val id = Id.generate()

        val testDomainEntity1 = TestDomainEntity1(id, CreatedAt.now(), ChangedAt.now())
        val testDomainEntity2 = TestDomainEntity1(id, CreatedAt.now(), ChangedAt.now())

        assertEquals(testDomainEntity1, testDomainEntity2)
    }

    @Test
    fun equals_objectsHaveDifferentIds_returnsFalse() {
        val id1 = Id.generate()
        val id2 = Id.generate()

        val testDomainEntity1 = TestDomainEntity1(id1, CreatedAt.now(), ChangedAt.now())
        val testDomainEntity2 = TestDomainEntity1(id2, CreatedAt.now(), ChangedAt.now())

        assertNotEquals(testDomainEntity1, testDomainEntity2)
    }

    @Test
    fun equals_objectsOfDifferentClasses_returnsFalse() {
        val id1 = Id.generate()
        val id2 = Id.generate()

        val testDomainEntity1 = TestDomainEntity1(id1, CreatedAt.now(), ChangedAt.now())
        val testDomainEntity2 = TestDomainEntity2(id2, CreatedAt.now(), ChangedAt.now())

        assertFalse { testDomainEntity1.equals(testDomainEntity2) }
    }

    @Test
    fun toString_returnsStringRepresentation() {
        val id = Id.generate()
        val testDomainEntity1 = TestDomainEntity1(id, CreatedAt.now(), ChangedAt.now())
        val expectedStringRepresentation = "TestDomainEntity1 [id='$id']"

        assertEquals(expectedStringRepresentation, testDomainEntity1.toString())
    }

    private class TestDomainEntity1(id: Id, createdAt: CreatedAt, changedAt: ChangedAt) : DomainEntity(id, createdAt, changedAt) {
    }
    private class TestDomainEntity2(id: Id, createdAt: CreatedAt, changedAt: ChangedAt) : DomainEntity(id, createdAt, changedAt) {
    }
}
