package com.thermondo.domain.note

import com.thermondo.domain.common.DomainException
import com.thermondo.domain.model.SimpleValueObject

/**
 * Value object representing a tag of a [Note]
 * @param tag the actual string tag, may not be empty or (containing) blank and not longer than [maxTagLength]
 */
class Tag(tag: String) : SimpleValueObject<String>(tag) {

    private val maxTagLength = 15

    init {
        if (tag.isBlank() || tag.isEmpty())
            throw NoEmptyTagException()

        if (tag.length > maxTagLength)
            throw TagTooLongException(maxTagLength)

        if (tag.contains(" "))
            throw NoBlanksInTagException()
    }

    class NoEmptyTagException : DomainException("Tag must not be empty")

    class TagTooLongException(maxTagLength: Int) : DomainException("Tag must be shorter than $maxTagLength characters")

    class NoBlanksInTagException : DomainException("Tag must not contain any blank characters")
}
