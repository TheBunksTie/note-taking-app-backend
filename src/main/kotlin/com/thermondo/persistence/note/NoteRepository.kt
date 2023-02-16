package com.thermondo.persistence.note

import com.thermondo.domain.note.Note
import com.thermondo.persistence.abstraction.IDomainPersistenceConverter
import com.thermondo.persistence.abstraction.IPersistenceRepository
import com.thermondo.persistence.common.RepositoryBase
import com.thermondo.usecase.note.abstraction.INoteRepository

/**
 * Configured implementation of a repository to store a [Note] via a [NotePersistenceEntity]
 * @property converter a two-way converter between [Note] and [NotePersistenceEntity]
 * @property persistenceRepository actual repository to store [NotePersistenceEntity] in
 */
class NoteRepository(converter: IDomainPersistenceConverter<Note, NotePersistenceEntity>,
                     persistenceRepository: IPersistenceRepository<NotePersistenceEntity>) :
    RepositoryBase<Note, NotePersistenceEntity>(converter, persistenceRepository), INoteRepository
