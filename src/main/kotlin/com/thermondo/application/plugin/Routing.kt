package com.thermondo.application.plugin

import com.thermondo.application.routes.noteRouting
import com.thermondo.application.routes.userRouting
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        userRouting()
        noteRouting()
    }
}
