package com.thermondo.infrastructure

import com.thermondo.infrastructure.authentication.JwtWebTokenAuthenticator
import com.thermondo.usecase.abstraction.IAuthenticator
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * DI registrations for all auto-injected classes/interfaces related to infrastructure concerns
 */
val infrastructureModule = module {
    singleOf(::JwtWebTokenAuthenticator) { bind<IAuthenticator>() }
}
