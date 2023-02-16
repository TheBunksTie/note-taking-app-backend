package com.thermondo.domain.user

import com.thermondo.domain.common.DomainException
import com.thermondo.domain.model.SimpleValueObject

/**
 * Value object representing the name of a [User]
 * @param userName the actual string username, may not be empty or (containing) blank and not longer than [maxUserNameLength]
 */
class UserName(userName: String) : SimpleValueObject<String>(userName) {

    private val maxUserNameLength = 20

    init {
        if (userName.isBlank() || userName.isEmpty())
            throw NoEmptyUserNameException()

        if (userName.length > maxUserNameLength)
            throw UserNameTooLongException(maxUserNameLength)

        if (userName.contains(" "))
            throw NoBlanksInUserNameException()
    }

    class NoEmptyUserNameException : DomainException("User name must not be empty")

    class UserNameTooLongException(maxUserNameLength: Int) : DomainException("User name mus be shorter than $maxUserNameLength characters")

    class NoBlanksInUserNameException : DomainException("User name must not contain any blank characters")
}

