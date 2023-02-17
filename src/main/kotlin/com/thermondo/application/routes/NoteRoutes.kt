package com.thermondo.application.routes

import com.thermondo.application.note.NoteService
import com.thermondo.infrastructure.authentication.JwtWebTokenAuthenticator
import com.thermondo.usecase.note.viewmodel.CreateNoteViewModel
import com.thermondo.usecase.note.viewmodel.UpdateNoteViewModel
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.util.toLowerCasePreservingASCIIRules
import org.koin.ktor.ext.inject

fun Route.noteRouting() {

    val noteService by inject<NoteService>()

    route("/api/v1/notes") {

        authenticate("jwt-auth") {

            get("") {
                // api/v1/notes?mode=(public|author)
                val modeString = call.request.queryParameters["mode"]
                if (!modeString.isNullOrEmpty()) {

                    if (modeString.toLowerCasePreservingASCIIRules() == "public") {
                        val resultModel = noteService.getNotesByPublicState()
                        call.respond(resultModel.notes)
                        return@get
                    }

                    if (modeString.toLowerCasePreservingASCIIRules() == "author") {
                        val authenticatedUser = call.authentication.principal<JwtWebTokenAuthenticator.JwtUser>()
                        val resultModel = noteService.getNotesByAuthor(authenticatedUser?.userId!!)
                        if (!resultModel.successful) {
                            call.respond(HttpStatusCode.BadRequest, resultModel.message)
                            return@get
                        }
                        call.respond(resultModel.notes)
                        return@get
                    }
                    call.respond(HttpStatusCode.NotFound)
                }

                // api/v1/notes?tag=tag1,tag2
                val tagString = call.request.queryParameters["tag"]
                if (!tagString.isNullOrEmpty()) {
                    val authenticatedUser = call.authentication.principal<JwtWebTokenAuthenticator.JwtUser>()
                    val tagList = tagString.split(",")
                    val resultModel = noteService.getNotesByTags(authenticatedUser?.userId!!, tagList)
                    if (!resultModel.successful) {
                        call.respond(HttpStatusCode.BadRequest, resultModel.message)
                        return@get
                    }
                    call.respond(resultModel.notes)
                    return@get
                }

                // api/v1/notes?keyword=keyword1,keyword2
                val keywordString = call.request.queryParameters["keyword"]
                if (!keywordString.isNullOrEmpty()) {
                    val authenticatedUser = call.authentication.principal<JwtWebTokenAuthenticator.JwtUser>()
                    val keywordList = keywordString.split(",")
                    val resultModel = noteService.getNotesByKeywords(authenticatedUser?.userId!!, keywordList)
                    if (!resultModel.successful) {
                        call.respond(HttpStatusCode.BadRequest, resultModel.message)
                        return@get
                    }
                    call.respond(resultModel.notes)
                    return@get
                }
                call.respond(HttpStatusCode.BadRequest)
            }

            get("/{id}") {
                // TODO authenticated single read
            }

            post("") {
                val note = call.receive<CreateNoteViewModel>()
                val authenticatedUser = call.authentication.principal<JwtWebTokenAuthenticator.JwtUser>()

                val resultModel = noteService.createNote(note.title, note.body, note.tags, authenticatedUser?.userId!!, note.noteType)
                if (!resultModel.successful) {
                    call.respond(HttpStatusCode.BadRequest, resultModel.message)
                    return@post
                }
                call.respond(HttpStatusCode.Created, resultModel.noteId!!)
            }

            put("") {
                val note = call.receive<UpdateNoteViewModel>()
                val authenticatedUser = call.authentication.principal<JwtWebTokenAuthenticator.JwtUser>()

                val resultModel = noteService.updateNote(
                    note.id,
                    note.title,
                    note.body,
                    note.tags,
                    authenticatedUser?.userId!!,
                    note.noteType
                )
                if (!resultModel.successful) {
                    call.respond(HttpStatusCode.BadRequest, resultModel.message)
                    return@put
                }
                call.respond(resultModel.noteId!!)
            }

            delete("/{id}") {
                val noteIdString = call.parameters["id"]
                if (noteIdString == null) {
                    call.respond(HttpStatusCode.BadRequest, "id parameter must not be empty")
                    return@delete
                }

                val authenticatedUser = call.authentication.principal<JwtWebTokenAuthenticator.JwtUser>()
                val resultModel = noteService.deleteNote(noteIdString, authenticatedUser?.userId!!)
                if (!resultModel.successful) {
                    call.respond(HttpStatusCode.BadRequest, resultModel.message)
                    return@delete
                }

                call.respond(resultModel.noteId!!)
            }
        }

        get("/public") {
            val resultModel = noteService.getNotesByPublicState()
            call.respond(resultModel.notes)
        }
    }
}
