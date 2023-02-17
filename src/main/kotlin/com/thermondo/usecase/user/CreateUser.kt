package com.thermondo.usecase.user

import com.thermondo.domain.user.Password
import com.thermondo.domain.user.User
import com.thermondo.domain.user.UserName
import com.thermondo.usecase.common.UseCase
import com.thermondo.usecase.user.abstraction.IUserRepository

/**
 * Use case class to create new users
 * @param userRepository repository to store and retrieve [User] objects
 */
class CreateUser(private val userRepository: IUserRepository)
    : UseCase<CreateUser.RequestModel, CreateUser.ResultModel>() {

    // TODO inject IHashProvider for hashing the pw before storage

    override fun execute(input: RequestModel): ResultModel {
        try {
            if (input.userName == null)
                return ResultModel.errorResult(null, "Username must not be null")

            if (input.password == null)
                return ResultModel.errorResult(input.userName, "Password must not be null")

            if (input.passwordRepetition == null)
                return ResultModel.errorResult(input.userName, "Password repetition must not be null")

            if (input.password != input.passwordRepetition)
                return ResultModel.errorResult(input.userName, "Password and password repetition do not match")

            val userName = UserName(input.userName)
            val isExistingUser = userRepository.exists(userName)
            if (isExistingUser)
                return ResultModel.errorResult(userName.value, "User name already exists")

            val password = Password(input.password)
            val newUser = User.new(userName, password)
            val persistedNewUser = userRepository.persist(newUser)

            return ResultModel.successResult(persistedNewUser.id.toString(), persistedNewUser.userName.toString())
        } catch (e: Exception) {
          return ResultModel.errorResult(input.userName, e.message)
        }
    }

    class RequestModel(val userName: String?, val password: String?, val passwordRepetition: String?)
        : UseCase.RequestModel

    class ResultModel(val userId: String?, override val successful: Boolean, override val message: String)
        : UseCase.ResultModel {
        // TODO in real life, status message would be either localized here
        //  or have an id to be translated in consuming instance (e.g. frontend)
        companion object FactoryMethods {
            fun successResult(userId: String, userName: String) : ResultModel {
                return ResultModel(userId, true, "Successfully created user '$userName'")
            }

            fun errorResult(userName: String?, errorReason: String?) : ResultModel {
                val effectiveUserName = if (!userName.isNullOrEmpty()) userName else "<null or empty user name>"
                val effectiveErrorReason = if (!errorReason.isNullOrEmpty()) errorReason else "<no error reason provided>"
                return ResultModel(null, false, "Unable to create user '$effectiveUserName' because of error: $effectiveErrorReason")
            }
        }
    }
}
