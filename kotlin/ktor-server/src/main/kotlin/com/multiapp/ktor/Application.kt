package com.multiapp.ktor

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8083, module = Application::module).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
    }

    val store = TaskStore()

    routing {
        get("/") {
            call.respondText(
                "Ktor Task API is running. Try GET /api/tasks — the Android app is the frontend.",
                ContentType.Text.Plain,
            )
        }

        // REST API (shared contract)
        route("/api/tasks") {
            get {
                call.respond(store.all())
            }

            post {
                val title = call.receive<TaskRequest>().title?.trim().orEmpty()
                if (title.isEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, ApiError("title is required"))
                } else {
                    call.respond(HttpStatusCode.Created, store.create(title))
                }
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toLongOrNull()
                val body = call.receive<TaskRequest>()
                val task = id?.let { store.update(it, body.title, body.done) }
                if (task == null) {
                    call.respond(HttpStatusCode.NotFound, ApiError("not found"))
                } else {
                    call.respond(task)
                }
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toLongOrNull()
                if (id != null && store.delete(id)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, ApiError("not found"))
                }
            }
        }
    }
}
