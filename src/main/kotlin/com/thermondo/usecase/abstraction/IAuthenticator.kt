package com.thermondo.usecase.abstraction

import com.thermondo.domain.model.Id

/**
 * Basic interface for generation of authentication token
 */
interface IAuthenticator {
    fun generateToken(userId: Id): String
}
