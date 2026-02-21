package com.ktor.repository

import com.ktor.dto.product.CreateProductRequest
import com.ktor.dto.product.UpdateProductRequest
import com.ktor.models.Product
import com.ktor.repository.interfaces.IProductRepository
import com.ktor.tables.ProductTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.math.BigDecimal
import kotlin.let


class ProductRepository : IProductRepository {
    override fun create(name: String, price: BigDecimal, stock: Int): Int {
        return transaction {
            ProductTable.insert {
                it[ProductTable.name] = name
                it[ProductTable.price] = price
                it[ProductTable.stock] = stock
            } get ProductTable.id
        }
    }

    override fun findAll(): List<Product> {
        return transaction {
            ProductTable.selectAll().map{ row ->
                Product(
                    id = row[ProductTable.id],
                    name = row[ProductTable.name],
                    price = row[ProductTable.price],
                    stock = row[ProductTable.stock]
                )

            }
        }
    }

    override fun findById(id: Int): Product? {
        return transaction {
            ProductTable.select { ProductTable.id eq id }
                .limit(1)
                .map{ row ->
                    Product(
                        id = row[ProductTable.id],
                        name = row[ProductTable.name],
                        price = row[ProductTable.price],
                        stock = row[ProductTable.stock]
                    )
                }
                .firstOrNull()
        }

    }

    override fun findByName(name: String): Product? {
        return transaction {
            ProductTable.select {ProductTable.name eq name }
                .limit(1)
                .map{ row ->
                    Product(
                        id = row[ProductTable.id],
                        name = row[ProductTable.name],
                        price = row[ProductTable.price],
                        stock = row[ProductTable.stock]
                    )
            }
                .firstOrNull()
        }
    }

    override fun update(id: Int, name: String?, price: BigDecimal?, stock: Int?): Boolean {
        return transaction {
            val rowsUpdated = ProductTable.update({ ProductTable.id eq id }) {
                name?.let { newName -> it[ProductTable.name] = newName }
                price?.let { newPrice -> it[ProductTable.price] = newPrice }
                stock?.let { newStock -> it[ProductTable.stock] = newStock }
            }
            rowsUpdated > 0
        }
    }
    override fun delete(id: Int): Boolean {
        return transaction {
            val rowDelete = ProductTable.deleteWhere{ProductTable.id eq id}
            rowDelete > 0
        }

    }

}





