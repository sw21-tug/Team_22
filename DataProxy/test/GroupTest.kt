package com.Table.Server

import com.Table.Server.DatabaseObjects.Bio
import com.Table.Server.DatabaseObjects.GroupCredentials
import com.Table.Server.DatabaseObjects.User
import com.Table.Server.DatabaseObjects.UserCredentials
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.*
import org.junit.Before
import kotlin.test.*
import io.ktor.server.testing.*

class GroupTest {
    val dbConnector: DatabaseConnector = DatabaseConnector()

    var loggedInUser: User = User(null, "maxi", "mustermann", "password")
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
    fun testAddGroupValid() {
        withTestApplication({ module(testing = true) }) {
            val create_group_request = handleRequest(HttpMethod.Post, "/group/create") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(GroupCredentials(loggedInUser.username, "Mygroup", null)))
            }

            create_group_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
    @Test
    fun testAddGroupInValid() {
        withTestApplication({ module(testing = true) }) {
            val create_group_request = handleRequest(HttpMethod.Post, "/group/create") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString("Not valid json"))
            }

            create_group_request.apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
            }
        }
    }
    @Test
    fun testAddGroupAndAddMember() {
        withTestApplication({ module(testing = true) }) {
            val create_group_request = handleRequest(HttpMethod.Post, "/group/create") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(GroupCredentials(loggedInUser.username, "Mygroup", null)))
            }

            create_group_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
            val add_member_request = handleRequest(HttpMethod.Post, "/group/addmember") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(GroupCredentials(loggedInUser.username, "Mygroup", 1)))
            }

            add_member_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun testAddMemberToNonExistantGroup() {
        withTestApplication({ module(testing = true) }) {
            val add_member_request = handleRequest(HttpMethod.Post, "/group/addmember") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(GroupCredentials(loggedInUser.username, "Mygroup", 1)))
            }

            add_member_request.apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
            }
        }
    }

    @Test
    fun testAddNonExistantMemberToGroup() {
        withTestApplication({ module(testing = true) }) {
            val create_group_request = handleRequest(HttpMethod.Post, "/group/create") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(GroupCredentials(loggedInUser.username, "Mygroup", null)))
            }

            create_group_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
            val add_member_request = handleRequest(HttpMethod.Post, "/group/addmember") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(GroupCredentials("User That does not exist", "Mygroup", 1)))
            }

            add_member_request.apply {
                assertEquals(HttpStatusCode.Conflict, response.status())
            }
        }
    }

   /* @Test
    fun testAddGroupMemberValid() {
        withTestApplication({ module(testing = true) }) {
            val update_bio_request = handleRequest(HttpMethod.Post, "/group/addmember") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(GroupCredentials(loggedInUser.username, "Mygroup", null)))
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
    @Test
    fun testAddGroupMemberInValid() {
        withTestApplication({ module(testing = true) }) {
            val update_bio_request = handleRequest(HttpMethod.Post, "/group/addmemebr") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString("Not valid json"))
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
            }
        }
    }*/


}


