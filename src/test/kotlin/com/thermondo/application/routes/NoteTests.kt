package com.thermondo.application.routes

import com.thermondo.domain.note.NoteType
import com.thermondo.usecase.common.viewmodel.AuthenticationTokenViewModel
import com.thermondo.usecase.note.viewmodel.CreateNoteViewModel
import com.thermondo.usecase.note.viewmodel.NoteViewModel
import com.thermondo.usecase.note.viewmodel.UpdateNoteViewModel
import com.thermondo.usecase.user.viewmodel.LoginUserViewModel
import io.ktor.client.*
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

class NoteTests {
    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun unauthenticated_publicNotes_returnsNoteList() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/api/v1/notes/public").body<List<NoteViewModel>>()
        assertEquals(2, response.size)
    }

    @Test
    fun unauthenticated_publicNotesAuthenticationOnlyPath_returnsUnauthorized() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/api/v1/notes?mode=public")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun authenticated_publicNotes_returnsNoteList() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val authenticationToken = loginTestUser1(client)

        val response = client.get("/api/v1/notes?mode=public") {
            header(HttpHeaders.Authorization, "Bearer $authenticationToken")
        }.body<List<NoteViewModel>>()

        assertEquals(2, response.size)
        assertEquals(NoteType.PUBLIC.toString(), response[0].noteType)
        assertEquals(NoteType.PUBLIC.toString(), response[1].noteType)
    }

    @Test
    fun authenticated_myNotes_returnsNoteList() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val authenticationToken = loginTestUser1(client)

        val response = client.get("/api/v1/notes?mode=author") {
            header(HttpHeaders.Authorization, "Bearer $authenticationToken")
        }.body<List<NoteViewModel>>()

        assertEquals(2, response.size)
        assertEquals(NoteType.PUBLIC.toString(), response[0].noteType)
        assertEquals(NoteType.PRIVATE.toString(), response[1].noteType)
    }

    @Test
    fun unauthenticated_myNotes_returnsUnauthorized() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/api/v1/notes?mode=author")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun authenticated_tagFiltered_returnsNoteList() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val authenticationToken = loginTestUser1(client)

        val response = client.get("/api/v1/notes?tag=tag111,tag222") {
            header(HttpHeaders.Authorization, "Bearer $authenticationToken")
        }.body<List<NoteViewModel>>()

        assertEquals(1, response.size)
        assertEquals("body11", response[0].body)
    }

    @Test
    fun unauthenticated_tagFiltered_returnsUnauthorized() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/api/v1/notes?tag=tag111,tag222")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun authenticated_keywordFiltered_returnsNoteList() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val authenticationToken = loginTestUser1(client)

        val response = client.get("/api/v1/notes?keyword=body12") {
            header(HttpHeaders.Authorization, "Bearer $authenticationToken")
        }.body<List<NoteViewModel>>()

        assertEquals(1, response.size)
        assertEquals("body12", response[0].body)
    }

    @Test
    fun unauthenticated_keywordFiltered_returnsUnauthorized() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/api/v1/notes?keyword=body12")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun authenticated_createNewNote_returnsNoteId() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val authenticationToken = loginTestUser1(client)

        val response = client.post("/api/v1/notes") {
            header(HttpHeaders.Authorization, "Bearer $authenticationToken")
            contentType(ContentType.Application.Json)
            setBody(CreateNoteViewModel("my newly created note", "the body of my new note", listOf("tag1", "tag2"), NoteType.PRIVATE.toString()))
        }

        assertEquals(HttpStatusCode.Created, response.status)

        val createdNoteId = response.bodyAsText()
        assertNotNull(UUID.fromString(createdNoteId))
    }

    @Test
    fun unauthenticated_createNewNote_returnsUnauthorized() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/api/v1/notes") {
            contentType(ContentType.Application.Json)
            setBody(CreateNoteViewModel("my newly created note", "the body of my new note", listOf("tag1", "tag2"), NoteType.PRIVATE.toString()))
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun authenticated_updateExistingNote_returnsUpdatedNotId() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val authenticationToken = loginTestUser1(client)

        val createdNoteId = client.post("/api/v1/notes") {
            header(HttpHeaders.Authorization, "Bearer $authenticationToken")
            contentType(ContentType.Application.Json)
            setBody(CreateNoteViewModel("my newly created note", "the body of my new note", listOf("tag1", "tag2"), NoteType.PRIVATE.toString()))
        }.bodyAsText()

        val updatedNoteId = client.put("/api/v1/notes") {
            header(HttpHeaders.Authorization, "Bearer $authenticationToken")
            contentType(ContentType.Application.Json)
            setBody(UpdateNoteViewModel(createdNoteId, "my newly created note", "the body of my new note", listOf("tag1", "tag2"), NoteType.PRIVATE.toString()))
        }.bodyAsText()

        assertNotNull(UUID.fromString(updatedNoteId))
        assertEquals(createdNoteId, updatedNoteId)
    }

    @Test
    fun unauthenticated_updateExistingNote_returnsUnauthorized() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val authenticationToken = loginTestUser1(client)

        val createdNoteId = client.post("/api/v1/notes") {
            header(HttpHeaders.Authorization, "Bearer $authenticationToken")
            contentType(ContentType.Application.Json)
            setBody(CreateNoteViewModel("my newly created note", "the body of my new note", listOf("tag1", "tag2"), NoteType.PRIVATE.toString()))
        }.bodyAsText()

        val response = client.put("/api/v1/notes") {
            contentType(ContentType.Application.Json)
            setBody(UpdateNoteViewModel(createdNoteId, "my newly created note", "the body of my new note", listOf("tag1", "tag2"), NoteType.PRIVATE.toString()))
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun authenticated_deleteExistingNote_returnsDeletedNoteId() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val authenticationToken = loginTestUser1(client)

        val createdNoteId = client.post("/api/v1/notes") {
            header(HttpHeaders.Authorization, "Bearer $authenticationToken")
            contentType(ContentType.Application.Json)
            setBody(CreateNoteViewModel("my newly created note", "the body of my new note", listOf("tag1", "tag2"), NoteType.PRIVATE.toString()))
        }.bodyAsText()


        val deletedNoteId = client.delete("/api/v1/notes/$createdNoteId") {
            header(HttpHeaders.Authorization, "Bearer $authenticationToken")
        }.bodyAsText()

        assertNotNull(UUID.fromString(deletedNoteId))
        assertEquals(createdNoteId, deletedNoteId)
    }

    @Test
    fun unauthenticated_deleteExistingNote_returnsUnauthorized() = testApplication {

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val authenticationToken = loginTestUser1(client)

        val createdNoteId = client.post("/api/v1/notes") {
            header(HttpHeaders.Authorization, "Bearer $authenticationToken")
            contentType(ContentType.Application.Json)
            setBody(CreateNoteViewModel("my newly created note", "the body of my new note", listOf("tag1", "tag2"), NoteType.PRIVATE.toString()))
        }.bodyAsText()


        val response = client.delete("/api/v1/notes/$createdNoteId")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    private suspend fun loginTestUser1(client: HttpClient): String {
        val loginResponse = client.post("/api/v1/users/actions/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginUserViewModel("testUser1", "Test1"))
        }
        return loginResponse.body<AuthenticationTokenViewModel>().authenticationToken
    }

//    @Test
//    fun publicNotes() = testApplication {
//
//        val client = createClient {
//
//            install(ContentNegotiation) {
//                json()
//            }
//        }
//
//        val loginResponse = client.post("login") {
//            contentType(ContentType.Application.Json)
//            //setBody(User("jetbrains", "foobar"))
//        }
//
//        val authToken = loginResponse.body<AuthToken>().token
//
//        val helloResponseText = client.get("hello") {
//            header(HttpHeaders.Authorization, "Bearer $authToken")
//        }.bodyAsText()
//
//        assertTrue {
//            helloResponseText.contains("Hello, jetbrains!")
//        }
//    }

}
