package com.thermondo.domain.model

import java.time.LocalDateTime

/**
 * Value object representing the timestamp, a domain entity was modified last
 */
class ChangedAt private constructor(changedAt: LocalDateTime) : SimpleValueObject<LocalDateTime>(changedAt) {

    companion object FactoryMethods {
        fun now() : ChangedAt {
            return ChangedAt(LocalDateTime.now())
        }

        fun fromDate(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int) : ChangedAt {
            return ChangedAt(LocalDateTime.of(year, month, day, hour, minute, second))
        }

        fun fromString(changedAtString: String) : ChangedAt {
            return ChangedAt(LocalDateTime.parse(changedAtString))
        }
    }
}
