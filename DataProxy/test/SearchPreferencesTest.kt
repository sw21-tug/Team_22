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

    @Before
    fun initDB() {
        dbConnector.reset()
        dbConnector.connectToDatabase()
        val id = dbConnector.insertUser(loggedInUser)
        jwtToken = "Bearer " + jwthandler.generateLoginToken(dbConnector.getUserForAuth(UserCredentials(loggedInUser.username, loggedInUser.password, null))[0])
        loggedInUser = dbConnector.getUserById(id)[0]
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
                setBody(jacksonObjectMapper().writeValueAsString(SearchPreferences(16, 30,"Graz", true, true, true, true)))
            }

            search_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val tree: JsonNode = mapper.readTree(response.content)
                    val mapperConvertValue:List<String> = mapper.convertValue<List<String>>(tree)
                    assertTrue(mapperConvertValue[0] == "maxi")
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
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser.bio_id, "maxi_muster", 16, "Graz", true, false, true, false)))
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val search_request = handleRequest(HttpMethod.Post, "/user/getUsersByPreferences") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(SearchPreferences(16, 30,"Wien", true, false, true, false)))
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
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser.bio_id, "maxi_muster", 20,  "Graz", true, true, true, true)))
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val search_request = handleRequest(HttpMethod.Post, "/user/getUsersByPreferences") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(SearchPreferences(18, 27, "Graz", true, true, true, true)))
            }

            search_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val tree: JsonNode = mapper.readTree(response.content)
                    val mapperConvertValue:List<String> = mapper.convertValue<List<String>>(tree)
                    assertTrue(mapperConvertValue[0] == "maxi")
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