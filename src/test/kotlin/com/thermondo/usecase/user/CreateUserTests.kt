package com.thermondo.usecase.user

import com.thermondo.domain.user.Password
import com.thermondo.domain.user.User
import com.thermondo.domain.user.UserName
import com.thermondo.persistence.user.userPersistenceModule
import com.thermondo.usecase.user.abstraction.IUserRepository
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import kotlin.test.*
import kotlin.test.assertNotNull

class CreateUserTests : KoinTest {
    private val userRepository: IUserRepository by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(userPersistenceModule)
    }

    @Test
    fun execute_userNameNull_returnsErrorResultModel() {
        val password = "seCreT123"
        val requestModel = CreateUser.RequestModel(null, password, password)

        val createUser = CreateUser(userRepository)
        val resultModel = createUser.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.userId)
        assertContains(resultModel.message, "Username must not be null")
    }

    @Test
    fun execute_passwordNull_returnsErrorResultModel() {
        val userName = "user1"
        val requestModel = CreateUser.RequestModel(userName, null, null)

        val createUser = CreateUser(userRepository)
        val resultModel = createUser.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.userId)
        assertContains(resultModel.message, userName)
        assertContains(resultModel.message, "Password must not be null")
    }

    @Test
    fun execute_passwordRepetitionNull_returnsErrorResultModel() {
        val userName = "user1"
        val password = "seCreT123"
        val requestModel = CreateUser.RequestModel(userName, password, null)

        val createUser = CreateUser(userRepository)
        val resultModel = createUser.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.userId)
        assertContains(resultModel.message, userName)
        assertContains(resultModel.message, "Password repetition must not be null")
    }

    @Test
    fun execute_nonMatchingPasswords_returnsErrorResultModel() {
        val userName = "user1"
        val password = "seCreT123"
        val passwordRepetition = "SeCreT123"
        val requestModel = CreateUser.RequestModel(userName, password, passwordRepetition)

        val createUser = CreateUser(userRepository)
        val resultModel = createUser.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.userId)
        assertContains(resultModel.message, userName)
        assertContains(resultModel.message, "Password and password repetition do not match")
    }

    @Test
    fun execute_invalidPassword_returnsErrorResultModel() {
        val userName = "user1"
        val password = "secret"
        val requestModel = CreateUser.RequestModel(userName, password, password)

        val createUser = CreateUser(userRepository)
        val resultModel = createUser.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.userId)
        assertContains(resultModel.message, userName)
        assertContains(resultModel.message, "Password must contain at least one upper case letter")
    }

    @Test
    fun execute_existingUserName_returnsErrorResultModel() {
        val userName = "user1"
        val password = "seCreT123"
        val existingUser = userRepository.persist(User.new(UserName(userName), Password(password)))

        val requestModel = CreateUser.RequestModel(existingUser.userName.value, password, password)

        val createUser = CreateUser(userRepository)
        val resultModel = createUser.execute(requestModel)

        assertFalse(resultModel.successful)
        assertNull(resultModel.userId)
        assertContains(resultModel.message, userName)
        assertContains(resultModel.message, "User name already exists")
    }

    @Test
    fun execute_validParameters_returnsSuccessResultModel() {
        val userName = "user1"
        val password = "seCreT123"
        val requestModel = CreateUser.RequestModel(userName, password, password)

        val createUser = CreateUser(userRepository)
        val resultModel = createUser.execute(requestModel)

        assertTrue(resultModel.successful)
        assertNotNull(resultModel.userId)
        assertContains(resultModel.message, userName)
        assertContains(resultModel.message, "Successfully created user")
    }
}
