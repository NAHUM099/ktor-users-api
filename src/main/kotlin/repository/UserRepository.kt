package repository

import com.ktor.repository.interfaces.IUserRepository
import models.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import tables.UsersTable
import tables.UsersTable.role

class UserRepository : IUserRepository {


    override fun findAllPaginated(
        page: Int,
        size: Int,
        sortBy: String,
        order: String
    ): Pair<List<User>, Long> = transaction {

        val offset = ((page - 1) * size).toLong()

        val column = when (sortBy) {
            "email" -> UsersTable.email
            "id" -> UsersTable.id
            else -> UsersTable.id
        }

        val sortOrder = if (order.lowercase() == "desc") {
            SortOrder.DESC
        } else {
            SortOrder.ASC
        }

        val total = UsersTable.selectAll().count()

        val users = UsersTable
            .selectAll()
            .orderBy(column to sortOrder)
            .limit(size, offset)
            .map { row ->
                User(
                    id = row[UsersTable.id],
                    name = row[UsersTable.name],
                    email = row[UsersTable.email],
                    password = row[UsersTable.password]
                )
            }

        Pair(users, total)
    }



    override fun create(name: String, email: String, password : String, role: String): Int {
        return transaction {
            UsersTable.insert {
                it[UsersTable.name] = name
                it[UsersTable.email] = email
                it[UsersTable.password] = password
                it[UsersTable.role] = role
            } get UsersTable.id
        }
    }

    override fun findAll(): List<User> {
        return transaction {
            UsersTable.selectAll().map { row ->
                User(
                    id = row[UsersTable.id],
                    name = row[UsersTable.name],
                    email = row[UsersTable.email],
                    password = row[UsersTable.password],
                    role = row[UsersTable.role]
                )
            }
        }
    }

    override fun findById(userId: Int): User? {
        return transaction {
            UsersTable.select{UsersTable.id eq userId }
                .limit(1)
                .map { row ->
                    User(
                        id = row[UsersTable.id],
                        name = row[UsersTable.name],
                        email = row[UsersTable.email],
                        password = row[UsersTable.password]
                    )
                }
                .firstOrNull()
        }
    }

    override fun findByEmail(email: String): User? {
        return transaction {
            UsersTable.select{UsersTable.email eq email}
                .limit(1)
                .map { row ->
                    User(
                        id = row[UsersTable.id],
                        name = row[UsersTable.name],
                        email = row[UsersTable.email],
                        password = row[UsersTable.password],
                        role = row[UsersTable.role]
                    )
                }
                .firstOrNull()
        }
    }

    override fun update(id: Int, name: String?, email: String?): Boolean {
        return transaction {
            val rowsUpdated = UsersTable.update({ UsersTable.id eq id }) {
                name?.let { newName -> it[UsersTable.name] = newName }
                email?.let { newEmail -> it[UsersTable.email] = newEmail }
            }
            rowsUpdated > 0
        }
    }

    override fun delete(userId: Int): Boolean {
        return transaction {
            val rowsDeleted = UsersTable.deleteWhere { UsersTable.id eq userId }
            rowsDeleted > 0
        }
    }
}