package com.thermondo.application.note

import com.thermondo.domain.note.Note
import com.thermondo.usecase.note.CreateNote
import com.thermondo.usecase.note.DeleteNote
import com.thermondo.usecase.note.GetNotesByAuthor
import com.thermondo.usecase.note.GetNotesByKeywords
import com.thermondo.usecase.note.GetNotesByPublicState
import com.thermondo.usecase.note.GetNotesByTags
import com.thermondo.usecase.note.UpdateNote

/**
 * Central point of interaction with all [Note] related use cases for presentation layer
 */
class NoteService(
    private val createNote: CreateNote,
    private val updateNote: UpdateNote,
    private val deleteNote: DeleteNote,
    private val getNotesByAuthor: GetNotesByAuthor,
    private val getNotesByTags: GetNotesByTags,
    private val getNotesByKeywords: GetNotesByKeywords,
    private val getNotesByPublicState: GetNotesByPublicState
) {

    fun createNote(title: String?, body: String?, tags: List<String>, authorId: String, noteType: String): CreateNote.ResultModel {

        val request = CreateNote.RequestModel(title, body, tags, authorId, noteType)
        return createNote.execute(request)
    }

    fun updateNote(id: String, title: String?, body: String?, tags: List<String>, authorId: String, noteType: String): UpdateNote.ResultModel {

        val request = UpdateNote.RequestModel(id, title, body, tags, authorId, noteType)
        return updateNote.execute(request)
    }

    fun deleteNote(id: String, authorId: String): DeleteNote.ResultModel {
        val request = DeleteNote.RequestModel(id, authorId)
        return deleteNote.execute(request)
    }

    fun getNotesByAuthor(authorId: String): GetNotesByAuthor.ResultModel {
        val request = GetNotesByAuthor.RequestModel(authorId)
        return getNotesByAuthor.execute(request)
    }

    fun getNotesByTags(authorId: String, tags: List<String>): GetNotesByTags.ResultModel {
        val request = GetNotesByTags.RequestModel(authorId, tags)
        return getNotesByTags.execute(request)
    }

    fun getNotesByKeywords(authorId: String, keywords: List<String>): GetNotesByKeywords.ResultModel {
        val request = GetNotesByKeywords.RequestModel(authorId, keywords)
        return getNotesByKeywords.execute(request)
    }

    fun getNotesByPublicState(): GetNotesByPublicState.ResultModel {
        val request = GetNotesByPublicState.RequestModel()
        return getNotesByPublicState.execute(request)
    }
}
