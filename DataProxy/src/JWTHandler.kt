package com.Table.Server

import com.Table.Server.DatabaseObjects.UserCredentials
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.*
import java.util.*

data class UserPrincipal(val username: String) : Principal

class JWTHandler {
    val issuer: String = "127.0.0.1"
    val audience: String ="L4GUser"
    val myRealm: String  = "L4GServer"
    val secret: String = "Hello this is a secret"
    val usernameString: String = "username"
    val encryptionAlgorithm: Algorithm = Algorithm.HMAC256(secret)
    val expirationDateFrame: Int = 72000000 //20 hours

    fun generateLoginToken(userCredentials: UserCredentials): String =
        JWT.create().withAudience(audience)
            .withIssuer(issuer)
            .withClaim(usernameString, userCredentials.username)
            .withExpiresAt(Date(System.currentTimeMillis() + expirationDateFrame))
            .sign(encryptionAlgorithm)

    fun getLoginVerifier() =
             JWT
            .require(encryptionAlgorithm)
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
}