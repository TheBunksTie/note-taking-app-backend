package com.thermondo.persistence.note

import com.thermondo.common.TestBase
import com.thermondo.domain.note.*
import com.thermondo.domain.user.Password
import com.thermondo.domain.user.User
import com.thermondo.domain.user.UserName
import kotlin.test.*

class NoteRepositoryTest : TestBase() {

    // TODO of course the different components of the repository system should be tested by unit tests
    @Test
    fun crudNoteIntegrationTest() {
        val exitingInitialDataNotesCount = noteRepository.getAll().size

        val initialTitle = Title("My note title")
        val initialBody = Body("An interesting body of a note")
        val initialTags = setOf(Tag("interesting"), Tag("note"))
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
        val updatedTags = setOf(Tag("interesting"), Tag("note"), Tag("newTag"))
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
        assertEquals(exitingInitialDataNotesCount + 1, persistedNotesList.size)

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

        // "empty" get all (only previous initial test data)
        val persistedNotesList2 = noteRepository.getAll()
        assertEquals(exitingInitialDataNotesCount, persistedNotesList2.size)
    }

    @Test
    fun getAllByAuthor_validAuthorId_returnsNoteList() {
        val exitingInitialDataNotesCount = noteRepository.getAll().size

        val users = setupUsers("user1", "user2")
        val author1 = users[0]
        val author1Notes = setupNotes(author1, "user1-title1", "user1-title2", "user1-title3")
        setupNotes(users[1], "user2-title1")

        val allNotesCount = noteRepository.getAll().size
        assertEquals(exitingInitialDataNotesCount + 4, allNotesCount)

        val author1NoteList = noteRepository.getAllByAuthor(author1.id)
        assertEquals(3, author1NoteList.size)
        assertEquals(author1Notes, author1NoteList)
    }

    @Test
    fun getAllByTags_multipleTags_returnsNoteList() {
        val exitingInitialDataNotesCount = noteRepository.getAll().size

        val users = setupUsers("user1", "user2")
        val author1 = users[0]
        val title1 = "user1-title1"
        val title3 = "user1-title3"
        val author1Notes = setupNotes(author1, title1, "user1-title2", title3)
        val searchedTag1 = "user1-tag12"
        val searchedTag2 = "user1-tag31"
        val searchedTag3 = "user1-tag11"
        updateTags(author1Notes[0], searchedTag3, searchedTag1)
        updateTags(author1Notes[1])
        updateTags(author1Notes[2], searchedTag2)

        setupNotes(users[1], "user2-title1")

        val allNotesCount = noteRepository.getAll().size
        assertEquals(exitingInitialDataNotesCount + 4, allNotesCount)

        val noteListByTags1 = noteRepository.getAllByTags(author1.id, setOf(Tag(searchedTag1), Tag(searchedTag2)))
        assertEquals(2, noteListByTags1.size)
        assertEquals(Title(title1), noteListByTags1[0].title)
        assertEquals(Title(title3), noteListByTags1[1].title)

        val noteListByTags2 = noteRepository.getAllByTags(author1.id, setOf(Tag(searchedTag3)))
        assertEquals(1, noteListByTags2.size)
        assertEquals(Title(title1), noteListByTags2[0].title)
    }

    @Test
    fun getAllByKeywords_multipleKeywords_returnsNoteList() {
        val exitingInitialDataNotesCount = noteRepository.getAll().size

        val users = setupUsers("user1", "user2")
        val author1 = users[0]
        val title1 = "user1-title1"
        val title2 = "user1-title2"
        val title3 = "user1-title3"
        val author1Notes = setupNotes(author1, title1, title2, title3)
        val searchedKeyword1 = "keyword1"
        val searchedKeyword2 = "keyword2"
        val searchedKeyword3 = "keyword3"

        updateBody(author1Notes[0], "$searchedKeyword2 and other $searchedKeyword1")
        updateBody(author1Notes[1], "single content of $searchedKeyword3")
        updateBody(author1Notes[2], "$searchedKeyword1 only an no other keyword content")

        setupNotes(users[1], "user2-title1")

        val allNotesCount = noteRepository.getAll().size
        assertEquals(exitingInitialDataNotesCount + 4, allNotesCount)

        val noteListByTags1 = noteRepository.getAllByKeywords(author1.id, setOf(Keyword(searchedKeyword1), Keyword(searchedKeyword2)))
        assertEquals(2, noteListByTags1.size)
        assertEquals(Title(title1), noteListByTags1[0].title)
        assertEquals(Title(title3), noteListByTags1[1].title)

        val noteListByTags2 = noteRepository.getAllByKeywords(author1.id, setOf(Keyword(searchedKeyword3)))
        assertEquals(1, noteListByTags2.size)
        assertEquals(Title(title2), noteListByTags2[0].title)
    }

    @Test
    fun getAllPublic_multiplePublicNotes_returnsNoteList() {
        val exitingInitialDataNotesCount = noteRepository.getAll().size
        val exitingInitialDataPublicNotesCount = noteRepository.getAllPublic().size

        val users = setupUsers("user1", "user2")
        val author1 = users[0]
        val author2 = users[1]
        val title12 = "user1-title2"
        val title21 = "user2-title1"
        val author1Notes = setupNotes(author1, "user1-title1", title12, "user1-title3")
        setPrivate(author1Notes[0])
        setPrivate(author1Notes[2])
        setupNotes(author2, title21)

        val allNotesCount = noteRepository.getAll().size
        assertEquals(exitingInitialDataNotesCount + 4, allNotesCount)

        val publicNoteList = noteRepository.getAllPublic()
        assertEquals(exitingInitialDataPublicNotesCount + 2, publicNoteList.size)
        assertEquals(Title(title12), publicNoteList[exitingInitialDataPublicNotesCount + 0].title)
        assertEquals(Title(title21), publicNoteList[exitingInitialDataPublicNotesCount + 1].title)
    }
}
