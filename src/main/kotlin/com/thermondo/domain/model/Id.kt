package com.thermondo.domain.model

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
}
