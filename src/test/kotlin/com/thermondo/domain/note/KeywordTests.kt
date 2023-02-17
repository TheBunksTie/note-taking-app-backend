package com.thermondo.domain.note

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class KeywordTests {

    @Test
    fun new_emptyKeyword_throwsException() {
        assertFailsWith<Keyword.NoEmptyKeywordException> { Keyword("") }
    }

    @Test
    fun new_blankKeyword_throwsException() {
        assertFailsWith<Keyword.NoEmptyKeywordException> { Keyword("   ") }
    }

    @Test
    fun new_tooLongKeyword_throwsException() {
        assertFailsWith<Keyword.KeywordTooLongException> { Keyword("VeryLongKeywordThatIsNotValid") }
    }

    @Test
    fun new_containingBlanks_throwsException() {
        assertFailsWith<Keyword.NoBlanksInKeywordException> { Keyword("Keyword blanks") }
    }

    @Test
    fun new_validKeyword_returnsKeyword() {
        val expectedKeyword = "ValidKeyword"

        val keyword = Keyword(expectedKeyword)
        assertEquals(expectedKeyword, keyword.value)
    }
}
