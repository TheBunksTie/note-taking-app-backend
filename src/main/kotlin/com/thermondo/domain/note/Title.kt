package com.thermondo.domain.note

import com.thermondo.domain.common.DomainException
import com.thermondo.domain.model.SimpleValueObject

/**
 * Value object representing the title of a [Note]
 * @param title the actual string title, may not be empty or blank and not longer than [maxTitleLength]
 */
class Title constructor(title: String): SimpleValueObject<String>(title) {

    private val maxTitleLength = 50

    init {
        if (title.isBlank() || title.isEmpty())
            throw NoEmptyTitleException()

        if (title.length > maxTitleLength)
            throw TitleTooLongException(maxTitleLength)
    }

    class NoEmptyTitleException : DomainException("Title must not be empty") {
    }

    class TitleTooLongException(maxTitleLength: Int) : DomainException("Title mus be shorter than $maxTitleLength characters") {
    }
}
