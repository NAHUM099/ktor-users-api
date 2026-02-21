package com.ktor.tables


import org.jetbrains.exposed.sql.Table

object CategoriesTable : Table("categories") {

    val id = integer("id").autoIncrement()
    val name = varchar("name", 100).uniqueIndex()
    val description = varchar("description", 255).nullable()

    override val primaryKey = PrimaryKey (id)
}