package com.thermondo.usecase.user.viewmodel

import com.thermondo.usecase.abstraction.IViewModel
import com.thermondo.usecase.user.CreateUser
import kotlinx.serialization.Serializable

/**
 * Simple view model representation for use case [CreateUser]
 * @param userName user to create
 * @param password password to set for user
 * @param passwordRepetition repetition of password to ensure correctness
 */
@Serializable
data class CreateUserViewModel(val userName: String, val password: String, val passwordRepetition: String) : IViewModel
