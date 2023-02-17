package com.thermondo.domain.model

import java.time.LocalDateTime

/**
 * Value object representing the timestamp, a domain entity was initially created
 */
class CreatedAt private constructor(createdAt: LocalDateTime) : SimpleValueObject<LocalDateTime>(createdAt) {

    companion object FactoryMethods {

        fun now(): CreatedAt {
            return CreatedAt(LocalDateTime.now())
        }

        fun fromDate(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): CreatedAt {
            return CreatedAt(LocalDateTime.of(year, month, day, hour, minute, second))
        }

        fun fromString(createdAtString: String): CreatedAt {
            return CreatedAt(LocalDateTime.parse(createdAtString))
        }
    }
}
