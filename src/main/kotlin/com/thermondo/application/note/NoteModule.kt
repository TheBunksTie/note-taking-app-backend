package com.thermondo.application.note

import com.thermondo.domain.note.Note
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * DI registrations for all auto-injected classes/interfaces related to [Note] service
 */
val noteApplicationModule = module {
    singleOf(::NoteService)
}
