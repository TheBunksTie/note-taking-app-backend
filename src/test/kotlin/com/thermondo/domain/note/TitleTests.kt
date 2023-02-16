package com.thermondo.domain.note

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TitleTests {

    @Test
    fun new_emptyTitle_throwsException() {
        assertFailsWith<Title.NoEmptyTitleException> { Title("") }
    }

    @Test
    fun new_blankTitle_throwsException() {
        assertFailsWith<Title.NoEmptyTitleException> { Title("   ") }
    }

    @Test
    fun new_tooLongTitle_throwsException() {
        assertFailsWith<Title.TitleTooLongException> { Title("a very long title, that should lead to an exception") }
    }

    @Test
    fun new_validTitle_returnsTitle() {
        val expectedTitle = "a perfectly fitting title"

        val title = Title(expectedTitle)
        assertEquals(expectedTitle, title.value)
    }
}
