package com.thermondo

import com.thermondo.application.plugin.configureAuthentication
import com.thermondo.application.plugin.configureDependencyInjection
import com.thermondo.application.plugin.configureRouting
import com.thermondo.application.plugin.configureSerialization
import io.ktor.server.application.Application

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

// NOTE: Referenced in application.conf
@Suppress("unused")
fun Application.module() {
    configureDependencyInjection()
    configureSerialization()
    configureAuthentication()
    configureRouting()
}
