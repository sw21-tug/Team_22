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
        dbConnector.insertUser(User(null, "generic1", "genericmail1", "password"))
        dbConnector.insertUser(User(null, "generic2", "genericmail2", "password"))
        //Add some generic users
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
            var group_id = 0
            val group_list_request = handleRequest(HttpMethod.Get, "/group/getgrouplist") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
            }

            group_list_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val mapper = jacksonObjectMapper()
                    val tree: JsonNode = mapper.readTree(response.content)
                    val mapperConvertValue: List<Pair<Int, String>> = mapper.convertValue(tree)
                    assertEquals(mapperConvertValue.size, 1)
                    group_id = mapperConvertValue[0].first
                } catch (e: Exception) {
                    println("Testcase Failed")
                    assert(false)
                }
            }

            val add_member_request = handleRequest(HttpMethod.Post, "/group/addmember") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(GroupCredentials("generic1", "Mygroup", group_id)))
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
            var group_id= 0

            val group_list_request = handleRequest(HttpMethod.Get, "/group/getgrouplist") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
            }

            group_list_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val mapper = jacksonObjectMapper()
                    val tree: JsonNode = mapper.readTree(response.content)
                    val mapperConvertValue: List<Pair<Int, String>> = mapper.convertValue(tree)
                    assertEquals(mapperConvertValue.size, 1)
                    group_id = mapperConvertValue[0].first
                } catch (e: Exception) {
                    println("Testcase Failed")
                    assert(false)
                }
            }


            val add_member_request = handleRequest(HttpMethod.Post, "/group/addmember") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(GroupCredentials("User That does not exist", "Mygroup", group_id)))
            }

            add_member_request.apply {
                assertEquals(HttpStatusCode.Conflict, response.status())
            }
        }
    }

    @Test
    fun getfilledGroupList() {
        withTestApplication({ module(testing = true) }) {
            val create_group_request1 = handleRequest(HttpMethod.Post, "/group/create") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(GroupCredentials(loggedInUser.username, "Mygroup1", null)))
            }
            create_group_request1.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
            val create_group_request2 = handleRequest(HttpMethod.Post, "/group/create") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(GroupCredentials(loggedInUser.username, "Mygroup2", null)))
            }
            create_group_request2.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val create_group_request3 = handleRequest(HttpMethod.Post, "/group/create") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(GroupCredentials(loggedInUser.username, "Mygroup1", null)))
            }
            create_group_request3.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val group_list_request = handleRequest(HttpMethod.Get, "/group/getgrouplist") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
            }

            group_list_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val mapper = jacksonObjectMapper()
                    val tree: JsonNode = mapper.readTree(response.content)
                    val mapperConvertValue:List<Pair<Int,String>> = mapper.convertValue(tree)
                    assertEquals(mapperConvertValue.size,3)
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
    fun getEmptyGroupList() {
        withTestApplication({ module(testing = true) }) {
            val group_list_request = handleRequest(HttpMethod.Get, "/group/getgrouplist") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
            }
            group_list_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val mapper = jacksonObjectMapper()
                    val tree: JsonNode = mapper.readTree(response.content)
                    val mapperConvertValue:List<Pair<Int,String>> = mapper.convertValue(tree)
                    assertEquals(mapperConvertValue.isEmpty(),true)
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
    fun testAddingMemberTwice() {
        withTestApplication({ module(testing = true) }) {
            val create_group_request1 = handleRequest(HttpMethod.Post, "/group/create") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(GroupCredentials(loggedInUser.username, "Mygroup1", null)))
            }
            create_group_request1.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
            var group_id = 0
            val group_list_request = handleRequest(HttpMethod.Get, "/group/getgrouplist") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
            }

            group_list_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val mapper = jacksonObjectMapper()
                    val tree: JsonNode = mapper.readTree(response.content)
                    val mapperConvertValue: List<Pair<Int, String>> = mapper.convertValue(tree)
                    assertEquals(mapperConvertValue.size, 1)
                    group_id = mapperConvertValue[0].first
                } catch (e: Exception) {
                    println("Testcase Failed")
                    assert(false)
                }
            }

            val add_member_request = handleRequest(HttpMethod.Post, "/group/addmember") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(GroupCredentials("generic1", "Mygroup1", group_id)))
            }
            add_member_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
            val add_member_request2 = handleRequest(HttpMethod.Post, "/group/addmember") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(GroupCredentials("generic1", "Mygroup1", group_id)))
            }
            add_member_request2.apply {
                assertEquals(HttpStatusCode.Conflict, response.status())
            }
        }
    }


    @Test
    fun testAuthenticationAddingDeletingAndGettingUserlist() {
        withTestApplication({ module(testing = true) }) {
            val username = "MaxMustermann"
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
            var group_id = 0
            val create_group_request1 = handleRequest(HttpMethod.Post, "/group/create") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, "Bearer " + token)
                setBody(jacksonObjectMapper().writeValueAsString(GroupCredentials(username, "Mygroup1", null)))
            }
            create_group_request1.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val group_list_request = handleRequest(HttpMethod.Get, "/group/getgrouplist") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, "Bearer " + token)
            }

            group_list_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val mapper = jacksonObjectMapper()
                    val tree: JsonNode = mapper.readTree(response.content)
                    val mapperConvertValue: List<Pair<Int, String>> = mapper.convertValue(tree)
                    assertEquals(mapperConvertValue.size, 1)
                    group_id = mapperConvertValue[0].first
                } catch (e: Exception) {
                    println("Testcase Failed")
                    assert(false)
                }
            }

                val add_member_request = handleRequest(HttpMethod.Post, "/group/addmember") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    addHeader(HttpHeaders.Authorization, "Bearer " + token)
                    setBody(
                        jacksonObjectMapper().writeValueAsString(
                            GroupCredentials(
                                loggedInUser.username,
                                "Mygroup1",
                                group_id
                            )
                        )
                    )
                }
                add_member_request.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }

                val group_list_request2 = handleRequest(HttpMethod.Get, "/group/getgrouplist") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    addHeader(HttpHeaders.Authorization, jwtToken!!)
                }

                group_list_request2.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    try {
                        val mapper = jacksonObjectMapper()
                        val tree: JsonNode = mapper.readTree(response.content)
                        val mapperConvertValue: List<Pair<Int, String>> = mapper.convertValue(tree)
                        assertEquals(mapperConvertValue.size, 1)
                        group_id = mapperConvertValue[0].first
                    } catch (e: Exception) {
                        println("Testcase Failed")
                        assert(false)
                    }

                }
                val delete_member_request = handleRequest(HttpMethod.Post, "/group/deletemember") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    addHeader(HttpHeaders.Authorization, "Bearer " + token)
                    setBody(
                        jacksonObjectMapper().writeValueAsString(
                            GroupCredentials(
                                loggedInUser.username,
                                "Mygroup1",
                                group_id
                            )
                        )
                    )
                }
                delete_member_request.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }

                val group_list_request3 = handleRequest(HttpMethod.Get, "/group/getgrouplist") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    addHeader(HttpHeaders.Authorization, jwtToken!!)
                }
                group_list_request3.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    try {
                        val mapper = jacksonObjectMapper()
                        val tree: JsonNode = mapper.readTree(response.content)
                        val mapperConvertValue: List<Pair<Int, String>> = mapper.convertValue(tree)
                        assertEquals(mapperConvertValue.size, 0)
                    } catch (e: Exception) {
                        println("Testcase Failed")
                        assert(false)
                    }

                }
            }
        }

        @Test
        fun testGetGroupUserNames()
        {
            withTestApplication({ module(testing = true) }) {
                val create_group_request = handleRequest(HttpMethod.Post, "/group/create") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    addHeader(HttpHeaders.Authorization, jwtToken!!)
                    setBody(
                        jacksonObjectMapper().writeValueAsString(
                            GroupCredentials(
                                loggedInUser.username,
                                "Mygroup",
                                null)))
                    }

                create_group_request.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                }
                var group_id = 0
                val group_list_request = handleRequest(HttpMethod.Get, "/group/getgrouplist") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    addHeader(HttpHeaders.Authorization, jwtToken!!)
                }

                group_list_request.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    try {
                        val mapper = jacksonObjectMapper()
                        val tree: JsonNode = mapper.readTree(response.content)
                        val mapperConvertValue: List<Pair<Int, String>> = mapper.convertValue(tree)
                        assertEquals(mapperConvertValue.size, 1)
                        group_id = mapperConvertValue[0].first
                    } catch (e: Exception) {
                        println("Testcase Failed")
                        assert(false)
                    }
                }
            val add_member_request = handleRequest(HttpMethod.Post, "/group/addmember") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(GroupCredentials("generic1", "Mygroup", group_id)))
            }

            add_member_request.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }

            val get_user_list = handleRequest(HttpMethod.Get, "/group/getUsersInGroup") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, jwtToken!!)
                setBody(jacksonObjectMapper().writeValueAsString(GroupCredentials(loggedInUser.username, "MyGroup", group_id)))
            }
            get_user_list.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                try {
                    val mapper = jacksonObjectMapper()
                    val tree: JsonNode = mapper.readTree(response.content)
                    val mapperConvertValue: List<String> = mapper.convertValue(tree)
                    assertEquals(mapperConvertValue.size, 2)
                    assertEquals(mapperConvertValue.contains(loggedInUser.username), true)
                    assertEquals(mapperConvertValue.contains("generic1"), true)
                }
                catch (e : java.lang.Exception)
                {
                    println("Testcase failed.")
                    assert(false)
                }
            }

        }

    }
}


