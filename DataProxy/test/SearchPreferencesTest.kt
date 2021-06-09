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

    var loggedInUser3: User = User(null, "jon_doe", "jonemail", "password")
    var jwtToken3: String? = null
    val jwthandler3 = JWTHandler()

    var loggedInUser4: User = User(null, "jane_doe", "janeemail", "password")
    var jwtToken4: String? = null
    val jwthandler4 = JWTHandler()

    var loggedInUser5: User = User(null, "zemo", "zemoemail", "password")
    var jwtToken5: String? = null
    val jwthandler5 = JWTHandler()

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

        val id3: Int = dbConnector.insertUser(loggedInUser3)
        jwtToken3 = "Bearer " + jwthandler3.generateLoginToken(dbConnector.getUserForAuth(UserCredentials(loggedInUser3.username, loggedInUser3.password, null))[0])
        loggedInUser3 = dbConnector.getUserById(id3)[0]

        val id4: Int = dbConnector.insertUser(loggedInUser4)
        jwtToken4 = "Bearer " + jwthandler4.generateLoginToken(dbConnector.getUserForAuth(UserCredentials(loggedInUser4.username, loggedInUser4.password, null))[0])
        loggedInUser4 = dbConnector.getUserById(id4)[0]

        val id5: Int = dbConnector.insertUser(loggedInUser5)
        jwtToken5 = "Bearer " + jwthandler5.generateLoginToken(dbConnector.getUserForAuth(UserCredentials(loggedInUser5.username, loggedInUser5.password, null))[0])
        loggedInUser5 = dbConnector.getUserById(id5)[0]

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
                setBody(jacksonObjectMapper().writeValueAsString(SearchPreferences(16, 30,"Vienna", true, false, true, false,loggedInUser.username)))
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

    @Test
    fun simpleGameInterestTest() {
        val mapper = jacksonObjectMapper()
        withTestApplication({ module(testing = true) }) {
            val update_bio_request = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken2!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser2.bio_id, "user2", 20,  "Graz", false, false, false, true)))
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val search_request = handleRequest(HttpMethod.Post, "/user/getUsersByPreferences") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(SearchPreferences(18, 27, "Graz", true, false, false, false,loggedInUser.username)))
            }

            search_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val tree: JsonNode = mapper.readTree(response.content)
                    val mapperConvertValue:List<String> = mapper.convertValue<List<String>>(tree)
                    assertTrue(mapperConvertValue.isEmpty())
                    //assertTrue(mapperConvertValue[0] != "user")
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
    fun unauthorizedTest() {
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
               // addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(SearchPreferences(16, 30,"Graz", true, true, true, true,loggedInUser.username)))
            }

            search_request.apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }


    @Test
    fun multipleMatchingTest() {
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

            val update_bio_request3 = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken3!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser3.bio_id, "jon", 21,  "Graz", true, true, true, true)))
            }

            val update_bio_request4 = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken4!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser4.bio_id, "jane", 21,  "Vienna", true, true, true, true)))
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            update_bio_request2.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            update_bio_request3.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            update_bio_request4.apply {
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
                    assertTrue(mapperConvertValue.size == 2)
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
    fun noMatchingTest() {
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
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser2.bio_id, "user2", 20,  "Vienna", true, true, true, true)))
            }

            val update_bio_request3 = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken3!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser3.bio_id, "jon", 21,  "Vienna", true, true, true, true)))
            }

            val update_bio_request4 = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken4!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser4.bio_id, "jane", 21,  "Vienna", true, true, true, true)))
            }

            val update_bio_request5 = handleRequest(HttpMethod.Post, "/user/updateBio") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken5!!)
                setBody(jacksonObjectMapper().writeValueAsString(Bio(loggedInUser5.bio_id, "zemo", 21,  "Vienna", true, true, true, true)))
            }

            update_bio_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            update_bio_request2.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            update_bio_request3.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            update_bio_request4.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            update_bio_request5.apply {
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
}