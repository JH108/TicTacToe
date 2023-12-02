package me.jesse.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.util.*

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        // TODO: Replace with a database that isn't in memory.
        val driver: SqlDriver = JdbcSqliteDriver(
            "jdbc:sqlite:tictactoe.db",
            properties = Properties().apply { put("foreign_keys", "true") })
        TicTacToeDatabase.Schema.create(driver)
        return driver
    }
}