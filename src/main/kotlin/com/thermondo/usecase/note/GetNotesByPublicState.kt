package com.thermondo.usecase.note

import com.thermondo.domain.note.Note
import com.thermondo.domain.user.User
import com.thermondo.usecase.common.UseCase
import com.thermondo.usecase.note.abstraction.INoteRepository
import com.thermondo.usecase.note.viewmodel.INoteDomainViewModelConverter
import com.thermondo.usecase.note.viewmodel.NoteViewModel
import com.thermondo.usecase.user.abstraction.IUserRepository

/**
 * Use case class to retrieve a list of existing notes which are public
 * @param noteRepository repository to store and retrieve [Note] objects
 * @param userRepository repository to store and retrieve [User] objects
 * @param viewModelConverter a one-way converter between [Note] and [NoteViewModel]
 */
class GetNotesByPublicState(
    private val noteRepository: INoteRepository,
    private val userRepository: IUserRepository,
    private val viewModelConverter: INoteDomainViewModelConverter
) :
    UseCase<GetNotesByPublicState.RequestModel, GetNotesByPublicState.ResultModel>() {

    @Suppress("LiftReturnOrAssignment")
    override fun execute(input: RequestModel): ResultModel {
        try {
            val publicNotes = noteRepository
                .getAllPublic()
                .stream().map { n -> viewModelConverter.createFromT1(n) }.toList()

            return ResultModel.successResult(publicNotes)
        } catch (e: Exception) {
            return ResultModel.errorResult(e.message)
        }
    }

    class RequestModel : UseCase.RequestModel

    class ResultModel(val notes: List<NoteViewModel>, override val successful: Boolean, override val message: String) :
        UseCase.ResultModel {

        // TODO in real life, status message would be either localized here
        //  or have an id to be translated in consuming instance (e.g. frontend)
        companion object FactoryMethods {
            fun successResult(notes: List<NoteViewModel>): ResultModel {
                return ResultModel(notes, true, "Successfully retrieved public notes")
            }
            fun errorResult(errorReason: String?): ResultModel {
                val effectiveErrorReason = if (!errorReason.isNullOrEmpty()) errorReason else "<no error reason provided>"
                return ResultModel(emptyList(), false, "Unable to retrieves public notes because of error: $effectiveErrorReason")
            }
        }
    }
}
