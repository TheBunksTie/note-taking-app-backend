package com.thermondo.persistence.user

import com.thermondo.domain.user.Password
import com.thermondo.domain.user.User
import com.thermondo.domain.user.UserName
import com.thermondo.usecase.user.abstraction.IUserRepository
import org.junit.Rule
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import kotlin.test.*

class UserRepositoryTest : KoinTest {
    private val userRepository: IUserRepository by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(userPersistenceModule)
    }

    // TODO of course the different components of the repository system should be tested by unit tests

    @Test
    fun crudUserIntegrationTest() {

        val initialUserName = UserName("user1")
        val initialPassword = Password("seCreT123")
        val initialUser = User.new(initialUserName, initialPassword)

        // initial persist
        val persistedUser = userRepository.persist(initialUser)
        assertNotNull(persistedUser)
        assertEquals(initialUserName, persistedUser.userName)
        assertEquals(initialPassword, persistedUser.password)

        // updated persist
        val updatedUserName = UserName("updated$initialUserName")
        val updatedPassword = Password("updated$initialPassword")
        persistedUser.userName = updatedUserName
        persistedUser.password = updatedPassword
        userRepository.persist(persistedUser)

        // get by id
        val updatedPersistedUser1 = userRepository.getBy(persistedUser.id)
        assertNotNull(updatedPersistedUser1)
        assertEquals(updatedUserName, updatedPersistedUser1.userName)
        assertEquals(updatedPassword, updatedPersistedUser1.password)

        // get by name
        val updatedPersistedUser2 = userRepository.getBy(persistedUser.userName)
        assertNotNull(updatedPersistedUser2)
        assertEquals(updatedUserName, updatedPersistedUser2.userName)
        assertEquals(updatedPassword, updatedPersistedUser2.password)

        // get all
        val persistedUsersList = userRepository.getAll()
        assertEquals(1, persistedUsersList.size)

        // successful exists by id
        val existsById1 = userRepository.exists(updatedPersistedUser2.id)
        assertTrue(existsById1)

        // successful exists by name
        val existsByName1 = userRepository.exists(updatedPersistedUser2.userName)
        assertTrue(existsByName1)

        // successful delete
        val delete1Result = userRepository.delete(updatedPersistedUser2.id)
        assertTrue(delete1Result)

        // non successful delete
        val delete2Result = userRepository.delete(updatedPersistedUser2.id)
        assertFalse(delete2Result)

        // non-successful exists by id
        val existsById2 = userRepository.exists(updatedPersistedUser2.id)
        assertFalse(existsById2)

        // non-successful exists by name
        val existsByName2 = userRepository.exists(updatedPersistedUser2.userName)
        assertFalse(existsByName2)

        // empty get all
        val persistedUsersList2 = userRepository.getAll()
        assertTrue(persistedUsersList2.isEmpty())

    }
}