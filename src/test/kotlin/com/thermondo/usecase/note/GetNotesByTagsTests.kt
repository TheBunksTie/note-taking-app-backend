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

class GetNotesByTagsTests : UseCaseTestBase() {
    private val viewModelConverter: INoteDomainViewModelConverter by inject()

    @Test
    fun execute_nonExistingAuthor_returnsErrorResultModel() {
        val nonExistingAuthorId = Id.generate()

        val requestModel = GetNotesByTags.RequestModel(nonExistingAuthorId.toString(), emptyList())

        val getNotesByTags = GetNotesByTags(noteRepository, userRepository, viewModelConverter)
        val resultModel = getNotesByTags.execute(requestModel)

        assertFalse(resultModel.successful)
        assertTrue(resultModel.notes.isEmpty())
        assertContains(resultModel.message, nonExistingAuthorId.toString())
        assertContains(resultModel.message, "could not be found")
    }

    @Test
    fun execute_validParametersButNoNotesWithRequestedTags_returnsSuccessResultModel() {
        val authors = setupUsers("user1")
        val author1 = authors[0]
        val author1Notes = setupNotes(author1, "title11", "title12", "title13", "title14")
        val searchedTag1 = "tag6"
        val searchedTag2 = "tag7"
        updateTags(author1Notes[0], "tag1", "tag2", "tag3")
        updateTags(author1Notes[1])
        updateTags(author1Notes[2], "tag1", "tag3", "tag4")
        updateTags(author1Notes[3], "tag5")

        val requestModel = GetNotesByTags.RequestModel(author1.id.toString(), listOf(searchedTag1, searchedTag2))

        val getNotesByTags = GetNotesByTags(noteRepository, userRepository, viewModelConverter)
        val resultModel = getNotesByTags.execute(requestModel)

        assertTrue(resultModel.successful)
        assertTrue(resultModel.notes.isEmpty())
        assertContains(resultModel.message, author1.id.toString())
        assertContains(resultModel.message, "Successfully retrieved notes for author")
    }

    @Test
    fun execute_validParametersAndMultipleNotesWithTargets_returnsSuccessResultModel() {
        val authors = setupUsers("user1")
        val author1 = authors[0]
        val author1Notes = setupNotes(author1, "title11", "title12", "title13", "title14")
        val searchedTag1 = "tag1"
        val searchedTag2 = "tag3"
        val searchedTag3 = "tag5"
        updateTags(author1Notes[0], "tag1", "tag2", "tag3")
        updateTags(author1Notes[1])
        updateTags(author1Notes[2], "tag1", "tag4")
        updateTags(author1Notes[3], "tag5")

        val requestModel = GetNotesByTags.RequestModel(author1.id.toString(), listOf(searchedTag1, searchedTag2, searchedTag3))

        val getNotesByTags = GetNotesByTags(noteRepository, userRepository, viewModelConverter)
        val resultModel = getNotesByTags.execute(requestModel)

        assertTrue(resultModel.successful)
        assertEquals(3, resultModel.notes.size)
        assertContains(resultModel.message, author1.id.toString())
        assertContains(resultModel.message, "Successfully retrieved notes for author")
    }
}
