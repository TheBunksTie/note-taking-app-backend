package com.thermondo.persistence.user.abstraction

import com.thermondo.persistence.abstraction.IPersistenceRepository
import com.thermondo.persistence.user.UserPersistenceEntity

/**
 * Extension interface to default repository to add more actions for accessing
 * the [UserPersistenceEntity] data
 */
interface IUserPersistenceRepository : IPersistenceRepository<UserPersistenceEntity> {
    fun exists(userName: String): Boolean
    fun getBy(userName: String): UserPersistenceEntity?
}
