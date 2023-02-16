package com.thermondo.persistence.note

import com.thermondo.domain.model.Id
import com.thermondo.persistence.abstraction.IPersistenceRepository

/**
 * In memory repository to store [NotePersistenceEntity]
 * Not to be used for production use, only for demonstration purposes
 */
class NotePersistenceRepository : IPersistenceRepository<NotePersistenceEntity> {

    private val noteMap : MutableMap<String, NotePersistenceEntity> = HashMap()

    override fun persist(persistableObject: NotePersistenceEntity): NotePersistenceEntity {
        noteMap[persistableObject.id] = persistableObject
        return persistableObject
    }

    override fun delete(id: Id): Boolean {
        noteMap.remove(id.toString()) ?: return false
        return true
    }

    override fun exists(id: Id): Boolean {
        return noteMap.containsKey(id.toString())
    }

    override fun getBy(id: Id): NotePersistenceEntity? {
        return noteMap[id.toString()]
    }

    override fun getAll(): List<NotePersistenceEntity> {
        return noteMap.values.stream().toList()
    }
}
