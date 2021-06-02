package com.Table.Server

import com.Table.Server.DatabaseObjects.Bio
import com.Table.Server.DatabaseObjects.User
import com.Table.Server.DatabaseObjects.UserCredentials
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchPreferencesTest {

    val dbConnector: DatabaseConnector = DatabaseConnector()

    var loggedInUser: User = User(null, "maxi", "mustermann", "password")
    var jwtToken: String? = null
    val jwthandler = JWTHandler()

    var loggedInUser2: User = User(null, "user", "useremail", "password")
    var jwtToken2: String? = null
    val jwthandler2 = JWTHandler()



    @Before
    fun initDB() {
        dbConnector.reset()
        dbConnector.connectToDatabase()
        // me
        val id = dbConnector.insertUser(loggedInUser)
        jwtToken = "Bearer " + jwthandler.generateLoginToken(dbConnector.getUserForAuth(UserCredentials(loggedInUser.username, loggedInUser.password, null))[0])
        loggedInUser = dbConnector.getUserById(id)[0]
        // other user
        val id2: Int = dbConnector.insertUser(loggedInUser2)
        jwtToken2 = "Bearer " + jwthandler2.generateLoginToken(dbConnector.getUserForAuth(UserCredentials(loggedInUser2.username, loggedInUser2.password, null))[0])
        loggedInUser2 = dbConnector.getUserById(id2)[0]

    }

    @Test
    fun testSimpleSearch() {
        val mapper = jacksonObjectMapper()
        withTestApplication({ module(testing = true) }) {
            val update_bio_request = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser.bio_id, "maxi_muster", 16, "Graz", true, true, true, true)))
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val search_request = handleRequest(HttpMethod.Post, "/user/getUsersByPreferences") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(SearchPreferences(16, 30,"Graz", true, true, true, true,loggedInUser.username)))
            }

            search_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val tree: JsonNode = mapper.readTree(response.content)
                    val mapperConvertValue:List<String> = mapper.convertValue<List<String>>(tree)
                    assertTrue(mapperConvertValue.isEmpty())
                    //print(tree)
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
    fun incorrectCity() {
        val mapper = jacksonObjectMapper()
        withTestApplication({ module(testing = true) }) {
            val update_bio_request = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken2!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser2.bio_id, "maxi_muster", 16, "Graz", true, false, true, false)))
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val search_request = handleRequest(HttpMethod.Post, "/user/getUsersByPreferences") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(SearchPreferences(16, 30,"Wien", true, false, true, false,loggedInUser.username)))
            }

            search_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val tree: JsonNode = mapper.readTree(response.content)
                    val mapperConvertValue:List<String> = mapper.convertValue<List<String>>(tree)
                    assertTrue(mapperConvertValue.isEmpty())
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
    fun simpleAgeRangeTest() {
        val mapper = jacksonObjectMapper()
        withTestApplication({ module(testing = true) }) {
            val update_bio_request = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken2!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser2.bio_id, "maxi_muster", 20,  "Graz", true, true, true, true)))
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val search_request = handleRequest(HttpMethod.Post, "/user/getUsersByPreferences") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(SearchPreferences(18, 27, "Graz", true, true, true, true,loggedInUser.username)))
            }

            search_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val tree: JsonNode = mapper.readTree(response.content)
                    val mapperConvertValue:List<String> = mapper.convertValue<List<String>>(tree)
                    assertTrue(mapperConvertValue[0] == "user")
                    //print(tree)
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
    fun searchYourself() {
        val mapper = jacksonObjectMapper()
        withTestApplication({ module(testing = true) }) {

            val update_bio_request = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser.bio_id, "maxi_muster", 20,  "Graz", true, true, true, true)))
            }

            val update_bio_request2 = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken2!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser2.bio_id, "user2", 20,  "Graz", true, true, true, true)))
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            update_bio_request2.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val search_request = handleRequest(HttpMethod.Post, "/user/getUsersByPreferences") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(SearchPreferences(18, 27, "Graz", true, true, true, true,loggedInUser.username)))
            }

            search_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val tree: JsonNode = mapper.readTree(response.content)
                    val mapperConvertValue:List<String> = mapper.convertValue<List<String>>(tree)
                    //assertTrue(mapperConvertValue[0] == "maxi")
                    //assertTrue(mapperConvertValue[1] == "user")
                    assertTrue(mapperConvertValue.size != 2)
                    //print(tree)
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