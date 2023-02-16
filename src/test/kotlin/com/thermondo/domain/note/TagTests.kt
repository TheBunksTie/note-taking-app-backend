package com.thermondo.domain.note

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TagTests {

    @Test
    fun new_emptyTag_throwsException() {
        assertFailsWith<Tag.NoEmptyTagException> { Tag("") }
    }

    @Test
    fun new_blankTag_throwsException() {
        assertFailsWith<Tag.NoEmptyTagException> { Tag("   ") }
    }

    @Test
    fun new_tooLongTag_throwsException() {
        assertFailsWith<Tag.TagTooLongException> { Tag("VeryLongTagThatIsNotValid") }
    }

    @Test
    fun new_containingBlanks_throwsException() {
        assertFailsWith<Tag.NoBlanksInTagException> { Tag("Tag with blanks") }
    }

    @Test
    fun new_validTag_returnsTag() {
        val expectedTag = "ValidTag"

        val tag = Tag(expectedTag)
        assertEquals(expectedTag, tag.value)
    }
}
