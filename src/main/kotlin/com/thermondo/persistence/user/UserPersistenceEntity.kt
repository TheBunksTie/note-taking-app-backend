package com.thermondo.persistence.user

import com.thermondo.persistence.common.PersistenceEntityBase

/**
 * Persistence entity representing a user with main properties [userName], [password]
 * mirroring the actual domain entity user
 * @param userName the username of the user
 * @param password the password of the user*
 */
class UserPersistenceEntity(
    id: String,
    createdAt: String,
    changedAt: String,
    val userName: String,
    val password: String
) :
    PersistenceEntityBase(id, createdAt, changedAt)
// TODO id and createdAt / changedAt could be more fitting data types in an actual DB
