package org.lab4

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.logging.Level
import java.util.logging.Logger

object ConnectionUtil {
    private const val JDBC_URL = "jdbc:postgresql://localhost:5432/testDB"
    private const val JDBC_USER = "postgres"
    private const val JDBC_PASSWORD = "0000"

    val connection: Connection?
        get() {
            var connection: Connection? = null
            try {
                connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)
            } catch (ex: SQLException) {
                Logger.getLogger(ConnectionUtil::class.java.name).log(
                    Level.SEVERE, null,
                    ex
                )
            }
            return connection
        }
}
