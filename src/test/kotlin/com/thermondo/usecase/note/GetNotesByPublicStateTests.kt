package com.thermondo.usecase.note

import com.thermondo.usecase.common.UseCaseTestBase
import com.thermondo.usecase.note.viewmodel.INoteDomainViewModelConverter
import org.junit.Test
import org.koin.test.inject
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetNotesByPublicStateTests : UseCaseTestBase() {
    private val viewModelConverter: INoteDomainViewModelConverter by inject()

    @Test
    fun execute_noPublicNotes_returnsSuccessResultModel() {
        val exitingInitialDataNotesCount = noteRepository.getAllPublic().size

        val authors = setupUsers("user1", "user2")
        val author1 = authors[0]
        val author2 = authors[1]
        val author1Notes = setupNotes(author1, "title11", "title12", "title13")
        val author2Notes = setupNotes(author2, "title21", "title22")
        setPrivate(author1Notes[0], author1Notes[1], author1Notes[2])
        setPrivate(author2Notes[0], author2Notes[1])

        val requestModel = GetNotesByPublicState.RequestModel()

        val getNotesByPublicState = GetNotesByPublicState(noteRepository, userRepository, viewModelConverter)
        val resultModel = getNotesByPublicState.execute(requestModel)

        assertTrue(resultModel.successful)
        // soft "empty" (only previous initial test data)
        assertEquals(exitingInitialDataNotesCount, resultModel.notes.size)
        assertContains(resultModel.message, "Successfully retrieved public notes")
    }

    @Test
    fun execute_multiplePublicNotes_returnsSuccessResultModel() {
        val exitingInitialDataNotesCount = noteRepository.getAllPublic().size

        val authors = setupUsers("user1", "user2")
        val author1 = authors[0]
        val author2 = authors[1]
        val author1Notes = setupNotes(author1, "title11", "title12", "title13")
        val author2Notes = setupNotes(author2, "title21", "title22")
        setPrivate(author1Notes[1])
        setPrivate(author2Notes[1])

        val requestModel = GetNotesByPublicState.RequestModel()

        val getNotesByPublicState = GetNotesByPublicState(noteRepository, userRepository, viewModelConverter)
        val resultModel = getNotesByPublicState.execute(requestModel)

        assertTrue(resultModel.successful)
        assertEquals(exitingInitialDataNotesCount + 3, resultModel.notes.size)
        assertContains(resultModel.message, "Successfully retrieved public notes")
    }
}
