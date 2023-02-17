package com.thermondo.application.plugin

import com.thermondo.application.note.noteApplicationModule
import com.thermondo.application.user.userApplicationModule
import com.thermondo.infrastructure.infrastructureModule
import com.thermondo.persistence.note.notePersistenceModule
import com.thermondo.persistence.user.userPersistenceModule
import com.thermondo.usecase.note.noteUseCaseModule
import com.thermondo.usecase.user.userUseCaseModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection() {
    install(Koin) {
        slf4jLogger()
        modules(
            userPersistenceModule,
            notePersistenceModule,
            userUseCaseModule,
            noteUseCaseModule,
            userApplicationModule,
            noteApplicationModule,
            infrastructureModule
        )
    }
}
