package com.thermondo.application.user

import com.thermondo.domain.user.User
import com.thermondo.usecase.user.CreateUser
import com.thermondo.usecase.user.LoginUser

/**
 * Central point of interaction with all [User] related use cases for presentation layer
 */
class UserService(private val createUser: CreateUser, private val loginUser: LoginUser) {

    fun createUser(userName: String, password: String, passwordRepetition: String): CreateUser.ResultModel {
        val request = CreateUser.RequestModel(userName, password, passwordRepetition)
        return createUser.execute(request)
    }

    fun loginUser(userName: String, password: String): LoginUser.ResultModel {
        val request = LoginUser.RequestModel(userName, password)
        return loginUser.execute(request)
    }
}
