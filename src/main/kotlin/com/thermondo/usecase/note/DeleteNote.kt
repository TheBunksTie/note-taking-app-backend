package com.thermondo.usecase.note

import com.thermondo.domain.model.Id
import com.thermondo.domain.note.Note
import com.thermondo.domain.user.User
import com.thermondo.usecase.common.UseCase
import com.thermondo.usecase.note.abstraction.INoteRepository
import com.thermondo.usecase.user.abstraction.IUserRepository

/**
 * Use case class to delete existing notes (for original authors only)
 * @param noteRepository repository to store and retrieve [Note] objects
 * @param userRepository repository to store and retrieve [User] objects
 */
class DeleteNote(
    private val noteRepository: INoteRepository,
    private val userRepository: IUserRepository
) :
    UseCase<DeleteNote.RequestModel, DeleteNote.ResultModel>() {

    override fun execute(input: RequestModel): ResultModel {
        try {
            val authorId = Id.fromString(input.authorId)
            val author = userRepository.getBy(authorId)
                ?: return ResultModel.errorResult(input.id, "User with id '${input.authorId}' could not be found")

            val noteId = Id.fromString(input.id)
            val existingNote = noteRepository.getBy(noteId)
                ?: return ResultModel.errorResult(input.id, "Note with id '${input.id}' could not be found")

            if (existingNote.author != author)
                return ResultModel.errorResult(input.id, "Deleting a note of another user ist not allowed")

            val isDeleted = noteRepository.delete(existingNote.id)
            if (!isDeleted)
                return ResultModel.errorResult(existingNote.title.toString(), "Deleting note '${existingNote.id}' was not possible")

            return ResultModel.successResult(existingNote.id.toString(), existingNote.title.toString())
        } catch (e: Exception) {
            return ResultModel.errorResult(input.id, e.message)
        }
    }

    class RequestModel(val id: String, val authorId: String) : UseCase.RequestModel

    class ResultModel(val noteId: String?, override val successful: Boolean, override val message: String) : UseCase.ResultModel {
        // TODO in real life, status message would be either localized here
        //  or have an id to be translated in consuming instance (e.g. frontend)
        companion object FactoryMethods {
            fun successResult(noteId: String, noteTitle: String): ResultModel {
                return ResultModel(noteId, true, "Successfully deleted note '$noteTitle'")
            }

            fun errorResult(noteId: String?, errorReason: String?): ResultModel {
                val effectiveNoteTitleName = if (!noteId.isNullOrEmpty()) noteId else "<null or empty note id>"
                val effectiveErrorReason = if (!errorReason.isNullOrEmpty()) errorReason else "<no error reason provided>"
                return ResultModel(null, false, "Unable to delete note '$effectiveNoteTitleName' because of error: $effectiveErrorReason")
            }
        }
    }
}
