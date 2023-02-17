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

class GetNotesByKeywordsTests : UseCaseTestBase() {
    private val viewModelConverter: INoteDomainViewModelConverter by inject()

    @Test
    fun execute_nonExistingAuthor_returnsErrorResultModel() {
        val nonExistingAuthorId = Id.generate()

        val requestModel = GetNotesByKeywords.RequestModel(nonExistingAuthorId.toString(), emptyList())

        val getNotesByKeywords = GetNotesByKeywords(noteRepository, userRepository, viewModelConverter)
        val resultModel = getNotesByKeywords.execute(requestModel)

        assertFalse(resultModel.successful)
        assertTrue(resultModel.notes.isEmpty())
        assertContains(resultModel.message, nonExistingAuthorId.toString())
        assertContains(resultModel.message, "could not be found")
    }

    @Test
    fun execute_validParametersButNoNotesContainingKeywords_returnsSuccessResultModel() {
        val author1 = setupUsers("user1")[0]
        val author1Notes = setupNotes(author1, "title11", "title12", "title13", "title14")
        val searchedKeyword1 = "keyword1"
        val searchedKeyword2 = "keyword2"
        val notSearchedKeyword = "keyword3"
        updateBody(author1Notes[1], "single content of $notSearchedKeyword")

        val requestModel = GetNotesByKeywords.RequestModel(author1.id.toString(), listOf(searchedKeyword1, searchedKeyword2))

        val getNotesByKeywords = GetNotesByKeywords(noteRepository, userRepository, viewModelConverter)
        val resultModel = getNotesByKeywords.execute(requestModel)

        assertTrue(resultModel.successful)
        assertTrue(resultModel.notes.isEmpty())
        assertContains(resultModel.message, author1.id.toString())
        assertContains(resultModel.message, "Successfully retrieved notes for author")
    }

    @Test
    fun execute_validParametersAndMultipleNotesWithTargets_returnsSuccessResultModel() {
        val author1 = setupUsers("user1")[0]
        val author1Notes = setupNotes(author1, "title11", "title12", "title13", "title14")
        val searchedKeyword1 = "keyword1"
        val searchedKeyword2 = "keyword2"
        val searchedKeyword3 = "keyword3"

        updateBody(author1Notes[0], "$searchedKeyword1 and other $searchedKeyword2")
        updateBody(author1Notes[2], "single content of $searchedKeyword3")

        val requestModel = GetNotesByKeywords.RequestModel(author1.id.toString(), listOf(searchedKeyword1, searchedKeyword2, searchedKeyword3))

        val getNotesByKeywords = GetNotesByKeywords(noteRepository, userRepository, viewModelConverter)
        val resultModel = getNotesByKeywords.execute(requestModel)

        assertTrue(resultModel.successful)
        assertEquals(2, resultModel.notes.size)
        assertContains(resultModel.message, author1.id.toString())
        assertContains(resultModel.message, "Successfully retrieved notes for author")
    }
}
