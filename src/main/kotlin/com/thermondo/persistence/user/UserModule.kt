package com.thermondo.persistence.user

import com.thermondo.domain.user.User
import com.thermondo.persistence.user.abstraction.IUserPersistenceRepository
import com.thermondo.usecase.user.abstraction.IUserRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * DI registrations for all auto-injected classes/interfaces related to persistence of [User]
 */
val userPersistenceModule = module {
    singleOf(::UserDomainPersistenceConverter) { bind<IUserDomainPersistenceConverter>() }
    singleOf(::UserPersistenceRepository) { bind<IUserPersistenceRepository>() }
    singleOf(::UserRepository) { bind<IUserRepository>() }
}
