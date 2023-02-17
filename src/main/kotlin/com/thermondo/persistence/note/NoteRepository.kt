package com.thermondo.persistence.note

import com.thermondo.domain.model.Id
import com.thermondo.domain.note.Keyword
import com.thermondo.domain.note.Note
import com.thermondo.domain.note.Tag
import com.thermondo.persistence.abstraction.IDomainPersistenceConverter
import com.thermondo.persistence.common.RepositoryBase
import com.thermondo.persistence.note.abstraction.INotePersistenceRepository
import com.thermondo.usecase.note.abstraction.INoteRepository

/**
 * Configured implementation of a repository to store a [Note] via a [NotePersistenceEntity]
 * @property converter a two-way converter between [Note] and [NotePersistenceEntity]
 * @property persistenceRepository actual repository to store [NotePersistenceEntity] in
 */
class NoteRepository(
    converter: IDomainPersistenceConverter<Note, NotePersistenceEntity>,
    persistenceRepository: INotePersistenceRepository
) :
    RepositoryBase<Note, NotePersistenceEntity>(converter, persistenceRepository), INoteRepository {

    override fun getAllByAuthor(authorId: Id): List<Note> {
        val authorIdString = authorId.toString()

        return (persistenceRepository as INotePersistenceRepository)
            .getAllByAuthor(authorIdString)
            .stream().map { n -> converter.createFromT2(n) }
            .toList().toList()
    }

    override fun getAllByTags(authorId: Id, tags: Set<Tag>): List<Note> {
        val authorIdString = authorId.toString()
        val tagStringSet = tags.stream().map { t -> t.toString() }.toList().toSet()

        return (persistenceRepository as INotePersistenceRepository)
            .getAllByTags(authorIdString, tagStringSet)
            .stream().map { n -> converter.createFromT2(n) }
            .toList().toList()
    }

    override fun getAllByKeywords(authorId: Id, keywords: Set<Keyword>): List<Note> {
        val authorIdString = authorId.toString()
        val keywordsStringSet = keywords.stream().map { t -> t.toString() }.toList().toSet()

        return (persistenceRepository as INotePersistenceRepository)
            .getAllByKeywords(authorIdString, keywordsStringSet)
            .stream().map { n -> converter.createFromT2(n) }
            .toList().toList()
    }

    override fun getAllPublic(): List<Note> {
        return (persistenceRepository as INotePersistenceRepository)
            .getAllPublic()
            .stream().map { n -> converter.createFromT2(n) }
            .toList().toList()
    }
}
