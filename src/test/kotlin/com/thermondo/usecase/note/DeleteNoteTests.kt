package com.thermondo.usecase.note

import com.thermondo.domain.model.Id
import com.thermondo.domain.note.*
import com.thermondo.domain.user.Password
import com.thermondo.domain.user.User
import com.thermondo.domain.user.UserName
import com.thermondo.persistence.note.notePersistenceModule
import com.thermondo.persistence.user.userPersistenceModule
import com.thermondo.usecase.note.abstraction.INoteRepository
import com.thermondo.usecase.user.abstraction.IUserRepository
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import kotlin.test.*
import kotlin.test.assertNotNull

class DeleteNoteTests : KoinTest {
    private val noteRepository: INoteRepository by inject()
    private val userRepository: IUserRepository by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(userPersistenceModule, notePersistenceModule)
    }

    @Test
    fun execute_nonExistingAuthor_returnsErrorResultModel() {
        val nonExistingAuthorId = Id.generate()
        val nonExistingNoteId = Id.generate()

        val requestModel = DeleteNote.RequestModel(nonExistingNoteId.toString(), nonExistingAuthorId.toString())

        val deleteNote = DeleteNote(noteRepository, userRepository)
        val resultModel = deleteNote.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.noteId)
        assertContains(resultModel.message, nonExistingNoteId.toString())
        assertContains(resultModel.message, "could not be found")
    }

    @Test
    fun execute_invalidAuthorId_returnsErrorResultModel() {
        val nonExistingNoteId = Id.generate()
        val invalidAuthorId = "<invalid id>"

        val requestModel = DeleteNote.RequestModel(nonExistingNoteId.toString(), invalidAuthorId)

        val deleteNote = DeleteNote(noteRepository, userRepository)
        val resultModel = deleteNote.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.noteId)
        assertContains(resultModel.message, nonExistingNoteId.toString())
        assertContains(resultModel.message, "Invalid id value")
    }

    @Test
    fun execute_nonExistingNote_returnsErrorResultModel() {
        val existingUser = userRepository.persist(User.new(UserName("user1"), Password("seCreT123")))
        val nonExistingNoteId = Id.generate()

        val requestModel = DeleteNote.RequestModel(nonExistingNoteId.toString(), existingUser.id.toString())

        val deleteNote = DeleteNote(noteRepository, userRepository)
        val resultModel = deleteNote.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.noteId)
        assertContains(resultModel.message, nonExistingNoteId.toString())
        assertContains(resultModel.message, "could not be found")
    }

    @Test
    fun execute_invalidNoteId_returnsErrorResultModel() {
        val existingUser = userRepository.persist(User.new(UserName("user1"), Password("seCreT123")))
        val invalidNoteId = "<invalid id>"

        val requestModel = DeleteNote.RequestModel(invalidNoteId, existingUser.id.toString())

        val deleteNote = DeleteNote(noteRepository, userRepository)
        val resultModel = deleteNote.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.noteId)
        assertContains(resultModel.message, invalidNoteId)
        assertContains(resultModel.message, "Invalid id value")
    }

    @Test
    fun execute_differentAuthor_returnsErrorResultModel() {
        val originalAuthor = userRepository.persist(User.new(UserName("user1"), Password("seCreT123")))
        val newAuthor = userRepository.persist(User.new(UserName("user2"), Password("seCreT123")))

        val existingNote = noteRepository.persist(Note.new()
            .withTitle(Title("My note title"))
            .withBody(Body("An interesting body of a note"))
            .withTags(listOf(Tag("interesting"), Tag("note")))
            .withAuthor(originalAuthor)
            .withNoteType(NoteType.PUBLIC)
            .create())

        val requestModel = DeleteNote.RequestModel(existingNote.id.toString(), newAuthor.id.toString())

        val deleteNote = DeleteNote(noteRepository, userRepository)
        val resultModel = deleteNote.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.noteId)
        assertContains(resultModel.message, existingNote.id.toString())
        assertContains(resultModel.message, "Deleting a note of another user ist not allowed")
    }

    @Test
    fun execute_validParameters_returnsSuccessResultModel() {
        val existingUser = userRepository.persist(User.new(UserName("user1"), Password("seCreT123")))
        val existingNote = noteRepository.persist(Note.new()
            .withTitle(Title("My note title"))
            .withBody(Body("An interesting body of a note"))
            .withTags(listOf(Tag("interesting"), Tag("note")))
            .withAuthor(existingUser)
            .withNoteType(NoteType.PUBLIC)
            .create())

        val requestModel = DeleteNote.RequestModel(
            existingNote.id.toString(),
            existingUser.id.toString())

        val deleteNote = DeleteNote(noteRepository, userRepository)
        val resultModel = deleteNote.execute(requestModel)

        assertTrue(resultModel.successful)
        assertNotNull(resultModel.noteId)
        assertContains(resultModel.message, existingNote.title.toString())
        assertContains(resultModel.message, "Successfully deleted note")
    }
}
