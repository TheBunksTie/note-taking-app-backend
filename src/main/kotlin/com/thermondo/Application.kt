package com.thermondo

import com.thermondo.application.plugin.configureAuthentication
import com.thermondo.application.plugin.configureDependencyInjection
import com.thermondo.application.plugin.configureRouting
import com.thermondo.application.plugin.configureSerialization
import com.thermondo.application.routes.noteRouting
import com.thermondo.application.routes.userRouting
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

// NOTE: Referenced in application.conf
@Suppress("unused")
fun Application.module() {
    configureRouting()
    configureSerialization()
    configureDependencyInjection()
    configureAuthentication()

    routing {
        userRouting()
        noteRouting()
    }
}
