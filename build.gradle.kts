plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.ktor"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

dependencies {

    // --- KTOR ---
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)

    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.call.logging)

    // --- DATABASE ---
    implementation(libs.postgresql)
    implementation(libs.hikaricp)

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)

    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    // --- DI ---
    implementation("io.insert-koin:koin-core:3.1.5")
    implementation("io.insert-koin:koin-ktor:3.1.5")

    // --- SECURITY ---
    implementation("com.auth0:java-jwt:4.4.0")
    implementation("org.mindrot:jbcrypt:0.4")

    // --- LOGGING ---
    implementation(libs.logback.classic)

    // --- TESTING ---
    testImplementation(libs.h2)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)

}
ktor {
    fatJar {
        archiveFileName.set("app.jar")
    }
}




//    implementation(libs.ktor.server.core)
//    implementation(libs.ktor.server.host.common)
//    implementation(libs.ktor.server.status.pages)
//    implementation(libs.ktor.serialization.kotlinx.json)
//    implementation(libs.ktor.server.netty)
//    implementation(libs.ktor.server.content.negotiation)
//
//
//
//    // database
//    implementation(libs.postgresql)
//    implementation("com.zaxxer:HikariCP:5.1.0")
//    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
//    implementation("org.postgresql:postgresql:42.7.3")
//
//
//
//
//    implementation("org.jetbrains.exposed:exposed-core:0.45.0")
//    implementation("org.jetbrains.exposed:exposed-jdbc:0.45.0")
//    implementation("io.insert-koin:koin-core:3.1.5")
//    implementation("io.insert-koin:koin-ktor:3.1.5")
//
//
//
//
//
//    implementation("com.auth0:java-jwt:4.4.0")
//    implementation("org.mindrot:jbcrypt:0.4")
//
//
//
////    implementation("io.ktor:ktor-server-auth:3.3.3")
////    implementation("io.ktor:ktor-server-auth-jwt:3.3.3")
////
////
////    implementation("io.ktor:ktor-server-cors:3.3.3")
//
//
//    //logging
//    implementation(libs.logback.classic)
//    implementation("ch.qos.logback:logback-classic:1.4.14")
////    implementation("io.ktor:ktor-server-call-logging")
//
//
//    //testing
//    testImplementation(libs.h2)
//    testImplementation(libs.ktor.server.test.host)
//    testImplementation(libs.kotlin.test.junit)
//    testImplementation(libs.ktor.server.test)
//    implementation(libs.ktor.server.core)
//
//
//
//}
