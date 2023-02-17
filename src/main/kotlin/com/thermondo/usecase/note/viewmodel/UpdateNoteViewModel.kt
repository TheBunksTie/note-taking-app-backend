package com.thermondo.usecase.note.viewmodel

import com.thermondo.usecase.abstraction.IViewModel
import com.thermondo.usecase.note.UpdateNote
import kotlinx.serialization.Serializable

/**
 * Simple view model representation for use case [UpdateNote]
 * @param title the title of the note
 * @param body the body of the note
 * @param tags the list of tags of the note
 * @param noteType the type of the note
 */
@Serializable
data class UpdateNoteViewModel(
    val id: String,
    val title: String?,
    val body: String?,
    val tags: List<String>,
    val noteType: String
) : IViewModel
