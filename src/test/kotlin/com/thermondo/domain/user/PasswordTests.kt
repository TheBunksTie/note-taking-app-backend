package com.thermondo.domain.user

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PasswordTests {

    @Test
    fun new_emptyPassword_throwsException() {
        assertFailsWith<Password.NoEmptyPasswordException> { Password("") }
    }

    @Test
    fun new_blankPassword_throwsException() {
        assertFailsWith<Password.NoEmptyPasswordException> { Password("   ") }
    }

    @Test
    fun new_tooLongPassword_throwsException() {
        assertFailsWith<Password.PasswordTooLongException> { Password("very_long_password_password") }
    }

    @Test
    fun new_containingBlanks_throwsException() {
        assertFailsWith<Password.NoBlanksInPasswordException> { Password("pass word") }
    }

    @Test
    fun new_noLowerCaseCharacter_throwsException() {
        assertFailsWith<Password.MustContainLowerCaseCharacter> { Password("PASSWORD123") }
    }

    @Test
    fun new_noUpperCaseCharacter_throwsException() {
        assertFailsWith<Password.MustContainUpperCaseCharacter> { Password("password123") }
    }

    @Test
    fun new_noDigits_throwsException() {
        assertFailsWith<Password.MustContainDigit> { Password("passWORD") }
    }

    @Test
    fun new_validPassword_returnsPassword() {
        val expectedPassword = "pass_WORD_123"
        val password = Password(expectedPassword)
        assertEquals(expectedPassword, password.value)
    }
}
