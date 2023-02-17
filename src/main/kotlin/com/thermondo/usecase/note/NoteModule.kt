package com.thermondo.usecase.note

import com.thermondo.domain.note.Note
import com.thermondo.usecase.note.viewmodel.INoteDomainViewModelConverter
import com.thermondo.usecase.note.viewmodel.NoteViewModelConverter
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * DI registrations for all auto-injected classes/interfaces related to use cases of [Note]
 */
val noteUseCaseModule = module {
    singleOf(::NoteViewModelConverter) { bind<INoteDomainViewModelConverter>() }
    singleOf(::CreateNote)
    singleOf(::DeleteNote)
    singleOf(::UpdateNote)
    singleOf(::GetNotesByAuthor)
    singleOf(::GetNotesByTags)
    singleOf(::GetNotesByKeywords)
    singleOf(::GetNotesByPublicState)
}
