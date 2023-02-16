package com.thermondo.persistence.user

import com.thermondo.domain.model.Id
import com.thermondo.persistence.user.abstraction.IUserPersistenceRepository

/**
 * In memory repository to store [UserPersistenceEntity]
 * Not to be used for production use, only for demonstration purposes
 */
class UserPersistenceRepository : IUserPersistenceRepository {

    private val userMap : MutableMap<String, UserPersistenceEntity> = HashMap()

    override fun persist(persistableObject: UserPersistenceEntity): UserPersistenceEntity {
        userMap[persistableObject.id] = persistableObject
        return persistableObject
    }

    override fun delete(id: Id): Boolean {
        userMap.remove(id.toString()) ?: return false
        return true
    }

    override fun exists(userName: String): Boolean {
        return userMap.values.firstOrNull { u -> u.userName == userName } != null
    }

    override fun exists(id: Id): Boolean {
        return userMap.containsKey(id.toString())
    }

    override fun getBy(userName: String): UserPersistenceEntity? {
        return userMap.values.firstOrNull { u -> u.userName == userName }
    }

    override fun getBy(id: Id): UserPersistenceEntity? {
        return userMap[id.toString()]
    }

    override fun getAll(): List<UserPersistenceEntity> {
        return userMap.values.stream().toList()
    }
}
