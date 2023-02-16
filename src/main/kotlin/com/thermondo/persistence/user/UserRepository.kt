package com.thermondo.persistence.user

import com.thermondo.domain.user.User
import com.thermondo.domain.user.UserName
import com.thermondo.persistence.common.RepositoryBase
import com.thermondo.persistence.user.abstraction.IUserPersistenceRepository
import com.thermondo.usecase.user.abstraction.IUserRepository

/**
 * Configured implementation of a repository to store a [User] via a [UserPersistenceEntity]
 * @property converter a two-way converter between [User] and [UserPersistenceEntity]
 * @property persistenceRepository actual repository to store [UserPersistenceEntity] in
 */
class UserRepository(converter: IUserDomainPersistenceConverter,
                     persistenceRepository: IUserPersistenceRepository) :
    RepositoryBase<User, UserPersistenceEntity>(converter, persistenceRepository), IUserRepository {

    override fun exists(userName: UserName): Boolean {
       return (persistenceRepository as IUserPersistenceRepository).exists(userName.value)
    }

    override fun getBy(userName: UserName): User? {
        val persistenceEntity = (persistenceRepository as IUserPersistenceRepository).getBy(userName.value) ?: return null
        return converter.createFromT2(persistenceEntity)
    }
}
