package me.jesse.tictactoe

import me.jesse.database.DatabaseDriverFactory
import me.jesse.database.TicTacToeDatabaseImpl

class TicTacToeSdk(databaseDriverFactory: DatabaseDriverFactory) {
    val database = TicTacToeDatabaseImpl(databaseDriverFactory)
    // We could make this database private and then only expose specific methods as part of the sdk. This would allow us to
    // have a greater separation of concerns between the database and the sdk. It would be more advantageous to do this when
    // we end up supporting multiple platforms.
}