package com.thermondo.usecase.note.viewmodel

import com.thermondo.domain.note.Note
import com.thermondo.usecase.abstraction.IDomainViewModelConverter
import com.thermondo.usecase.common.viewmodel.DomainViewModelConverterBase
import com.thermondo.usecase.common.viewmodel.IdViewModel

/**
 * Maker interface for converter, to convert between [Note] and its corresponding [NoteViewModel]
 */
interface INoteDomainViewModelConverter : IDomainViewModelConverter<Note, NoteViewModel>

/**
 * One-way converter between [Note] and its corresponding [NoteViewModel]
 */
class NoteViewModelConverter : DomainViewModelConverterBase<Note, NoteViewModel>(), INoteDomainViewModelConverter {

    override fun createInstanceOfT2ByT1(source: Note): NoteViewModel {
        try {
            return NoteViewModel(
                source.id.toString(),
                source.createdAt.toString(),
                source.changedAt.toString(),
                source.title.toString(),
                source.body.toString(),
                source.tags.map { t -> t.value }.toSet(),
                IdViewModel(source.author.id.toString()),
                source.noteType.name
            )
        } catch (e: Exception) {
            throw UseCaseConversionException(e)
        }
    }

    override fun createInstanceOfT1ByT2(source: NoteViewModel): Note {
        // TODO to transform the authorId from view model to actual author object
        // user repository would be needed
        throw NotImplementedError("Currently not required")
    }
}
