package com.ktor

import com.ktor.auth.JwtConfig
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserRoutesTest {

    @Test
    fun testUsersWithFakeToken() = testApplication {
        application {
            module()
        }

        val response = client.get("/users?page=1&size=5") {
            header(HttpHeaders.Authorization, "Bearer fake_token")
        }

        // Puede ser 401 o 200 dependiendo tu lógica
        println(response.status)
    }
}