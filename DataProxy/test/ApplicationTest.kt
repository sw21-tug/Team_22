package com.Table.Server


import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.http.*
import kotlin.test.*
import io.ktor.server.testing.*
import org.junit.Before

class ApplicationTest {

    val dbConnector:DatabaseConnector = DatabaseConnector()

    @Before
    fun initDB() {
        dbConnector.connectToDatabase()
    }

    @BeforeTest
    fun resetDB() {
        dbConnector.reset()
    }

    @Test
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("HELLO WORLD!", response.content)
            }
        }
    }

    @Test
    fun testIfUserIsInserted() {
        withTestApplication({ module(testing = true) }) {
            val usernames: List<String> = listOf("testuser1", "testuser2", "testuser3")
            val emails: List<String> = listOf("test1@test.at", "test2@test.at", "test3@test.at")
            val mapper = jacksonObjectMapper()
            for (i in usernames.indices) {
                val addUser = handleRequest(HttpMethod.Post, "/user/register") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody("{\"username\": \"${usernames[i]}\", \"email\": \"${emails[i]}\", \"password\": \"pw\"}")
                }
                addUser.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    try {
                        val tree: JsonNode = mapper.readTree(response.content)
                        val mapperConvertValue = jacksonObjectMapper().convertValue<Int>(tree.get("id"))
                        assertTrue(mapperConvertValue!=0)
                        handleRequest(HttpMethod.Get, "/user/${mapperConvertValue}") {
                            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        }.apply {
                            val usertree: JsonNode = mapper.readTree(response.content)
                            val mapperUsername = jacksonObjectMapper().convertValue<String>(usertree.get("username"))
                            assertEquals(mapperUsername, usernames[i])
                        }
                    }
                    catch (e:Exception)
                    {
                        println("Mission failed.")
                        assert(false)
                    }
                }
            }
        }
    }

    @Test
    fun testIfInvalidUserJsonRecognized() {
        withTestApplication({ module(testing = true) }) {

            val addUser = handleRequest(HttpMethod.Post, "/user/register") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("{\"usern1ame\": \"testname\", \"email\": \"testmail\"}")
            }
            addUser.apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
            }
        }
    }

    @Test
    fun testUserRegistrationInsert() {
        withTestApplication({ module(testing = true) }) {
            val usernames: List<String> = listOf("testuser1", "testuser2", "testuser3")
            val emails: List<String> = listOf("test1@test.at", "test2@test.at", "test3@test.at")
            val mapper = jacksonObjectMapper()
            val list: MutableList<Int> = mutableListOf()
            for (i in usernames.indices) {
                val addUser = handleRequest(HttpMethod.Post, "/user/register") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody("{\"username\": \"${usernames[i]}\", \"email\": \"${emails[i]}\", \"password\": \"pw\"}")
                }

                addUser.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    try {
                        val tree: JsonNode = mapper.readTree(response.content)
                        val mapperConvertValue = jacksonObjectMapper().convertValue<Int>(tree.get("id"))
                        list.add(mapperConvertValue)
                        assertTrue(mapperConvertValue!=0)
                    }
                    catch (e:Exception)
                    {
                        println("Mission failed.")
                        assert(false)
                    }
                }
            }

            /*
            ** (removed as out of the scope of this testcase, for later usage)

            val getAllUsers = handleRequest(HttpMethod.Get, "/user")
            getAllUsers.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                var mapper = jacksonObjectMapper()
                val result: List<User> = mapper.readValue(response.content.toString())
                assertEquals(result.size, usernames.size)
                for (i in usernames.indices) {
                    assertTrue { }
                }
            }
     */
        }
    }

    @Test
    fun testUserDoubleRegistrationInsert() {
        withTestApplication({ module(testing = true) }) {
            val usernames: List<String> = listOf("testuser1", "testuser1", "testuser2")
            val emails: List<String> = listOf("test1@test.at", "test2@test.at", "test1@test.at")
            for (i in usernames.indices) {
                val addUser = handleRequest(HttpMethod.Post, "/user/register") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody("{\"username\": \"${usernames[i]}\", \"email\": \"${emails[i]}\", \"password\": \"pw\"}")
                }

                addUser.apply {
                    if (i == 0) {
                        assertEquals(HttpStatusCode.OK, response.status())
                    } else {
                        assertEquals(HttpStatusCode.Conflict, response.status())
                    }
                }
            }
        }
    }

    @Test
    fun testRegistrationInvalidEmail() {
        withTestApplication({ module(testing = true) }) {
            val usernames: List<String> = listOf("testuser1")
            val emails: List<String> = listOf("test1test.at")
            for (i in usernames.indices) {
                val addUser = handleRequest(HttpMethod.Post, "/user/register") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody("{\"username\": \"${usernames[i]}\", \"email\": \"${emails[i]}\", \"password\": \"pw\"}")
                }

                addUser.apply {
                        assertEquals(HttpStatusCode.NotAcceptable, response.status())
                }
            }
        }
    }

    @Test
    fun testRegistrationInvalidEmail2() {
        withTestApplication({ module(testing = true) }) {
            val usernames: List<String> = listOf("testuser1")
            val emails: List<String> = listOf("test1test@at")
            for (i in usernames.indices) {
                val addUser = handleRequest(HttpMethod.Post, "/user/register") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody("{\"username\": \"${usernames[i]}\", \"email\": \"${emails[i]}\", \"password\": \"pw\"}")
                }

                addUser.apply {
                    assertEquals(HttpStatusCode.NotAcceptable, response.status())
                }
            }
        }
    }

    @Test
    fun testLoginIsInvalid(){
        withTestApplication({ module(testing = true) }) {
            val username ="Max Mustermann"
            val password ="1234567"
            val login_request = handleRequest(HttpMethod.Post, "/user/login"){
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("{\"username\": \"${username}\", \"password\": \"${password}\"}");
            }
            login_request.apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun testLoginIsValid() {
        withTestApplication({ module(testing = true) }) {
            val username = "Max Mustermann"
            val email = "muster@gmail.com"
            val password = "123456"
            val addUser = handleRequest(HttpMethod.Post, "/user/register") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("{\"username\": \"${username}\", \"email\": \"${email}\", \"password\": \"${password}\"}")
            }
            addUser.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val login_request = handleRequest(HttpMethod.Post, "/user/login") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("{\"username\": \"${username}\", \"password\": \"${password}\"}");
            }
            login_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun TestLoginMultipleTimes() {
        withTestApplication({ module(testing = true) }) {
            val username = "Max Mustermann"
            val email = "muster@gmail.com"
            val password = "123456"
            val addUser = handleRequest(HttpMethod.Post, "/user/register") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("{\"username\": \"${username}\", \"email\": \"${email}\", \"password\": \"${password}\"}")
            }
            addUser.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val login_request = handleRequest(HttpMethod.Post, "/user/login") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("{\"username\": \"${username}\", \"password\": \"${password}\"}");
            }
            login_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
            val login_request2 = handleRequest(HttpMethod.Post, "/user/login") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("{\"username\": \"${username}\", \"password\": \"${password}\"}");
            }
            login_request2.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
            val login_request3 = handleRequest(HttpMethod.Post, "/user/login") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("{\"username\": \"${username}\", \"password\": \"${password}\"}");
            }
            login_request3.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

        }
    }

    @Test
    fun testInvalidPassword(){
        withTestApplication({ module(testing = true) }) {
            val username = "Max Mustermann"
            val email = "muster@gmail.com"
            val password = "123456"
            val password_invalid = "invalid"
            val addUser = handleRequest(HttpMethod.Post, "/user/register") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("{\"username\": \"${username}\", \"email\": \"${email}\", \"password\": \"${password}\"}")
            }
            addUser.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val login_request = handleRequest(HttpMethod.Post, "/user/login") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("{\"username\": \"${username}\", \"password\": \"${password_invalid}\"}");
            }
            login_request.apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }

    @Test
    fun testAuthentication(){
        withTestApplication({ module(testing = true) }) {
            val username = "Max Mustermann"
            val email = "muster@gmail.com"
            val password = "123456"
            val addUser = handleRequest(HttpMethod.Post, "/user/register") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("{\"username\": \"${username}\", \"email\": \"${email}\", \"password\": \"${password}\"}")
            }
            addUser.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val login_request = handleRequest(HttpMethod.Post, "/user/login") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("{\"username\": \"${username}\", \"password\": \"${password}\"}");
            }
            val mapper = jacksonObjectMapper()
            var token = ""
            login_request.apply {
                //assertEquals(HttpStatusCode.OK, response.status())
                val tokentree: JsonNode = mapper.readTree(response.content)
                val mapperJWT = jacksonObjectMapper().convertValue<String>(tokentree.get("jwtToken"))
                token = mapperJWT
            }
            val authentication_request = handleRequest(HttpMethod.Get,"/testauthentication/") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, "Bearer " + token)
            }

            authentication_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

        }
    }

    @Test
    fun testMissingPassword(){
        withTestApplication({ module(testing = true) }) {
            val username = "Max Mustermann"
            val email = "muster@gmail.com"
            val password = "123456"
            val addUser = handleRequest(HttpMethod.Post, "/user/register") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("{\"username\": \"${username}\", \"email\": \"${email}\", \"password\": \"${password}\"}")
            }
            addUser.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val login_request = handleRequest(HttpMethod.Post, "/user/login") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("{\"username\": \"${username}\", }");
            }
            login_request.apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
            }
        }
    }

    @Test
    fun testMissingUsername(){
        withTestApplication({ module(testing = true) }) {
            val username = "Max Mustermann"
            val email = "muster@gmail.com"
            val password = "123456"
            val addUser = handleRequest(HttpMethod.Post, "/user/register") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("{\"username\": \"${username}\", \"email\": \"${email}\", \"password\": \"${password}\"}")
            }
            addUser.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val login_request = handleRequest(HttpMethod.Post, "/user/login") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("{\"password\": \"${password}\"}");
            }
            login_request.apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
            }
        }
    }

    @Test
    fun testMissingLoginData(){
        withTestApplication({ module(testing = true) }) {
            val username = "Max Mustermann"
            val email = "muster@gmail.com"
            val password = "123456"
            val addUser = handleRequest(HttpMethod.Post, "/user/register") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("{\"username\": \"${username}\", \"email\": \"${email}\", \"password\": \"${password}\"}")
            }
            addUser.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val login_request = handleRequest(HttpMethod.Post, "/user/login") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody("{}");
            }
            login_request.apply {
                assertEquals(HttpStatusCode.NotAcceptable, response.status())
            }
        }
    }

    @Test
    fun testAuthenticationWithoutToken(){
        withTestApplication({ module(testing = true) }) {
            val authentication_request = handleRequest(HttpMethod.Get,"/testauthentication/") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }
            authentication_request.apply {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }
    
}
