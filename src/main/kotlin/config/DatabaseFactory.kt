//package config
//
//import com.ktor.tables.CategoriesTable
//import com.ktor.tables.ProductTable
//import com.zaxxer.hikari.HikariConfig
//import com.zaxxer.hikari.HikariDataSource
//import io.github.cdimascio.dotenv.dotenv
//import org.jetbrains.exposed.sql.Database
//import org.jetbrains.exposed.sql.SchemaUtils
//import org.jetbrains.exposed.sql.transactions.transaction
//import tables.UsersTable
//import javax.sql.DataSource
//
//
//object DatabaseFactory {
//
//    private val dotenv = dotenv()
//
//    private val dbUrl: String =
//        dotenv["DB_URL"]
//            ?: System.getenv("DB_URL")
//            ?: throw IllegalStateException("DB_URL no está configurado")
//
//    private val dbUser: String =
//        dotenv["DB_USER"]
//            ?: System.getenv("DB_USER")
//            ?: throw IllegalStateException("DB_USER no está configurado")
//
//    private val dbPassword: String =
//        dotenv["DB_PASSWORD"]
//            ?: System.getenv("DB_PASSWORD")
//            ?: throw IllegalStateException("DB_PASSWORD no está configurado")
//
//    fun create(): DataSource {
//
//        val config = HikariConfig().apply {
//            jdbcUrl = dbUrl
//            username = dbUser
//            password = dbPassword
//            driverClassName = "org.postgresql.Driver"
//            maximumPoolSize = 5
//        }
//
//        return HikariDataSource(config)
//    }
//
////    fun create() : DataSource {
////
////
////        val dotenv = dotenv()
////
////        val config = HikariConfig().apply {
////            jdbcUrl = dotenv["DB_URL"]
////            username = dotenv["DB_USER"]
////            password = dotenv["DB_PASSWORD"]
////            driverClassName = "org.postgresql.Driver"
////            maximumPoolSize = 5
////        }
////
////
////        return HikariDataSource(config)
////    }
//
//    fun init() {
////        val dotenv = dotenv()
////
////        // Conectar Exposed a la base de datos
////        Database.connect(
////            url = dotenv["DB_URL"]!!,
////            driver = "org.postgresql.Driver",
////            user = dotenv["DB_USER"]!!,
////            password = dotenv["DB_PASSWORD"]!!
////        )
//    Database.connect(create())
//        // Crear tablas si no existen
//        transaction {
//            SchemaUtils.create(UsersTable)
//            println(" Tabla users creada/verificada")
//        }
//
//        transaction {
//            SchemaUtils.create(ProductTable)
//            println("tabla productos creada/verficad")
//        }
//
//        transaction {
//            SchemaUtils.create(CategoriesTable)
//            println("tabla categories creada/verificada")
//        }
//    }
//}
//
//


package config

import com.ktor.tables.CategoriesTable
import com.ktor.tables.ProductTable
import com.ktor.tables.UsersTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import tables.UsersTable
import javax.sql.DataSource

object DatabaseFactory {

    // Intentamos cargar .env solo si existe
    private val env = try { dotenv() } catch (e: Exception) { null }

    // Saber si estamos en producción
    private fun isProduction(): Boolean = System.getenv("ENVIRONMENT") == "production"

    // Función helper para leer variables de entorno
    private fun getEnv(name: String): String {
        return System.getenv(name) ?: env?.get(name)
        ?: throw IllegalStateException("La variable $name no está configurada ni en .env ni en entorno")
    }

    private fun getDbUrl(): String = getEnv("DB_URL")
    private fun getDbUser(): String = getEnv("DB_USER")
    private fun getDbPassword(): String = getEnv("DB_PASSWORD")

    fun create(): DataSource {
        val config = HikariConfig().apply {
            jdbcUrl = getDbUrl()
            username = getDbUser()
            password = getDbPassword()
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 5
        }
        return HikariDataSource(config)
    }

    fun init() {
        // Conectar Exposed a la base de datos
        Database.connect(create())

        // Crear tablas si no existen
        transaction {
            SchemaUtils.create(UsersTable)
            SchemaUtils.create(ProductTable)
            SchemaUtils.create(CategoriesTable)
            println("Tablas creadas/verificadas")
        }
    }
}