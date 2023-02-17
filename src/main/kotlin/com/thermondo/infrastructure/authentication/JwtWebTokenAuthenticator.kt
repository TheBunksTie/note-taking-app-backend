package com.thermondo.infrastructure.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.thermondo.domain.model.Id
import com.thermondo.usecase.abstraction.IAuthenticator
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

/**
 * Json web token authentication strategy
 */
class JwtWebTokenAuthenticator : IAuthenticator {

    companion object Constants {
        // jwt config
        private const val jwtIssuer = "com.thermondo"
        private const val jwtAudience = "com.thermondo"
        private const val jwtRealm = "com.thermondo.notes"
        // TODO obviously in production this would be retrieved from an encrypted source outside of code
        private const val jwtSecret = "secret"

        // claims
        private const val CLAIM_USERID = "userId"
    }

    override fun generateToken(userId: Id): String =
        JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtIssuer)
            .withClaim(CLAIM_USERID, userId.toString())
            .sign(Algorithm.HMAC256(jwtSecret))

    fun configureJwtBasedAuthentication(config: JWTAuthenticationProvider.Config) = with(config) {

        realm = jwtRealm

        verifier(JWT
            .require(Algorithm.HMAC256(jwtSecret))
            .withAudience(jwtAudience)
            .withIssuer(jwtIssuer)
            .build())

        validate { credential ->
            val userId = credential.payload.getClaim(CLAIM_USERID).asString()
            if (!userId.isNullOrEmpty()) {
                JwtUser(userId)
            } else {
                null
            }
        }

        challenge { _, _ ->
            call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
        }
    }

    data class JwtUser(val userId: String): Principal
}
