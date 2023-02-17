package com.thermondo.persistence.note

import com.thermondo.domain.model.ChangedAt
import com.thermondo.domain.model.CreatedAt
import com.thermondo.domain.model.Id
import com.thermondo.domain.note.*
import com.thermondo.persistence.abstraction.IDomainPersistenceConverter
import com.thermondo.persistence.common.DomainPersistenceConverterBase
import com.thermondo.persistence.user.IUserDomainPersistenceConverter

/**
 * Maker interface for converter, to convert between [Note] and its corresponding [NotePersistenceEntity]
 */
interface INoteDomainPersistenceConverter : IDomainPersistenceConverter<Note, NotePersistenceEntity>

/**
 * Two-way converter between [Note] and its corresponding [NotePersistenceEntity]
 */
class NoteDomainPersistenceConverter(private val userConverter: IUserDomainPersistenceConverter) :
    DomainPersistenceConverterBase<Note, NotePersistenceEntity>(), INoteDomainPersistenceConverter {

    override fun createInstanceOfT2ByT1(source: Note): NotePersistenceEntity {
        try {
            return NotePersistenceEntity(
                source.id.toString(),
                source.createdAt.toString(),
                source.changedAt.toString(),
                source.title.toString(),
                source.body.toString(),
                source.tags.stream().map { t -> t.value }.toList().toSet(),
                userConverter.createFromT1(source.author),
                source.noteType.name
            )
        } catch (e: Exception) {
            throw PersistenceConversionException(e)
        }
    }

    override fun createInstanceOfT1ByT2(source: NotePersistenceEntity): Note {
        try {
            return Note(
                Id.fromString(source.id),
                CreatedAt.fromString(source.createdAt),
                ChangedAt.fromString(source.changedAt),
                Title(source.title),
                Body(source.body),
                source.tags.stream().map { t -> Tag(t) }.toList().toSet(),
                userConverter.createFromT2(source.author),
                NoteType.valueOf(source.noteType)
            )
        } catch (e: Exception) {
            throw PersistenceConversionException(e)
        }
    }
}
