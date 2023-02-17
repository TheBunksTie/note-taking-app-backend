package com.thermondo.application.plugin

import com.thermondo.infrastructure.authentication.JwtWebTokenAuthenticator
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.jwt
import org.koin.ktor.ext.inject

fun Application.configureAuthentication() {
    val jwtAuthenticator by inject<JwtWebTokenAuthenticator>()

    install(Authentication) {
        jwt("jwt-auth") {
            jwtAuthenticator.configureJwtBasedAuthentication(this)
        }
    }
}
