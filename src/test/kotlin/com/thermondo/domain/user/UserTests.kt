package com.thermondo.domain.user

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UserTests {

    @Test
    fun new_validArguments_returnsUser() {
        val expectedUserName = "user1"
        val expectedPassword = "seCreT123"

        val user = User.new(UserName(expectedUserName), Password(expectedPassword))

        assertNotNull(user.id)
        assertNotNull(user.createdAt)
        assertNotNull(user.changedAt)
        assertEquals(expectedUserName, user.userName.value)
        assertEquals(expectedPassword, user.password.value)
    }
}
