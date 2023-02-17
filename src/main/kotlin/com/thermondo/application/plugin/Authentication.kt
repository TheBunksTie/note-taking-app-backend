package com.thermondo.application.plugin

import com.thermondo.infrastructure.authentication.JwtWebTokenAuthenticator
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject

fun Application.configureAuthentication() {
    val jwtAuthenticator by inject<JwtWebTokenAuthenticator>()

    install(Authentication) {
        jwt("jwt-auth") {
            jwtAuthenticator.configureJwtBasedAuthentication(this)
        }
    }
}
