package com.thermondo.persistence.note

import com.thermondo.domain.note.Note
import com.thermondo.persistence.abstraction.IDomainPersistenceConverter
import com.thermondo.persistence.note.abstraction.INotePersistenceRepository
import com.thermondo.usecase.note.abstraction.INoteRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * DI registrations for all auto-injected classes/interfaces related to persistence of [Note]
 */
val notePersistenceModule = module {
    singleOf(::NoteDomainPersistenceConverter) { bind<IDomainPersistenceConverter<Note, NotePersistenceEntity>>() }
    singleOf(::NotePersistenceRepository) { bind<INotePersistenceRepository>() }
    singleOf(::NoteRepository) { bind<INoteRepository>() }
}
