package com.thermondo.domain.note

import com.thermondo.domain.common.DomainException
import com.thermondo.domain.model.SimpleValueObject

/**
 * Value object representing the body of a [Note]
 * @param body the actual string body, may be empty or blank but not longer than [maxBodyLength]
 */
class Body(body: String) : SimpleValueObject<String>(body) {
    private val maxBodyLength = 100

    init {
        if (body.length > maxBodyLength)
            throw BodyTooLongException(maxBodyLength)
    }

    class BodyTooLongException(maxBodyLength: Int) : DomainException("Body mus be shorter than $maxBodyLength characters") {
    }
}
