package com.thermondo.usecase.user.viewmodel

import com.thermondo.usecase.abstraction.IViewModel
import com.thermondo.usecase.user.LoginUser
import kotlinx.serialization.Serializable

/**
 * Simple view model representation for use case [LoginUser]
 * @param userName user to log in
 * @param password password for authentication
 */
@Serializable
data class LoginUserViewModel(val userName: String, val password: String) : IViewModel
