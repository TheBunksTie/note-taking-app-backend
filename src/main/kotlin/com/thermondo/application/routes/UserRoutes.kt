package com.thermondo.application.routes

import com.thermondo.application.user.UserService
import com.thermondo.usecase.user.viewmodel.CreateUserViewModel
import com.thermondo.usecase.user.viewmodel.LoginUserViewModel
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.userRouting() {

    val userService by inject<UserService>()

    route("/api/v1/users") {

        post("") {
            val user = call.receive<CreateUserViewModel>()
            val resultModel = userService.createUser(user.userName, user.password, user.passwordRepetition)
            if (!resultModel.successful) {
                call.respond(HttpStatusCode.BadRequest, resultModel.message)
                return@post
            }

            call.respond(resultModel.userId!!)
        }

        post("/actions/login") {
            val loginViewModel = call.receive<LoginUserViewModel>()
            val resultModel = userService.loginUser(loginViewModel.userName, loginViewModel.password)
            if (!resultModel.successful) {
                call.respond(HttpStatusCode.BadRequest, resultModel.message)
                return@post
            }
            call.respond(resultModel.authenticationToken!!)
        }
    }
}
