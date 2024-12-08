package org.lab4

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.jsonPrimitive


fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        install(ContentNegotiation) {
            json()
        }

        val bookDao = BookDAO()

        routing {
            route("/") {
                get("/") {
                    call.respond("Hello, world!")
                }
            }
            route("/books") {
                get {
                    call.respond(bookDao.listAllBooks())
                }

                get("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()
                    id?.let {
                        val book = bookDao.getBook(it)
                        if (book != null) call.respond(book) else call.respond(HttpStatusCode.NotFound)
                    } ?: call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                }
                post {
                    val  rawBody = call.receiveText()
                    println(rawBody)
                    try {
                        if (rawBody.isBlank()) {
                            call.respond(HttpStatusCode.BadRequest, "Request body cannot be empty")
                            return@post
                        }

                        val json = kotlinx.serialization.json.Json.parseToJsonElement(rawBody)
                        if (json !is kotlinx.serialization.json.JsonObject) {
                            call.respond(HttpStatusCode.BadRequest, "Invalid JSON structure")
                            return@post
                        }

                        val authorElement = json["author"]
                        if (authorElement == null || !authorElement.jsonPrimitive.isString) {
                            call.respond(HttpStatusCode.BadRequest, "Field 'author' is missing or not a string")
                            return@post
                        }

                        val titleElement = json["title"]
                        if (titleElement == null || !titleElement.jsonPrimitive.isString) {
                            call.respond(HttpStatusCode.BadRequest, "Field 'title' is missing or not a string")
                            return@post
                        }

                        val publishedElement = json["published"]
                        if (publishedElement == null || publishedElement.jsonPrimitive.isString ||
                            publishedElement.jsonPrimitive.content.toIntOrNull() == null) {
                            call.respond(HttpStatusCode.BadRequest, "Field 'published' is missing or not an integer")
                            return@post
                        }

                        val scoreElement = json["score"]
                        if (scoreElement == null || scoreElement.jsonPrimitive.isString ||
                            scoreElement.jsonPrimitive.content.toIntOrNull() == null) {
                            call.respond(HttpStatusCode.BadRequest, "Field 'score' is missing or not an integer")
                            return@post
                        }

                        val descriptionElement = json["description"]
                        if (descriptionElement != null && !descriptionElement.jsonPrimitive.isString) {
                            call.respond(HttpStatusCode.BadRequest, "Field 'description' must be a string if provided")
                            return@post
                        }
                        val description = descriptionElement?.jsonPrimitive?.content ?: ""

                        val book = BookLib(
                            0,
                            authorElement.jsonPrimitive.content,
                            titleElement.jsonPrimitive.content,
                            description,
                            publishedElement.jsonPrimitive.content.toInt(),
                            scoreElement.jsonPrimitive.content.toInt()
                        )

                        val created = bookDao.insertBook(book)
                        if (created) call.respond(HttpStatusCode.Created) else call.respond(HttpStatusCode.InternalServerError)


                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid request: ${e.message}")
                    }
                }
                put("{id}") {
                    val  rawBody = call.receiveText()
                    println(rawBody)
                    try {
                        if (rawBody.isBlank()) {
                            call.respond(HttpStatusCode.BadRequest, "Request body cannot be empty")
                            return@put
                        }

                        val json = kotlinx.serialization.json.Json.parseToJsonElement(rawBody)
                        if (json !is kotlinx.serialization.json.JsonObject) {
                            call.respond(HttpStatusCode.BadRequest, "Invalid JSON structure")
                            return@put
                        }

                        val authorElement = json["author"]
                        if (authorElement == null || !authorElement.jsonPrimitive.isString) {
                            call.respond(HttpStatusCode.BadRequest, "Field 'author' is missing or not a string")
                            return@put
                        }

                        val titleElement = json["title"]
                        if (titleElement == null || !titleElement.jsonPrimitive.isString) {
                            call.respond(HttpStatusCode.BadRequest, "Field 'title' is missing or not a string")
                            return@put
                        }

                        val publishedElement = json["published"]
                        if (publishedElement == null || publishedElement.jsonPrimitive.isString ||
                            publishedElement.jsonPrimitive.content.toIntOrNull() == null) {
                            call.respond(HttpStatusCode.BadRequest, "Field 'published' is missing or not an integer")
                            return@put
                        }

                        val scoreElement = json["score"]
                        if (scoreElement == null || scoreElement.jsonPrimitive.isString ||
                            scoreElement.jsonPrimitive.content.toIntOrNull() == null) {
                            call.respond(HttpStatusCode.BadRequest, "Field 'score' is missing or not an integer")
                            return@put
                        }

                        val descriptionElement = json["description"]
                        if (descriptionElement != null && !descriptionElement.jsonPrimitive.isString) {
                            call.respond(HttpStatusCode.BadRequest, "Field 'description' must be a string if provided")
                            return@put
                        }
                        val description = descriptionElement?.jsonPrimitive?.content ?: ""

                        val idElement  = json["id"]
                        if (idElement == null || idElement.jsonPrimitive.isString ||
                            idElement.jsonPrimitive.content.toIntOrNull() == null) {
                            call.respond(HttpStatusCode.BadRequest, "Field 'id' is missing or not an integer")
                            return@put
                        }

                        val book = BookLib(
                            idElement.jsonPrimitive.content.toInt(),
                            authorElement.jsonPrimitive.content,
                            titleElement.jsonPrimitive.content,
                            description,
                            publishedElement.jsonPrimitive.content.toInt(),
                            scoreElement.jsonPrimitive.content.toInt()
                        )
                        val updated = bookDao.updateBook(book)
                        if (updated) call.respond(HttpStatusCode.OK) else call.respond(HttpStatusCode.NotFound, "Invalid ID")
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, "Invalid request: ${e.message}")
                    }
                }

                delete("{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()
                    id?.let {
                        val deleted = bookDao.deleteBook(it)
                        if (deleted) call.respond(HttpStatusCode.OK) else call.respond(HttpStatusCode.NotFound)
                    } ?: call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                }
            }
        }
    }.start(wait = true)
}
