package com.thermondo.application.routes

import com.thermondo.usecase.common.viewmodel.AuthenticationTokenViewModel
import com.thermondo.usecase.user.viewmodel.CreateUserViewModel
import com.thermondo.usecase.user.viewmodel.LoginUserViewModel
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.junit.After
import org.junit.Test
import org.koin.core.context.GlobalContext.stopKoin
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UserTests {

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun unauthenticated_createUser_returnsUserId() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val createdUserId = client.post("/api/v1/users") {
            contentType(ContentType.Application.Json)
            setBody(CreateUserViewModel("anotherUser", "Test1", "Test1"))
        }.bodyAsText()

        assertNotNull(UUID.fromString(createdUserId))
    }

    @Test
    fun unauthenticated_loginValidUser_returnsAuthenticationToken() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val resultViewModel = client.post("/api/v1/users/actions/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginUserViewModel("testUser1", "Test1"))
        }.body<AuthenticationTokenViewModel>()

        assertNotNull(resultViewModel.authenticationToken)
        assertTrue(resultViewModel.authenticationToken.isNotEmpty())
    }

    @Test
    fun unauthenticated_loginInValidUser_returnsBadRequest() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/api/v1/users/actions/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginUserViewModel("testUser1", "Test22"))
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}
