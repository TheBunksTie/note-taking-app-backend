package com.thermondo.usecase.note

import com.thermondo.domain.model.Id
import com.thermondo.domain.note.Keyword
import com.thermondo.domain.note.Note
import com.thermondo.domain.user.User
import com.thermondo.usecase.common.UseCase
import com.thermondo.usecase.note.abstraction.INoteRepository
import com.thermondo.usecase.note.viewmodel.INoteDomainViewModelConverter
import com.thermondo.usecase.note.viewmodel.NoteViewModel
import com.thermondo.usecase.user.abstraction.IUserRepository

/**
 * Use case class to retrieve a list of existing notes for a specific user
 * containing one or more of requested [Keyword]s
 * @param noteRepository repository to store and retrieve [Note] objects
 * @param userRepository repository to store and retrieve [User] objects
 * @param viewModelConverter a one-way converter between [Note] and [NoteViewModel]
 */
class GetNotesByKeywords(
    private val noteRepository: INoteRepository,
    private val userRepository: IUserRepository,
    private val viewModelConverter: INoteDomainViewModelConverter
) :
    UseCase<GetNotesByKeywords.RequestModel, GetNotesByKeywords.ResultModel>() {

    override fun execute(input: RequestModel): ResultModel {
        try {
            val authorId = Id.fromString(input.authorId)
            val author = userRepository.getBy(authorId)
                ?: return ResultModel.errorResult(input.authorId, "User with id '${input.authorId}' could not be found")

            val requestedKeywords = input.keywords.map { t -> Keyword(t) }.toSet()
            val authoredNotes = noteRepository
                .getAllByKeywords(author.id, requestedKeywords)
                .map { n -> viewModelConverter.createFromT1(n) }
                .toList()

            return ResultModel.successResult(authoredNotes, author.id.toString())
        } catch (e: Exception) {
            return ResultModel.errorResult(input.authorId, e.message)
        }
    }

    class RequestModel(val authorId: String, val keywords: List<String>) : UseCase.RequestModel

    class ResultModel(val notes: List<NoteViewModel>, override val successful: Boolean, override val message: String) :
        UseCase.ResultModel {

        // TODO in real life, status message would be either localized here
        //  or have an id to be translated in consuming instance (e.g. frontend)
        companion object FactoryMethods {
            fun successResult(notes: List<NoteViewModel>, authorId: String): ResultModel {
                return ResultModel(notes, true, "Successfully retrieved notes for author'$authorId'")
            }

            fun errorResult(authorId: String?, errorReason: String?): ResultModel {
                val effectiveAuthorId =
                    if (!authorId.isNullOrEmpty()) authorId else "<null or empty author id>"
                val effectiveErrorReason =
                    if (!errorReason.isNullOrEmpty()) errorReason else "<no error reason provided>"
                return ResultModel(
                    emptyList(),
                    false,
                    "Unable to retrieves notes for author '$effectiveAuthorId' because of error: $effectiveErrorReason"
                )
            }
        }
    }
}
