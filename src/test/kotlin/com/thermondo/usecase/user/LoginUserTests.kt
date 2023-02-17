package com.thermondo.usecase.user

import com.thermondo.domain.model.Id
import com.thermondo.domain.user.Password
import com.thermondo.domain.user.User
import com.thermondo.domain.user.UserName
import com.thermondo.usecase.abstraction.IAuthenticator
import com.thermondo.usecase.common.UseCaseTestBase
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LoginUserTests : UseCaseTestBase() {

    private val authenticatorMock = object : IAuthenticator {
        override fun generateToken(userId: Id): String {
            return "<generated token>"
        }
    }
    @Test
    fun execute_userNameNull_returnsErrorResultModel() {
        val password = "seCreT123"
        val requestModel = LoginUser.RequestModel(null, password)

        val loginUser = LoginUser(userRepository, authenticatorMock)
        val resultModel = loginUser.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.userId)
        assertContains(resultModel.message, "Username must not be null")
    }

    @Test
    fun execute_passwordNull_returnsErrorResultModel() {
        val userName = "user1"
        val requestModel = LoginUser.RequestModel(userName, null)

        val loginUser = LoginUser(userRepository, authenticatorMock)
        val resultModel = loginUser.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.userId)
        assertContains(resultModel.message, userName)
        assertContains(resultModel.message, "Password must not be null")
    }

    @Test
    fun execute_nonExistingUserName_returnsErrorResultModel() {
        val userName = "user1"
        val password = "seCreT123"
        val requestModel = LoginUser.RequestModel(userName, password)

        val loginUser = LoginUser(userRepository, authenticatorMock)
        val resultModel = loginUser.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.userId)
        assertContains(resultModel.message, userName)
        assertContains(resultModel.message, "Invalid username/password data")
    }

    @Test
    fun execute_incorrectPassword_returnsErrorResultModel() {
        val userName = "user1"
        val password = "seCreT123"
        val existingUser = userRepository.persist(User.new(UserName(userName), Password(password)))

        val requestModel = LoginUser.RequestModel(existingUser.userName.value, "incorrectPassword")

        val loginUser = LoginUser(userRepository, authenticatorMock)
        val resultModel = loginUser.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.userId)
        assertContains(resultModel.message, userName)
        assertContains(resultModel.message, "Invalid username/password data")
    }

    @Test
    fun execute_validParameters_returnsSuccessResultModel() {
        val userName = "user1"
        val password = "seCreT123"
        val existingUser = userRepository.persist(User.new(UserName(userName), Password(password)))

        val requestModel = LoginUser.RequestModel(existingUser.userName.value, existingUser.password.value)

        val loginUser = LoginUser(userRepository, authenticatorMock)
        val resultModel = loginUser.execute(requestModel)

        assertTrue(resultModel.successful)
        assertNotNull(resultModel.userId)
        assertContains(resultModel.message, userName)
        assertContains(resultModel.message, "Successfully logged in")
    }
}
