package com.thermondo.domain.model

import com.thermondo.domain.common.DomainException
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class IdTests {

    @Test
    fun fromString_emptyString_throwsException() {
        assertFailsWith<DomainException> { Id.fromString("") }
    }

    @Test
    fun fromString_noUUIDString_throwsException() {
        assertFailsWith<DomainException> { Id.fromString("<no uuid string>") }
    }

    @Test
    fun fromString_validUUIDString_returnsId() {
        val validUUIDString = UUID.randomUUID().toString()

        val id = Id.fromString(validUUIDString)
        assertNotNull(id)
    }

    @Test
    fun fromUUID_validUUID_returnsId() {
        val validUUID = UUID.randomUUID()

        val id = Id.fromUUID(validUUID)
        assertNotNull(id)
    }

    @Test
    fun equals_differentUUIDs_returnsFalse() {
        val id1 = Id.generate()
        val id2 = Id.generate()

        assertNotEquals(id1, id2)
    }

    @Test
    fun equals_sameUUID_returnTrue() {
        val validUUID = UUID.randomUUID()

        val id1 = Id.fromUUID(validUUID)
        val id2 = Id.fromUUID(validUUID)
        assertEquals(id1, id2)
    }
}
