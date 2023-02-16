package com.thermondo.domain.note

import com.thermondo.domain.model.ChangedAt
import com.thermondo.domain.model.CreatedAt
import com.thermondo.domain.model.Id
import com.thermondo.domain.user.Password
import com.thermondo.domain.user.User
import com.thermondo.domain.user.UserName
import org.junit.Test
import kotlin.test.*

class NoteTests {

    @Test
    fun fromAuthor_minimumValidArguments_returnsNote() {

        val expectedAuthor = User.new(UserName("user1"), Password("seCreT123"))
        val expectedTitle = Title("title")
        val note = Note.fromAuthor(expectedAuthor).withTitle(expectedTitle).create()

        assertNotNull(note.id)
        assertNotNull(note.createdAt)
        assertNotNull(note.changedAt)
        assertNull(note.body)
        assertTrue(note.tags.isEmpty())

        assertEquals(expectedTitle, note.title)
        assertEquals(expectedAuthor, note.author)
        assertEquals(NoteType.PRIVATE, note.noteType)
    }

    @Test
    fun new_completeValidArguments_returnsNote() {

        val expectedId = Id.generate()
        val expectedCreatedAt = CreatedAt.now()
        val expectedChangedAt = ChangedAt.now()
        val expectedTitle = Title("My note title")
        val expectedBody = Body("An interesting body of a note")
        val expectedTags = listOf(Tag("interesting"), Tag("note"))
        val expectedNoteType = NoteType.PUBLIC

        val expectedAuthor = User.new(UserName("user1"), Password("seCreT123"))

        val note = Note.new()
            .withId(expectedId)
            .withCreatedAt(expectedCreatedAt)
            .withChangedAt(expectedChangedAt)
            .withTitle(expectedTitle)
            .withBody(expectedBody)
            .withTags(expectedTags)
            .withAuthor(expectedAuthor)
            .withNoteType(expectedNoteType)
            .create()

        assertEquals(expectedId, note.id)
        assertEquals(expectedCreatedAt, note.createdAt)
        assertEquals(expectedChangedAt, note.changedAt)
        assertEquals(expectedTitle, note.title)
        assertEquals(expectedBody, note.body)
        assertEquals(expectedTags, note.tags)
        assertEquals(expectedAuthor, note.author)
        assertEquals(expectedNoteType, note.noteType)
    }

    @Test
    fun new_withoutAuthor_throwsException() {
        assertFailsWith<Note.NoteBuilder.NoAuthorException> {
            Note.new().withTitle(Title("title")).create()
        }
    }

    @Test
    fun new_withoutTitle_throwsException() {
        assertFailsWith<Note.NoteBuilder.NoTitleException> {
            Note.new().create()
        }
    }
}
