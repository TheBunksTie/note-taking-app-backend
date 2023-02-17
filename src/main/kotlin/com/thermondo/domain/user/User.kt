package com.thermondo.domain.user

import com.thermondo.domain.model.ChangedAt
import com.thermondo.domain.model.CreatedAt
import com.thermondo.domain.model.DomainEntity
import com.thermondo.domain.model.Id

/**
 * Domain entity representing a user with main properties [userName], [password]
 * @param userName the username of the user
 * @param password the password of the user
 */
class User(
    id: Id,
    createdAt: CreatedAt,
    changedAt: ChangedAt,
    var userName: UserName,
    var password: Password
) : DomainEntity(id, createdAt, changedAt) {

    companion object FactoryMethods {
        fun new(userName: UserName, password: Password): User {
            return User(Id.generate(), CreatedAt.now(), ChangedAt.now(), userName, password)
        }
    }
}
