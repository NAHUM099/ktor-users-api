package com.ktor.repository

import com.ktor.models.Category
import com.ktor.repository.interfaces.ICategoryRepository
import com.ktor.tables.CategoriesTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class CategoryRepository : ICategoryRepository{

    override fun create(name: String, description: String?): Int {
       return transaction {
           CategoriesTable.insert{
               it[CategoriesTable.name] = name
               it[CategoriesTable.description] = description
           }  get CategoriesTable.id
       }
    }

    override fun findAll(): List<Category> {
        return transaction {
            CategoriesTable.selectAll().map {row ->
                Category(
                    id = row[CategoriesTable.id],
                    name = row[CategoriesTable.name],
                    description = row[CategoriesTable.description]
                )
            }
        }
    }

    override fun findById(id: Int): Category? {
        return transaction {
            CategoriesTable.select(CategoriesTable.id eq id )
                .limit(1)
                .map { row ->
                    Category (
                        id = row[CategoriesTable.id],
                        name = row[CategoriesTable.name],
                        description = row[CategoriesTable.description]
                    )
            }
                .firstOrNull()
        }

    }

    override fun findByName(name: String): Category? {
        return transaction {
            CategoriesTable.select{CategoriesTable.name eq name}
                .limit(1)
                .map{ row ->
                    Category (
                        id = row[CategoriesTable.id],
                        name = row[CategoriesTable.name],
                        description = row[CategoriesTable.description]
                    )
                }
                .firstOrNull()
        }
    }

    override fun update(id : Int, name: String?, description: String?): Boolean {
        if (name == null && description == null) return false

        return transaction {
            val rowsUpdate = CategoriesTable.update({ CategoriesTable.id eq id}){
                name?.let {newName -> it[CategoriesTable.name] = newName}
                description?.let{newDescription -> it[CategoriesTable.description] = newDescription}
            }
            rowsUpdate > 0
        }
    }

    override fun delete(id: Int): Boolean {
        return transaction {
            val rowsDelete = CategoriesTable.deleteWhere { CategoriesTable.id eq id }

            rowsDelete > 0
        }
    }
}