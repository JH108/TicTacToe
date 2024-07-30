package me.jessehill.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.util.*

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver: SqlDriver = if (System.getProperty("USE_MEMORY_DB") != null) {
            createInMemorySqlDriver()
        } else {
            createRegularSqlDriver()
        }

        TicTacToeDatabase.Schema.create(driver)
        return driver
    }

    private fun createInMemorySqlDriver(): SqlDriver {
        return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    }

    private fun createRegularSqlDriver(): SqlDriver {
        return JdbcSqliteDriver(
            "jdbc:sqlite:tictactoe.db",
            properties = Properties().apply { put("foreign_keys", "true") }
        )
    }
}