package com.ktor.repository

import javax.sql.DataSource

class PingRepository(private val dataSource: DataSource) {

    fun obtenerMensaje(): String {
        dataSource.connection.use { conn ->
            conn.prepareStatement(
                "SELECT mensaje FROM ping LIMIT 1"
            ).use { stmt ->
                val rs = stmt.executeQuery()
                return if (rs.next()) {
                    rs.getString("mensaje")
                } else {
                    "sin datos"
                }
            }
        }
    }
}