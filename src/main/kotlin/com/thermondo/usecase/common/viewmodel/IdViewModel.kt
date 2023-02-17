package com.thermondo.usecase.common.viewmodel

import com.thermondo.domain.model.Id
import com.thermondo.usecase.abstraction.IViewModel
import kotlinx.serialization.Serializable

/**
 * Simple view model representation for [Id]
 */
@Serializable
data class IdViewModel(val id: String) : IViewModel
