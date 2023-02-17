package com.thermondo

import com.thermondo.application.plugin.configureAuthentication
import com.thermondo.application.plugin.configureDependencyInjection
import com.thermondo.application.plugin.configureRouting
import com.thermondo.application.plugin.configureSerialization
import com.thermondo.application.routes.noteRouting
import com.thermondo.application.routes.userRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

// NOTE: Referenced in application.conf
@Suppress("unused")
fun Application.module(testing: Boolean = false) {
    configureRouting()
    configureSerialization()
    configureDependencyInjection()
    configureAuthentication()

    routing {
        userRouting()
        noteRouting()
    }
}
