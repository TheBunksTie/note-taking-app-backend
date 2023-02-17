package com.thermondo.usecase.note

import com.thermondo.domain.model.ChangedAt
import com.thermondo.domain.model.Id
import com.thermondo.domain.note.Body
import com.thermondo.domain.note.Note
import com.thermondo.domain.note.NoteType
import com.thermondo.domain.note.Tag
import com.thermondo.domain.note.Title
import com.thermondo.domain.user.User
import com.thermondo.usecase.common.UseCase
import com.thermondo.usecase.note.abstraction.INoteRepository
import com.thermondo.usecase.user.abstraction.IUserRepository

/**
 * Use case class to updated existing notes (for original authors only)
 * @param noteRepository repository to store and retrieve [Note] objects
 * @param userRepository repository to store and retrieve [User] objects
 */
class UpdateNote(
    private val noteRepository: INoteRepository,
    private val userRepository: IUserRepository
) :
    UseCase<UpdateNote.RequestModel, UpdateNote.ResultModel>() {

    override fun execute(input: RequestModel): ResultModel {
        try {
            val authorId = Id.fromString(input.authorId)
            val author = userRepository.getBy(authorId)
                ?: return ResultModel.errorResult(
                    input.title,
                    "User with id '${input.authorId}' could not be found"
                )

            val noteType = NoteType.valueOf(input.noteType)

            val noteId = Id.fromString(input.id)
            val existingNote = noteRepository.getBy(noteId)
                ?: return ResultModel.errorResult(
                    input.title,
                    "Note with id '${input.id}' could not be found"
                )

            if (existingNote.author != author)
                return ResultModel.errorResult(
                    input.title,
                    "Modifying a note of another user ist not allowed"
                )

            if (input.title != null)
                existingNote.title = Title(input.title)
            if (input.body != null)
                existingNote.body = Body(input.body)

            existingNote.tags = input.tags.map { t -> Tag(t) }.toSet()

            existingNote.noteType = noteType
            existingNote.changedAt = ChangedAt.now()

            // TODO detection if an actual value change happened and specific message if not
            val persistedExistingNote = noteRepository.persist(existingNote)
            return ResultModel.successResult(
                persistedExistingNote.id.toString(),
                persistedExistingNote.title.toString()
            )
        } catch (e: Exception) {
            return ResultModel.errorResult(input.title, e.message)
        }
    }

    class RequestModel(
        val id: String,
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
                return ResultModel(noteId, true, "Successfully updated note '$noteTitle'")
            }

            fun errorResult(noteTitle: String?, errorReason: String?): ResultModel {
                val effectiveNoteTitleName =
                    if (!noteTitle.isNullOrEmpty()) noteTitle
                    else "<null or empty note title>"

                val effectiveErrorReason =
                    if (!errorReason.isNullOrEmpty()) errorReason
                    else "<no error reason provided>"

                return ResultModel(
                    null,
                    false,
                    "Unable to update note '$effectiveNoteTitleName' because of error: $effectiveErrorReason"
                )
            }
        }
    }
}
