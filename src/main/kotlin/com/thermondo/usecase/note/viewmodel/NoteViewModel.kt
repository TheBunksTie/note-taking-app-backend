package com.thermondo.usecase.note.viewmodel

import com.thermondo.domain.note.Note
import com.thermondo.usecase.abstraction.IDomainViewModel
import com.thermondo.usecase.common.viewmodel.IdViewModel
import kotlinx.serialization.Serializable

/**
 * View model representation for [Note]
 */
@Serializable
data class NoteViewModel(
    val id: String,
    val createdAt: String,
    val changedAt: String,
    val title: String,
    val body: String?,
    val tags: Set<String>,
    val author: IdViewModel,
    val noteType: String) : IDomainViewModel
