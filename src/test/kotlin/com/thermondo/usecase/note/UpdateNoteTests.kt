package com.thermondo.usecase.note

import com.thermondo.domain.model.Id
import com.thermondo.domain.note.Body
import com.thermondo.domain.note.Note
import com.thermondo.domain.note.NoteType
import com.thermondo.domain.note.Tag
import com.thermondo.domain.note.Title
import com.thermondo.domain.user.Password
import com.thermondo.domain.user.User
import com.thermondo.domain.user.UserName
import com.thermondo.usecase.common.UseCaseTestBase
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UpdateNoteTests : UseCaseTestBase() {

    @Test
    fun execute_nonExistingAuthor_returnsErrorResultModel() {
        val nonExistingAuthorId = Id.generate()
        val nonExistingNoteId = Id.generate()
        val title = "My note title"

        val requestModel = UpdateNote.RequestModel(
            nonExistingNoteId.toString(),
            title,
            "An interesting body of a note",
            listOf("interesting", "note"),
            nonExistingAuthorId.toString(),
            NoteType.PUBLIC.toString()
        )

        val updateNote = UpdateNote(noteRepository, userRepository)
        val resultModel = updateNote.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.noteId)
        assertContains(resultModel.message, title)
        assertContains(resultModel.message, "could not be found")
    }

    @Test
    fun execute_invalidAuthorId_returnsErrorResultModel() {
        val nonExistingNoteId = Id.generate()
        val invalidAuthorId = "<invalid id>"
        val title = "My note title"

        val requestModel = UpdateNote.RequestModel(
            nonExistingNoteId.toString(),
            title,
            "An interesting body of a note",
            listOf("interesting", "note"),
            invalidAuthorId,
            NoteType.PUBLIC.toString()
        )

        val updateNote = UpdateNote(noteRepository, userRepository)
        val resultModel = updateNote.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.noteId)
        assertContains(resultModel.message, title)
        assertContains(resultModel.message, "Invalid id value")
    }

    @Test
    fun execute_nonExistingNote_returnsErrorResultModel() {
        val existingUser = userRepository.persist(User.new(UserName("user1"), Password("seCreT123")))
        val nonExistingNoteId = Id.generate()
        val title = "My note title"

        val requestModel = UpdateNote.RequestModel(
            nonExistingNoteId.toString(),
            title,
            "An interesting body of a note",
            listOf("interesting", "note"),
            existingUser.id.toString(),
            NoteType.PUBLIC.toString()
        )

        val updateNote = UpdateNote(noteRepository, userRepository)
        val resultModel = updateNote.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.noteId)
        assertContains(resultModel.message, title)
        assertContains(resultModel.message, "could not be found")
    }

    @Test
    fun execute_invalidNoteId_returnsErrorResultModel() {
        val existingUser = userRepository.persist(User.new(UserName("user1"), Password("seCreT123")))
        val invalidNoteId = "<invalid id>"
        val title = "My note title"

        val requestModel = UpdateNote.RequestModel(
            invalidNoteId,
            title,
            "An interesting body of a note",
            listOf("interesting", "note"),
            existingUser.id.toString(),
            NoteType.PUBLIC.toString()
        )

        val updateNote = UpdateNote(noteRepository, userRepository)
        val resultModel = updateNote.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.noteId)
        assertContains(resultModel.message, title)
        assertContains(resultModel.message, "Invalid id value")
    }

    @Test
    fun execute_differentAuthor_returnsErrorResultModel() {
        val originalAuthor = userRepository.persist(User.new(UserName("user1"), Password("seCreT123")))
        val newAuthor = userRepository.persist(User.new(UserName("user2"), Password("seCreT123")))

        val title = "My note title"
        val existingNote = noteRepository.persist(
            Note.new()
                .withTitle(Title(title))
                .withBody(Body("An interesting body of a note"))
                .withTags(setOf(Tag("interesting"), Tag("note")))
                .withAuthor(originalAuthor)
                .withNoteType(NoteType.PUBLIC)
                .create()
        )

        val requestModel = UpdateNote.RequestModel(
            existingNote.id.toString(),
            existingNote.title.toString(),
            existingNote.body.toString(),
            existingNote.tags.map { t -> t.toString() }.toList(),
            newAuthor.id.toString(),
            existingNote.noteType.name
        )

        val updateNote = UpdateNote(noteRepository, userRepository)
        val resultModel = updateNote.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.noteId)
        assertContains(resultModel.message, title)
        assertContains(resultModel.message, "Modifying a note of another user ist not allowed")
    }

    @Test
    fun execute_invalidNoteType_returnsErrorResultModel() {
        val existingUser = userRepository.persist(User.new(UserName("user1"), Password("seCreT123")))
        val nonExistingNoteId = Id.generate()

        val title = "My note title"
        val noteType = "<invalid note type>"

        val requestModel = UpdateNote.RequestModel(
            nonExistingNoteId.toString(),
            title,
            "An interesting body of a note",
            listOf("interesting", "note"),
            existingUser.id.toString(),
            noteType
        )

        val updateNote = UpdateNote(noteRepository, userRepository)
        val resultModel = updateNote.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.noteId)
        assertContains(resultModel.message, title)
        assertContains(resultModel.message, "No enum constant")
    }

    @Test
    fun execute_validParameters_returnsSuccessResultModel() {
        val existingUser = userRepository.persist(User.new(UserName("user1"), Password("seCreT123")))
        val existingNote = noteRepository.persist(
            Note.new()
                .withTitle(Title("My note title"))
                .withBody(Body("An interesting body of a note"))
                .withTags(setOf(Tag("interesting"), Tag("note")))
                .withAuthor(existingUser)
                .withNoteType(NoteType.PUBLIC)
                .create()
        )

        Thread.sleep(1000)

        val requestModel = UpdateNote.RequestModel(
            existingNote.id.toString(),
            existingNote.title.toString(),
            existingNote.body.toString(),
            existingNote.tags.map { t -> t.toString() }.toList(),
            existingUser.id.toString(),
            existingNote.noteType.name
        )

        val updateNote = UpdateNote(noteRepository, userRepository)
        val resultModel = updateNote.execute(requestModel)

        assertTrue(resultModel.successful)
        assertNotNull(resultModel.noteId)
        assertContains(resultModel.message, existingNote.title.toString())
        assertContains(resultModel.message, "Successfully updated note")

        val updatedNote = noteRepository.getBy(existingNote.id)
        assertNotNull(updatedNote)
        assertTrue(updatedNote.createdAt.value < updatedNote.changedAt.value)
    }
}
