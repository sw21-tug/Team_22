package com.Table.Server


import io.ktor.http.*
import kotlin.test.*
import io.ktor.server.testing.*

class ApplicationTest {
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
    fun testUserRegistrationInsert() {
        withTestApplication({ module(testing = true) }) {
            val usernames: List<String> = listOf("testuser1", "testuser2", "testuser3")
            val emails: List<String> = listOf("test1@test.at", "test2@test.at", "test3@test.at")
            for (i in usernames.indices) {
                val addUser = handleRequest(HttpMethod.Post, "/user/register") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody("{'username': '${usernames[i]}', 'email': '${emails[i]}', 'password': 'pw'}")
                }

                addUser.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    assertEquals("Successfully created user ${usernames[i]}", response.content)
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
}
