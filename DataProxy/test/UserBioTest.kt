package com.Table.Server

import com.Table.Server.DatabaseObjects.Bio
import com.Table.Server.DatabaseObjects.User
import com.Table.Server.DatabaseObjects.UserCredentials
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.*
import org.junit.Before
import kotlin.test.*
import io.ktor.server.testing.*

class UserBioTest {
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
                setBody("{\"id\": 100, \"age\": \"16Invalid\"}") // age not parseable
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
            }
        }
    }

    @Test
    fun testGetUserBioValid(){
        val mapper = jacksonObjectMapper()
        withTestApplication({ module(testing = true) }) {
            val update_bio_request = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser.bio_id, "maxi_muster", 16, "Graz", true, false, true, false)))
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val get_bio_request = handleRequest(HttpMethod.Get, "/user/getBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
            }

            get_bio_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val tree: JsonNode = mapper.readTree(response.content)
                    val mapperConvertValue:Bio = mapper.convertValue<Bio>(tree)
                    assertTrue(mapperConvertValue.user_name == "maxi_muster")
                }
                catch (e:Exception)
                {
                    println("Testcase Failed")
                    assert(false)
                }
            }

        }
    }

    @Test
    fun testInvalidUserUpdateBio(){
        withTestApplication({ module(testing = true) }) {
            val update_bio_request = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, "Bearer invalid_token")
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser.bio_id, "maxi_muster", 16, "Graz", true, false, true, false)))
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun testUpdateBioValidWithoutId(){
        withTestApplication({ module(testing = true) }) {
            val update_bio_request = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(null, "maxi_muster", 16, "Graz", true, false, true, false)))
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun testGetUserBioValidAtStart(){
        val mapper = jacksonObjectMapper()
        withTestApplication({ module(testing = true) }) {

            val get_bio_request = handleRequest(HttpMethod.Get, "/user/getBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
            }
            get_bio_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val tree: JsonNode = mapper.readTree(response.content)
                    val mapperConvertValue:Bio = mapper.convertValue<Bio>(tree)
                    assertTrue(mapperConvertValue.user_name == null)
                }
                catch (e:Exception)
                {
                    println("Testcase Failed")
                    assert(false)
                }
            }
        }
    }

    @Test
    fun testGetUserBioInvalid(){
        val mapper = jacksonObjectMapper()
        withTestApplication({ module(testing = true) }) {
            val update_bio_request = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser.bio_id, "maxi_muster", 16, "Graz", true, false, true, false)))
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val get_bio_request = handleRequest(HttpMethod.Get, "/user/getBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                //addHeader(HttpHeaders.Authorization, jwtToken!!)
            }

            get_bio_request.apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }

        }
    }

    @Test
    fun testGetUserBioValidFields(){
        val mapper = jacksonObjectMapper()
        withTestApplication({ module(testing = true) }) {
            val update_bio_request = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser.bio_id, "maxi_muster", 16, "Graz", true, false, true, false)))
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val get_bio_request = handleRequest(HttpMethod.Get, "/user/getBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
            }

            get_bio_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val tree: JsonNode = mapper.readTree(response.content)
                    val mapperConvertValue:Bio = mapper.convertValue<Bio>(tree)
                    assertTrue(mapperConvertValue.user_name == "maxi_muster")
                    assertTrue(mapperConvertValue.age == 16)
                    assertTrue(mapperConvertValue.city == "Graz")
                    assertTrue(mapperConvertValue.card_games == true)
                    assertTrue(mapperConvertValue.board_games == false)
                }
                catch (e:Exception)
                {
                    println("Testcase Failed")
                    assert(false)
                }
            }

            val update_bio_request2 = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser.bio_id, "maxi_muster", 21, "Wien", false, true, true, false)))
            }

            update_bio_request2.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val get_bio_request2 = handleRequest(HttpMethod.Get, "/user/getBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
            }

            get_bio_request2.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val tree: JsonNode = mapper.readTree(response.content)
                    val mapperConvertValue:Bio = mapper.convertValue<Bio>(tree)
                    assertTrue(mapperConvertValue.user_name == "maxi_muster")
                    assertTrue(mapperConvertValue.age == 21)
                    assertTrue(mapperConvertValue.city == "Wien")
                    assertTrue(mapperConvertValue.card_games == false)
                    assertTrue(mapperConvertValue.board_games == true)
                }
                catch (e:Exception)
                {
                    println("Testcase Failed")
                    assert(false)
                }
            }
        }
    }


}


