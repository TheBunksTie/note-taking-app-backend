package com.thermondo.usecase.user

import com.thermondo.domain.user.User
import com.thermondo.domain.user.UserName
import com.thermondo.usecase.abstraction.IAuthenticator
import com.thermondo.usecase.common.UseCase
import com.thermondo.usecase.common.viewmodel.AuthenticationTokenViewModel
import com.thermondo.usecase.user.abstraction.IUserRepository

/**
 * Use case class to log current users in
 * @param userRepository repository to store and retrieve [User] objects
 */
class LoginUser(
    private val userRepository: IUserRepository,
    private val authenticator: IAuthenticator
) :
    UseCase<LoginUser.RequestModel, LoginUser.ResultModel>() {

    override fun execute(input: RequestModel): ResultModel {
        try {
            if (input.userName == null)
                return ResultModel.errorResult("<not provided>", "Username must not be null")

            if (input.password == null)
                return ResultModel.errorResult(input.userName, "Password must not be null")

            val userName = UserName(input.userName)
            val existingUser = userRepository.getBy(userName)
                ?: return ResultModel.errorResult(userName.value, "Invalid username/password data")

            if (input.password != existingUser.password.value)
                return ResultModel.errorResult(input.userName, "Invalid username/password data")

            val authenticationToken = authenticator.generateToken(existingUser.id)
            return ResultModel.successResult(existingUser.id.toString(), existingUser.userName.toString(), authenticationToken)
        } catch (e: Exception) {
            return ResultModel.errorResult(input.userName, e.message)
        }
    }

    class RequestModel(val userName: String?, val password: String?) : UseCase.RequestModel

    class ResultModel(
        val userId: String?,
        val userName: String?,
        val authenticationToken: AuthenticationTokenViewModel?,
        override val successful: Boolean,
        override val message: String
    ) :
        UseCase.ResultModel {

        // TODO in real life, status message would be either localized here
        //  or have an id to be translated in consuming instance (e.g. frontend)
        companion object FactoryMethods {
            fun successResult(userId: String, userName: String, authenticationToken: String): ResultModel {
                return ResultModel(userId, userName, AuthenticationTokenViewModel(authenticationToken), true, "Successfully logged in user '$userName'")
            }

            fun errorResult(userName: String?, errorReason: String?): ResultModel {
                val effectiveUserName = if (!userName.isNullOrEmpty()) userName else "<null or empty user name>"
                val effectiveErrorReason = if (!errorReason.isNullOrEmpty()) errorReason else "<no error reason provided>"
                return ResultModel(null, effectiveUserName, null, false, "Unable to login user '$effectiveUserName' because of error: $effectiveErrorReason")
            }
        }
    }
}
