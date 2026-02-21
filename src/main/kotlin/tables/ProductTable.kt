package com.ktor.tables

import org.jetbrains.exposed.sql.Table


object ProductTable : Table("products") {

    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val price = decimal("price", precision = 10, scale = 2)
    val stock = integer("stock")


    override val primaryKey = PrimaryKey(id)
}