package com.Table.Server

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.sessions.*
import io.ktor.auth.*
import com.fasterxml.jackson.databind.*
import io.ktor.jackson.*
import io.ktor.features.*
import com.Table.Server.DatabaseConnector
import com.Table.Server.DatabaseObjects.*
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    install(Authentication) {
        basic("myBasicAuth") {
            realm = "Ktor Server"
            validate { if (it.name == "test" && it.password == "password") UserIdPrincipal(it.name) else null }
        }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    //set dbconnector
    val dbConnector = DatabaseConnector()
    dbConnector.connectToDatabase()

    install(Routing) {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        route("/user") {
            get("/") {
                val users = dbConnector.getAllUsers()
                call.respond(users)
            }
            get("/{id}") {
                val id = call.parameters["id"]!!.toInt()
                val users = dbConnector.getUserById(id)
                if (users.size != 1) {
                    call.respond("User not found")
                    return@get
                }
                call.respond(users[0])
            }
            post("/") {
                val user = call.receive<User>()
                val responseUser = dbConnector.insertUser(user)
                call.respond(responseUser)
            }
            post("/register") {
                var user: User?
                var id: Int?
                try {
                    user = call.receive<User>()
                    if(!Users.emailValidator(user.email)) {
                        call.response.status(HttpStatusCode.NotAcceptable)
                        call.respondText("Email invalid")
                        return@post
                    }
                    id = dbConnector.insertUser(user)
                } catch (e: ExposedSQLException) {
                    call.response.status(HttpStatusCode.Conflict)
                    call.respondText("User already existing or database error")
                    return@post
                } catch (e: Exception) {
                    call.response.status(HttpStatusCode.NotAcceptable)
                    call.respondText("Failed to parse User")
                    return@post
                }
                call.response.status(HttpStatusCode.OK)
                call.respond(mapOf("id" to id))
            }

        }



        get("/session/increment") {
            val session = call.sessions.get<MySession>() ?: MySession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }

        authenticate("myBasicAuth") {
            get("/protected/route/basic") {
                val principal = call.principal<UserIdPrincipal>()!!
                call.respondText("Hello ${principal.name}")
            }
        }

        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

data class MySession(val count: Int = 0)

