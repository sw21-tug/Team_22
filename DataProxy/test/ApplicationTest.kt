package com.Table.Server


import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
    fun testIfUserIsInserted() {
        withTestApplication({ module(testing = true) }) {
            val usernames: List<String> = listOf("testuser1", "testuser2", "testuser3")
            val emails: List<String> = listOf("test1@test.at", "test2@test.at", "test3@test.at")
            var mapper = jacksonObjectMapper()
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
    fun testUserRegistrationInsert() {
        withTestApplication({ module(testing = true) }) {
            val usernames: List<String> = listOf("testuser1", "testuser2", "testuser3")
            val emails: List<String> = listOf("test1@test.at", "test2@test.at", "test3@test.at")
            var mapper = jacksonObjectMapper()
            var list: MutableList<Int> = mutableListOf()
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

}
