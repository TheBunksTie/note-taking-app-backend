package com.thermondo.domain.model

import com.thermondo.domain.common.DomainException
import java.util.*

/**
 * Unique identifier for domain entities, based on Java's UUID
 */
class Id private constructor(value: UUID) : SimpleValueObject<UUID>(value) {

    companion object FactoryMethods {

        fun fromString(stringValue: String) : Id {
            try {
                val uuidValue = UUID.fromString(stringValue)
                return fromUUID(uuidValue)
            } catch (e: Exception) {
                throw InvalidIdException(stringValue)
            }
        }

        fun fromUUID(uuidValue: UUID) : Id {
            return Id(uuidValue)
        }

        fun generate() : Id {
            return Id(UUID.randomUUID())
        }
    }

    /**
     * DomainException denoting an invalid argument was passed into the creation of an [Id]
     */
    class InvalidIdException(invalidIdValue: String) : DomainException("Invalid id value '$invalidIdValue'") {

    }
}
