package com.thermondo.usecase.user

import com.thermondo.domain.user.User
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * DI registrations for all auto-injected classes/interfaces related to use cases of [User]
 */
val userUseCaseModule = module {
    singleOf(::CreateUser)
    singleOf(::LoginUser)
}
