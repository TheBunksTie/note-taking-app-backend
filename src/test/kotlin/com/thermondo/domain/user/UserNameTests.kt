package com.thermondo.domain.user

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UserNameTests {

    @Test
    fun new_emptyUserName_throwsException() {
        assertFailsWith<UserName.NoEmptyUserNameException> { UserName("") }
    }

    @Test
    fun new_blankUserName_throwsException() {
        assertFailsWith<UserName.NoEmptyUserNameException> { UserName("   ") }
    }

    @Test
    fun new_tooLongUserName_throwsException() {
        assertFailsWith<UserName.UserNameTooLongException> { UserName("AVeryLongUserNameThatIsInvalid") }
    }

    @Test
    fun new_containingBlanks_throwsException() {
        assertFailsWith<UserName.NoBlanksInUserNameException> { UserName("User name") }
    }

    @Test
    fun new_validUserName_returnsUserName() {
        val expectedUserName = "MyUser1"

        val userName = UserName(expectedUserName)
        assertEquals(expectedUserName, userName.value)
    }
}
