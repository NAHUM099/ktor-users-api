package config

import com.ktor.tables.CategoriesTable
import com.ktor.tables.ProductTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import tables.UsersTable
import javax.sql.DataSource


object DatabaseFactory {

    private val dotenv =  dotenv {
        ignoreIfMissing = true
    }

    private val dbUrl: String =
        System.getenv("DB_URL")
            ?: dotenv["DB_URL"]
            ?: throw IllegalStateException("DB_URL no está configurado")

    private val dbUser: String =
        System.getenv("DB_USER")
            ?: dotenv["DB_USER"]
            ?: throw IllegalStateException("DB_USER no está configurado")

    private val dbPassword: String =
        System.getenv("DB_PASSWORD")
            ?: dotenv["DB_PASSWORD"]
            ?: throw IllegalStateException("DB_PASSWORD no está configurado")

    fun create(): DataSource {

        val config = HikariConfig().apply {
            jdbcUrl = dbUrl
            username = dbUser
            password = dbPassword
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 5
        }

        return HikariDataSource(config)
    }

//    fun create() : DataSource {
//
//
//        val dotenv = dotenv()
//
//        val config = HikariConfig().apply {
//            jdbcUrl = dotenv["DB_URL"]
//            username = dotenv["DB_USER"]
//            password = dotenv["DB_PASSWORD"]
//            driverClassName = "org.postgresql.Driver"
//            maximumPoolSize = 5
//        }
//
//
//        return HikariDataSource(config)
//    }

    fun init() {
//        val dotenv = dotenv()
//
//        // Conectar Exposed a la base de datos
//        Database.connect(
//            url = dotenv["DB_URL"]!!,
//            driver = "org.postgresql.Driver",
//            user = dotenv["DB_USER"]!!,
//            password = dotenv["DB_PASSWORD"]!!
//        )
    Database.connect(create())
        // Crear tablas si no existen
        transaction {
            SchemaUtils.create(UsersTable)
            println(" Tabla users creada/verificada")
        }

        transaction {
            SchemaUtils.create(ProductTable)
            println("tabla productos creada/verficad")
        }

        transaction {
            SchemaUtils.create(CategoriesTable)
            println("tabla categories creada/verificada")
        }
    }
}


