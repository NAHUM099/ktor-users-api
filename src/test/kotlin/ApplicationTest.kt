package com.ktor

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

        @Test
        fun `GET health should return 200`() = testApplication {
            application {
                module()
            }

            val response = client.get("/health")

            assertEquals(HttpStatusCode.OK, response.status)
        }
    }
//    @Test
//    fun testRoot() = testApplication {
//        application {
//            module()
//        }
//        client.get("/").apply {
//            assertEquals(HttpStatusCode.OK, status)
//        }
//    }
//
//}
