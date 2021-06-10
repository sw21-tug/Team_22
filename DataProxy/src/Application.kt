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
import com.Table.Server.DatabaseObjects.*
import io.ktor.auth.jwt.*
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.*
import kotlin.collections.ArrayList

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads

fun Application.module(testing: Boolean = false) {
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    val jwthandler: JWTHandler = JWTHandler();
    install(Authentication) {
        jwt("requires-logged-in"){
            realm = jwthandler.myRealm
            verifier(jwthandler.getLoginVerifier())
            validate { credential ->
                var hasClaim = credential.payload.claims.contains(jwthandler.usernameString)
                if (hasClaim) {
                    //JWTPrincipal(credential.payload)
                    UserPrincipal(credential.payload.getClaim("username").asString())
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
            authenticate("requires-logged-in"){
            get("/") {
                //think about cases where users delete accounts, yet someone could still send a request with a valid JWT Token with no corresponding user.
                call.response.status(HttpStatusCode.OK)
                call.respond(mapOf("response" to "JWT is valid"))
               }
            }
        }

        route("/group") {
            authenticate("requires-logged-in") {
                post("/create") {
                    try {
                        val credentials = call.receive<GroupCredentials>()
                        if (credentials.groupid != null) {
                            call.response.status(HttpStatusCode.NotAcceptable)
                            call.respond(mapOf("response" to "Group id must be null when creating a new group."))
                            return@post
                        }
                        dbConnector.createGroup(credentials)
                        // send username, groupname, groupid as json
                        // response is status code
                        call.response.status(HttpStatusCode.OK)
                        call.respond(mapOf("response" to "Created Group"))
                    }
                    catch (e : java.lang.Exception) {
                        call.response.status(HttpStatusCode.NotAcceptable)
                        call.respond(mapOf("response" to "Invalid JSON format"))
                    }
                }
            }
            authenticate("requires-logged-in") {
                post("/addmember") {
                    try {
                        val credentials = call.receive<GroupCredentials>()
                        val errorcode = dbConnector.addMemberToGroup(credentials)
                        if(errorcode.equals(1)){
                            call.response.status(HttpStatusCode.Conflict)
                            call.respond(mapOf("response" to "User does not exist"))
                            return@post
                        }
                        if(errorcode.equals(2)){
                            call.response.status(HttpStatusCode.Conflict)
                            call.respond(mapOf("response" to "User already added"))
                            return@post
                        }

                        call.response.status(HttpStatusCode.OK)
                        call.respond(mapOf("response" to "User added"))
                    }
                    catch (i: JdbcSQLIntegrityConstraintViolationException) {
                        call.response.status(HttpStatusCode.Conflict)
                        call.respond(mapOf("response" to "Database constraint conflict"))
                        return@post
                    }
                    catch (e : java.lang.Exception) {
                        call.response.status(HttpStatusCode.NotAcceptable)
                        call.respond(mapOf("response" to "Invalid JSON format"))
                    }
                }
            }
            authenticate("requires-logged-in") {
                post("/deletemember") {
                    try {
                        val credentials = call.receive<GroupCredentials>()
                        dbConnector.deleteMemberFromGroup(credentials)
                        // send username, groupname, groupid
                        // response is status code
                        call.response.status(HttpStatusCode.OK)
                        call.respond(mapOf("response" to "Created Group"))
                    }
                    catch (e : java.lang.Exception) {
                        call.response.status(HttpStatusCode.NotAcceptable)
                        call.respond(mapOf("response" to "Invalid JSON format"))
                    }
                }
            }
            authenticate("requires-logged-in") {
                get("/getgrouplist") {
                    // send username
                    try {
                        val username = call.principal<UserPrincipal>()!!.username
                        // response is a statuscode with a dictionary mapping between groupname and id
                        var membernames:Map<String,String> = dbConnector.getGroupNames(username).toMap()
                        call.response.status(HttpStatusCode.OK)
                        call.respond(mapOf("list" to membernames))
                    }
                    catch (e : java.lang.Exception) {
                        call.response.status(HttpStatusCode.NotAcceptable)
                        call.respond(mapOf("response" to "Username not found"))
                    }

                }
            }

            authenticate("requires-logged-in") {
                post("/getUsersInGroup") {
                    try {
                        val credentials = call.receive<GroupCredentials>()
                        val user_list = dbConnector.getGroupUserNames(credentials)
                        call.response.status(HttpStatusCode.OK)
                        call.respond(mapOf("list" to user_list))
                    }

                    catch (e : java.lang.Exception)
                    {
                        call.response.status(HttpStatusCode.NotAcceptable)
                        call.respond(mapOf("response" to "Something Went Wrong"))
                    }
                }
            }

        }


            route("/user") {
            get("/{id}") {
                val id = call.parameters["id"]!!.toInt()
                val users = dbConnector.getUserById(id)
                if (users.size != 1) {
                    call.respond("User not found")
                    return@get
                }
                call.respond(users[0])
            }
            authenticate("requires-logged-in"){
                post("/updateBio"){
                    try {
                        val bio = call.receive<Bio>()
                        val ret = dbConnector.updateBio(bio, call.principal<UserPrincipal>()!!.username)
                        if (ret == 0) {
                            call.response.status(HttpStatusCode.OK)
                            call.respond(mapOf("response" to "success"))
                            return@post
                        } else {
                            throw Exception("ID invalid")
                        }
                    }
                    catch (e: ExposedSQLException) {
                        call.response.status(HttpStatusCode.Conflict)
                        call.respond(mapOf("response" to "Conflict"))
                        return@post
                    }
                    catch (i:java.lang.Exception) {
                        call.response.status(HttpStatusCode.NotAcceptable)
                        call.respond(mapOf("response" to "Request not acceptable"))
                        return@post
                    }
                    call.response.status(HttpStatusCode.ExpectationFailed)
                    call.respond(mapOf("response" to "Oh no!"))
                }

                get("/getBio") {
                    try {
                        val bios = dbConnector.getBioByUsername(call.principal<UserPrincipal>()!!.username)
                        if (!bios.isEmpty()) {
                            call.response.status(HttpStatusCode.OK)
                            call.respond(bios[0])
                            return@get
                        } else {
                            call.response.status(HttpStatusCode.NotFound)
                            call.respond(mapOf("response" to "No bio existing for user"))
                            return@get
                        }
                    }
                    catch (i:java.lang.Exception){
                        call.response.status(HttpStatusCode.Conflict)
                        call.respond(mapOf("response" to "Cant retrieve bio"))
                        return@get
                    }
                }

                post("/getUsersByPreferences"){
                    try{
                        val preferences = call.receive<SearchPreferences>()
                        print(preferences.username)
                        val users = dbConnector.getAllUsers()
                        var matched_users :MutableList<String> = ArrayList()

                        for (user in users){
                            val bio = dbConnector.getBioByUsername(user.username)

                            if(user.username == preferences.username){
                                continue
                            }
                            if(bio.isEmpty()){
                                continue
                            }

                            if(((bio[0].wargames == preferences.wargames && preferences.wargames)
                                            || (bio[0].card_games == preferences.card_games &&preferences.card_games)
                                            || (bio[0].board_games == preferences.board_games && preferences.board_games)
                                            ||( bio[0].ttrpg == preferences.ttrpg && preferences.ttrpg))
                                    && bio[0].city == preferences.city &&
                                    (bio[0].age!! <= preferences.max_age && bio[0].age!! >= preferences.min_age)){
                                matched_users.add(user.username)
                            }
                        }
                        call.response.status(HttpStatusCode.OK)
                        call.respond(matched_users)
                        return@post
                    }
                    catch (i:java.lang.Exception){
                        call.response.status(HttpStatusCode.Conflict)
                        call.respond(mapOf("response" to "Cant retrieve Searchpreferences"))
                        return@post
                    }
                }

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
                            call.respond(mapOf("jwtToken" to jwthandler.generateLoginToken(users[0])))
                            return@post
                    }
                    call.response.status(HttpStatusCode.NotFound)
                    call.respond("Yes")
                    return@post

                } catch (e: Exception) {
                    call.response.status(HttpStatusCode.NotAcceptable)
                    call.respondText("Failed to parse User")
                    return@post
                }
                call.response.status(HttpStatusCode.ExpectationFailed)
                call.respond("Oh no!")
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
