package com.thermondo.common

import com.thermondo.domain.note.Body
import com.thermondo.domain.note.Note
import com.thermondo.domain.note.NoteType
import com.thermondo.domain.note.Tag
import com.thermondo.domain.note.Title
import com.thermondo.domain.user.Password
import com.thermondo.domain.user.User
import com.thermondo.domain.user.UserName
import com.thermondo.persistence.note.notePersistenceModule
import com.thermondo.persistence.user.userPersistenceModule
import com.thermondo.usecase.note.abstraction.INoteRepository
import com.thermondo.usecase.note.noteUseCaseModule
import com.thermondo.usecase.user.abstraction.IUserRepository
import org.junit.Rule
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

abstract class TestBase : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(userPersistenceModule, notePersistenceModule, noteUseCaseModule)
    }

    protected val noteRepository: INoteRepository by inject()
    protected val userRepository: IUserRepository by inject()

    protected fun setPrivate(vararg notes: Note) {
        for (n in notes) {
            n.noteType = NoteType.PRIVATE
            noteRepository.persist(n)
        }
    }

    protected fun updateTags(note: Note, vararg tags: String) {
        note.tags = tags.map { t -> Tag(t) }.toSet()
        noteRepository.persist(note)
    }

    protected fun updateBody(note: Note, body: String) {
        note.body = Body(body)
        noteRepository.persist(note)
    }

    protected fun setupUsers(vararg userNames: String): List<User> {
        return userNames.map { u -> userRepository.persist(User.new(UserName(u), Password("seCreT123"))) }
    }

    protected fun setupNotes(author: User, vararg titleNames: String): List<Note> {
        val body = Body("An interesting body of a note")
        val tags = setOf(Tag("interesting"), Tag("note"))
        val initialNoteType = NoteType.PUBLIC

        return titleNames.map { t ->
            Thread.sleep(200)
            noteRepository.persist(
                Note.new()
                    .withTitle(Title(t))
                    .withBody(body)
                    .withTags(tags)
                    .withAuthor(author)
                    .withNoteType(initialNoteType)
                    .create()
            )
        }
    }
}
