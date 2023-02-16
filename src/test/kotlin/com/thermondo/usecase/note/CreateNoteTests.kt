package com.thermondo.usecase.note

import com.thermondo.domain.model.Id
import com.thermondo.domain.note.NoteType
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

class CreateNoteTests : KoinTest {
    private val noteRepository: INoteRepository by inject()
    private val userRepository: IUserRepository by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(userPersistenceModule, notePersistenceModule)
    }

    @Test
    fun execute_nonExistingAuthor_returnsErrorResultModel() {
        val nonExistingAuthorId = Id.generate().toString()
        val title = "My note title"
        val body = "An interesting body of a note"
        val tags = listOf("interesting", "note")
        val noteType = NoteType.PUBLIC.toString()

        val requestModel = CreateNote.RequestModel(title, body, tags, nonExistingAuthorId, noteType)

        val createNote = CreateNote(noteRepository, userRepository)
        val resultModel = createNote.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.noteId)
        assertContains(resultModel.message, title)
        assertContains(resultModel.message, "could not be found")
    }

    @Test
    fun execute_invalidAuthorId_returnsErrorResultModel() {
        val invalidAuthorId = "<invalid id>"
        val title = "My note title"
        val body = "An interesting body of a note"
        val tags = listOf("interesting", "note")
        val noteType = NoteType.PUBLIC.toString()

        val requestModel = CreateNote.RequestModel(title, body, tags, invalidAuthorId, noteType)

        val createNote = CreateNote(noteRepository, userRepository)
        val resultModel = createNote.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.noteId)
        assertContains(resultModel.message, title)
        assertContains(resultModel.message, "Invalid id value")
    }

    @Test
    fun execute_invalidNoteType_returnsErrorResultModel() {
        val existingUser = userRepository.persist(User.new(UserName("user1"), Password("seCreT123")))

        val title = "My note title"
        val body = "An interesting body of a note"
        val tags = listOf("interesting", "note")
        val noteType = "<invalid note type>"

        val requestModel = CreateNote.RequestModel(title, body, tags, existingUser.id.toString(), noteType)

        val createNote = CreateNote(noteRepository, userRepository)
        val resultModel = createNote.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.noteId)
        assertContains(resultModel.message, title)
        assertContains(resultModel.message, "No enum constant")
    }

    @Test
    fun execute_nullTitle_returnsErrorResultModel() {
        val existingUser = userRepository.persist(User.new(UserName("user1"), Password("seCreT123")))

        val body = "An interesting body of a note"
        val tags = listOf("interesting", "note")
        val noteType = NoteType.PUBLIC.toString()

        val requestModel = CreateNote.RequestModel(null, body, tags, existingUser.id.toString(), noteType)

        val createNote = CreateNote(noteRepository, userRepository)
        val resultModel = createNote.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.noteId)
        assertContains(resultModel.message, "A note must have a title")
    }

    @Test
    fun execute_validMinimumParameters_returnsSuccessResultModel() {
        val existingUser = userRepository.persist(User.new(UserName("user1"), Password("seCreT123")))
        val title = "My note title"
        val tags = emptyList<String>()
        val noteType = NoteType.PUBLIC.toString()

        val requestModel = CreateNote.RequestModel(title, null, tags, existingUser.id.toString(), noteType)

        val createNote = CreateNote(noteRepository, userRepository)
        val resultModel = createNote.execute(requestModel)

        assertTrue(resultModel.successful)
        assertNotNull(resultModel.noteId)
        assertContains(resultModel.message, title)
        assertContains(resultModel.message, "Successfully created note")
    }

    @Test
    fun execute_validParameters_returnsSuccessResultModel() {
        val existingUser = userRepository.persist(User.new(UserName("user1"), Password("seCreT123")))

        val title = "My note title"
        val body = "An interesting body of a note"
        val tags = listOf("interesting", "note")
        val noteType = NoteType.PUBLIC.toString()

        val requestModel = CreateNote.RequestModel(title, body, tags, existingUser.id.toString(), noteType)

        val createNote = CreateNote(noteRepository, userRepository)
        val resultModel = createNote.execute(requestModel)

        assertTrue(resultModel.successful)
        assertNotNull(resultModel.noteId)
        assertContains(resultModel.message, title)
        assertContains(resultModel.message, "Successfully created note")
    }
}
