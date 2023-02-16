package com.thermondo.persistence.note

import com.thermondo.domain.note.*
import com.thermondo.domain.user.Password
import com.thermondo.domain.user.User
import com.thermondo.domain.user.UserName
import com.thermondo.persistence.user.userPersistenceModule
import com.thermondo.usecase.note.abstraction.INoteRepository
import org.junit.Rule
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import kotlin.test.*

class NoteRepositoryTest : KoinTest {
    private val noteRepository: INoteRepository by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(userPersistenceModule, notePersistenceModule)
    }

    // TODO of course the different components of the repository system should be tested by unit tests
    @Test
    fun crudNoteIntegrationTest() {

        val initialTitle = Title("My note title")
        val initialBody = Body("An interesting body of a note")
        val initialTags = listOf(Tag("interesting"), Tag("note"))
        val initialNoteType = NoteType.PUBLIC

        val initialAuthor = User.new(UserName("user1"), Password("seCreT123"))
        val initialNote = Note.new()
            .withTitle(initialTitle)
            .withBody(initialBody)
            .withTags(initialTags)
            .withAuthor(initialAuthor)
            .withNoteType(initialNoteType)
            .create()

        // initial persist
        val persistedNote = noteRepository.persist(initialNote)
        assertNotNull(persistedNote)
        assertEquals(initialTitle, persistedNote.title)
        assertEquals(initialBody, persistedNote.body)
        assertEquals(initialTags, persistedNote.tags)
        assertEquals(initialAuthor, persistedNote.author)
        assertEquals(initialNoteType, persistedNote.noteType)

        // updated persist
        val updatedTitle = Title("updated$initialTitle")
        val updatedBody = Body("updated$initialBody")
        val updatedTags = listOf(Tag("interesting"), Tag("note"), Tag("newTag"))
        val otherAuthor = User.new(UserName("user2"), Password("seCreT123"))
        val newNoteType = NoteType.PRIVATE
        persistedNote.title = updatedTitle
        persistedNote.body = updatedBody
        persistedNote.tags = updatedTags
        persistedNote.author = otherAuthor
        persistedNote.noteType = newNoteType
        noteRepository.persist(persistedNote)

        // get by id
        val updatedPersistedNote = noteRepository.getBy(persistedNote.id)
        assertNotNull(updatedPersistedNote)
        assertEquals(updatedTitle, updatedPersistedNote.title)
        assertEquals(updatedBody, updatedPersistedNote.body)
        assertEquals(updatedTags, updatedPersistedNote.tags)
        assertEquals(otherAuthor, updatedPersistedNote.author)
        assertEquals(newNoteType, updatedPersistedNote.noteType)

        // get all
        val persistedNotesList = noteRepository.getAll()
        assertEquals(1, persistedNotesList.size)

        // successful exists
        val exists1 = noteRepository.exists(updatedPersistedNote.id)
        assertTrue(exists1)

        // successful delete
        val delete1Result = noteRepository.delete(updatedPersistedNote.id)
        assertTrue(delete1Result)

        // non successful delete
        val delete2Result = noteRepository.delete(updatedPersistedNote.id)
        assertFalse(delete2Result)

        // non successful exists
        val exists2 = noteRepository.exists(updatedPersistedNote.id)
        assertFalse(exists2)

        // empty get all
        val persistedNotesList2 = noteRepository.getAll()
        assertTrue(persistedNotesList2.isEmpty())

    }
}
