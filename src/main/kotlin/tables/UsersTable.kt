package tables

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {

    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val email = varchar("email", 150).uniqueIndex()
    val password = varchar("password", 100)
    val role = varchar("role", 20).default("USER")

    override val primaryKey = PrimaryKey(id)
}