package com.thermondo.persistence.user

import com.thermondo.domain.model.ChangedAt
import com.thermondo.domain.model.CreatedAt
import com.thermondo.domain.model.Id
import com.thermondo.domain.user.Password
import com.thermondo.domain.user.User
import com.thermondo.domain.user.UserName
import com.thermondo.persistence.abstraction.IDomainPersistenceConverter
import com.thermondo.persistence.common.DomainPersistenceConverterBase

/**
 * Maker interface for converter, to convert between [User] and its corresponding [UserPersistenceEntity]
 */
interface IUserDomainPersistenceConverter : IDomainPersistenceConverter<User, UserPersistenceEntity>

/**
 * Two-way converter between [User] and its corresponding [UserPersistenceEntity]
 */
class UserDomainPersistenceConverter
    : DomainPersistenceConverterBase<User, UserPersistenceEntity>(), IUserDomainPersistenceConverter {

    override fun createInstanceOfT2ByT1(source: User): UserPersistenceEntity {
        // TODO in real world application, password must be encrypted when persisted
        // via injected IEncryptionManager.encrypt

        try {
            return UserPersistenceEntity(
                source.id.toString(),
                source.createdAt.toString(),
                source.changedAt.toString(),
                source.userName.toString(),
                source.password.toString()
            )
        } catch (e: Exception) {
            throw PersistenceConversionException(e)
        }
    }

    override fun createInstanceOfT1ByT2(source: UserPersistenceEntity): User {
        // TODO in real world application, password must be decrypted when retrieved from persistence source
        // via injected IEncryptionManager.decrypt

        try {
            return User(
                Id.fromString(source.id),
                CreatedAt.fromString(source.createdAt),
                ChangedAt.fromString(source.changedAt),
                UserName(source.userName),
                Password(source.password)
            )
        } catch (e: Exception) {
            throw PersistenceConversionException(e)
        }
    }
}
