package com.thermondo.domain.user

import com.thermondo.domain.common.DomainException
import com.thermondo.domain.model.SimpleValueObject

/**
 * Value object representing the name of a [User]
 * @param password the actual string password, may not be empty or (containing) blank,
 * must contain at least one lowercase and one uppercase character and at least one digit
 * and may not be longer than [maxPasswordLength]
 */
class Password(password: String) : SimpleValueObject<String>(password) {
    // TODO internal representation of password should be more safe (char array?)

    private val maxPasswordLength = 20
    private val lowerCaseChar by lazy { Regex("[a-z]") }
    private val upperCaseChar by lazy { Regex("[A-Z]") }
    private val decimal by lazy { Regex("\\d") }

    init {
        if (password.isBlank() || password.isEmpty())
            throw NoEmptyPasswordException()

        if (password.length > maxPasswordLength)
            throw PasswordTooLongException(maxPasswordLength)

        if (password.contains(" "))
            throw NoBlanksInPasswordException()

        if (!password.contains(lowerCaseChar))
            throw MustContainLowerCaseCharacter()

        if (!password.contains(upperCaseChar))
            throw MustContainUpperCaseCharacter()

        if (!password.contains(decimal))
            throw MustContainDigit()
    }

    class NoEmptyPasswordException : DomainException("Password must not be empty")

    class PasswordTooLongException(maxPasswordLength: Int) : DomainException("Title mus be shorter than $maxPasswordLength characters")

    class NoBlanksInPasswordException : DomainException("Password must not contain any blank characters")

    class MustContainLowerCaseCharacter : DomainException("Password must contain at least one lower case letter")

    class MustContainUpperCaseCharacter : DomainException("Password must contain at least one upper case letter")

    class MustContainDigit : DomainException("Password must contain at least one digit")
}
