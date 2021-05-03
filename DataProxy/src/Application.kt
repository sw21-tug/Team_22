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
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.jwt.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads

fun Application.module(testing: Boolean = false) {
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    val issuer = "127.0.0.1" //environment.config.property("jwt.domain").getString()
    val audience ="L4GUser"// environment.config.property("jwt.audience").getString()
    val myRealm = "L4GServer"//environment.config.property("jwt.realm").getString()
    val secret:String = "secret"
    val verifierStringHeader:String = "isLoggedIn"
    val verifierString:String = "loggedIn"
    val encryptionAlgorithm:Algorithm = Algorithm.HMAC256(secret)


    fun Application.generateToken(userCredentials: UserCredentials): String =
        JWT.create().withAudience(audience)
        .withIssuer(issuer)
        .withClaim(verifierStringHeader, verifierString)
        .withClaim("username", userCredentials.username)
        .withExpiresAt(Date(System.currentTimeMillis() + 36000000)) //expirationtime, currently 10hours
        .sign(encryptionAlgorithm)

    install(Authentication) {
        jwt{
            realm = myRealm
            verifier(JWT
                .require(encryptionAlgorithm)
                .withAudience(audience)
                .withIssuer(issuer)
                .build())
            validate { credential ->
                var claim = credential.payload.getClaim(verifierStringHeader).asString()
                if (claim.equals(verifierString)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
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

        route("/testauthentication"){
            authenticate{
            get("/") {
                call.response.status(HttpStatusCode.OK)
                call.respond("JWT is valid")
               }
            }
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
            post("/login") {
                var credentials: UserCredentials?
                try {
                    credentials = call.receive<UserCredentials>()
                    val users:List<UserCredentials> = dbConnector.getUserForAuth(credentials)
                    if (!users.isEmpty() && Users.credentialsEquals(credentials, users[0]))
                    {
                            call.response.status(HttpStatusCode.OK)
                            call.respondText(generateToken(credentials))
                            return@post
                    }

                } catch (e: Exception) {
                    call.response.status(HttpStatusCode.NotAcceptable)
                    call.respondText("Failed to parse User")
                    return@post
                }
                call.response.status(HttpStatusCode.NotFound)
                call.respond("Yes")
            }

        }



        get("/session/increment") {
            val session = call.sessions.get<MySession>() ?: MySession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }

        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

data class MySession(val count: Int = 0)
