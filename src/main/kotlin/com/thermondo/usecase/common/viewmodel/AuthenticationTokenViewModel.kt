package com.thermondo.usecase.common.viewmodel

import com.thermondo.usecase.abstraction.IViewModel
import kotlinx.serialization.Serializable

/**
 * Simple view model representation for a string based authentication token
 */
@Serializable
data class AuthenticationTokenViewModel(val authenticationToken: String) : IViewModel
