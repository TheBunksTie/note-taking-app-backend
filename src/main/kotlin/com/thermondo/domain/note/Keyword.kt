package com.thermondo.domain.note

import com.thermondo.domain.common.DomainException
import com.thermondo.domain.model.SimpleValueObject

/**
 * Value object representing a keyword to search within a [Note]
 * @param keyword the actual string keyword, may not be empty or (containing) blank and not longer than [maxKeywordLength]
 */
class Keyword(keyword: String) : SimpleValueObject<String>(keyword) {

    private val maxKeywordLength = 15

    init {
        if (keyword.isBlank() || keyword.isEmpty())
            throw NoEmptyKeywordException()

        if (keyword.length > maxKeywordLength)
            throw KeywordTooLongException(maxKeywordLength)

        if (keyword.contains(" "))
            throw NoBlanksInKeywordException()
    }

    class NoEmptyKeywordException : DomainException("Keyword must not be empty")

    class KeywordTooLongException(maxTagLength: Int) : DomainException("Keyword must be shorter than $maxTagLength characters")

    class NoBlanksInKeywordException : DomainException("Keyword must not contain any blank characters")
}
