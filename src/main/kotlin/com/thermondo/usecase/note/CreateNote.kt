package com.thermondo.usecase.note

import com.thermondo.domain.model.Id
import com.thermondo.domain.note.*
import com.thermondo.domain.user.User
import com.thermondo.usecase.common.UseCase
import com.thermondo.usecase.note.abstraction.INoteRepository
import com.thermondo.usecase.user.abstraction.IUserRepository

/**
 * Use case class to create new notes
 * @param noteRepository repository to store and retrieve [Note] objects
 * @param userRepository repository to store and retrieve [User] objects
 */
class CreateNote(
    private val noteRepository: INoteRepository,
    private val userRepository: IUserRepository
) :
    UseCase<CreateNote.RequestModel, CreateNote.ResultModel>() {

    override fun execute(input: RequestModel): ResultModel {
        try {
            val authorId = Id.fromString(input.authorId)
            val author = userRepository.getBy(authorId)
                ?: return ResultModel.errorResult(input.title, "User with id '${input.authorId}' could not be found")

            val noteType = NoteType.valueOf(input.noteType)

            val newNoteBuilder = Note.fromAuthor(author).withNoteType(noteType)

            if (input.title != null)
                newNoteBuilder.withTitle(Title(input.title))
            if (input.body != null)
                newNoteBuilder.withBody(Body(input.body))
            if (input.tags.isNotEmpty())
                newNoteBuilder.withTags(input.tags.stream().map { t -> Tag(t) }.toList().toSet())

            val newNote = newNoteBuilder.create()
            val persistedNewNote = noteRepository.persist(newNote)

            return ResultModel.successResult(persistedNewNote.id.toString(), persistedNewNote.title.toString())
        } catch (e: Exception) {
            return ResultModel.errorResult(input.title, e.message)
        }
    }

    class RequestModel(
        val title: String?,
        val body: String?,
        val tags: List<String>,
        val authorId: String,
        val noteType: String
    ) : UseCase.RequestModel

    class ResultModel(val noteId: String?, override val successful: Boolean, override val message: String) :
        UseCase.ResultModel {
        // TODO in real life, status message would be either localized here
        //  or have an id to be translated in consuming instance (e.g. frontend)
        companion object FactoryMethods {
            fun successResult(noteId: String, noteTitle: String): ResultModel {
                return ResultModel(noteId, true, "Successfully created note '$noteTitle'")
            }

            fun errorResult(noteTitle: String?, errorReason: String?): ResultModel {
                val effectiveNoteTitleName = if (!noteTitle.isNullOrEmpty()) noteTitle else "<null or empty note title>"
                val effectiveErrorReason = if (!errorReason.isNullOrEmpty()) errorReason else "<no error reason provided>"
                return ResultModel(null, false, "Unable to create note '$effectiveNoteTitleName' because of error: $effectiveErrorReason")
            }
        }
    }
}
