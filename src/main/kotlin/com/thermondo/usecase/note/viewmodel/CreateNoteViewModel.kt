package com.thermondo.usecase.note.viewmodel

import com.thermondo.usecase.abstraction.IViewModel
import com.thermondo.usecase.note.CreateNote
import kotlinx.serialization.Serializable

/**
 * Simple view model representation for use case [CreateNote]
 * @param title the title of the note
 * @param body the body of the note
 * @param tags the list of tags of the note
 * @param noteType the type of the note
 */
@Serializable
data class CreateNoteViewModel(
    val title: String?,
    val body: String?,
    val tags: List<String>,
    val noteType: String
) : IViewModel
