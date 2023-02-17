package com.thermondo.persistence.note.abstraction

import com.thermondo.persistence.abstraction.IPersistenceRepository
import com.thermondo.persistence.note.NotePersistenceEntity

interface INotePersistenceRepository : IPersistenceRepository<NotePersistenceEntity> {

    fun getAllByAuthor(authorId: String): List<NotePersistenceEntity>

    fun getAllByTags(authorId: String, tags: Set<String>): List<NotePersistenceEntity>

    fun getAllByKeywords(authorId: String, keywords: Set<String>): List<NotePersistenceEntity>

    fun getAllPublic(): List<NotePersistenceEntity>
}
