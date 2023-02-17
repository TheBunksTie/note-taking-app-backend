package com.thermondo.application.user

import com.thermondo.domain.user.User
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * DI registrations for all auto-injected classes/interfaces related to [User] service
 */
val userApplicationModule = module {
    singleOf(::UserService)
}
