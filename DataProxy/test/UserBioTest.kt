package com.Table.Server

import com.Table.Server.DatabaseObjects.Bio
import com.Table.Server.DatabaseObjects.User
import com.Table.Server.DatabaseObjects.UserCredentials
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.*
import org.junit.Before
import kotlin.test.*
import io.ktor.server.testing.*

class UserBioTest {
    val dbConnector: DatabaseConnector = DatabaseConnector()

    var loggedInUser: User = User(null, "max", "mustermann", "password")
    var jwtToken: String? = null
    val jwthandler = JWTHandler()

    @Before
    fun initDB() {
        dbConnector.reset()
        dbConnector.connectToDatabase()
        val id = dbConnector.insertUser(loggedInUser)
        jwtToken = "Bearer " + jwthandler.generateLoginToken(dbConnector.getUserForAuth(UserCredentials(loggedInUser.username, loggedInUser.password, null))[0])
        loggedInUser = dbConnector.getUserById(id)[0]
    }

    @Test
    fun testAddUserBio() {
        withTestApplication({ module(testing = true) }) {
            val update_bio_request = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser.bio_id, "maxi_muster", 16, "Graz", true, false, true, false)))
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun testAddUserBioInvalid() {
        withTestApplication({ module(testing = true) }) {
            val update_bio_request = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody("{\"id\": 100, \"age\": \"16Invalid\"}") // missing obligatory username
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
            }
        }
    }

}