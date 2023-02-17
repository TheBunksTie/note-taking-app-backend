package com.thermondo.usecase.note

import com.thermondo.domain.model.Id
import com.thermondo.usecase.common.UseCaseTestBase
import com.thermondo.usecase.note.viewmodel.INoteDomainViewModelConverter
import org.junit.Test
import org.koin.test.inject
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GetNotesByAuthorTests : UseCaseTestBase() {
    private val viewModelConverter: INoteDomainViewModelConverter by inject()

    @Test
    fun execute_nonExistingAuthor_returnsErrorResultModel() {
        val nonExistingAuthorId = Id.generate()

        val requestModel = GetNotesByAuthor.RequestModel(nonExistingAuthorId.toString())

        val getNotesByAuthor = GetNotesByAuthor(noteRepository, userRepository, viewModelConverter)
        val resultModel = getNotesByAuthor.execute(requestModel)

        assertFalse(resultModel.successful)
        assertTrue(resultModel.notes.isEmpty())
        assertContains(resultModel.message, nonExistingAuthorId.toString())
        assertContains(resultModel.message, "could not be found")
    }

    @Test
    fun execute_validParametersButNoNotes_returnsSuccessResultModel() {
        val authors = setupUsers("user1", "user2")
        val author1 = authors[0]
        val author2 = authors[1]
        setupNotes(author1, "title11", "title12")

        val requestModel = GetNotesByAuthor.RequestModel(author2.id.toString())

        val getNotesByAuthor = GetNotesByAuthor(noteRepository, userRepository, viewModelConverter)
        val resultModel = getNotesByAuthor.execute(requestModel)

        assertTrue(resultModel.successful)
        assertTrue(resultModel.notes.isEmpty())
        assertContains(resultModel.message, author2.id.toString())
        assertContains(resultModel.message, "Successfully retrieved notes for author")
    }

    @Test
    fun execute_validParametersAndMultipleNotes_returnsSuccessResultModel() {
        val authors = setupUsers("user1", "user2")
        val author1 = authors[0]
        val author2 = authors[1]
        setupNotes(author1, "title11", "title12")
        setupNotes(author2, "title21")

        val requestModel = GetNotesByAuthor.RequestModel(author1.id.toString())

        val getNotesByAuthor = GetNotesByAuthor(noteRepository, userRepository, viewModelConverter)
        val resultModel = getNotesByAuthor.execute(requestModel)

        assertTrue(resultModel.successful)
        assertEquals(2, resultModel.notes.size)
        assertContains(resultModel.message, author1.id.toString())
        assertContains(resultModel.message, "Successfully retrieved notes for author")
    }
}
