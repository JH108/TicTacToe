package me.jesse.database

import io.ktor.server.application.*
import me.jesse.tictactoe.TicTacToeSdk

/**
 * This must be called before Application.ticTacToeSdk is accessed, or it will throw an IllegalStateException.
 */
fun Application.configureDatabases() {
    // TODO: Pass the configuration read from a hocon file to the database driver factory.
    DatabaseReference.init()
}

val Application.ticTacToeSdk: TicTacToeSdk
    get() = DatabaseReference.instance

/**
 * Singleton object for referencing the database.
 */
object DatabaseReference {
    private var sdk: TicTacToeSdk? = null

    fun init() {
        sdk = TicTacToeSdk(DatabaseDriverFactory())
    }

    val instance: TicTacToeSdk
        get() = sdk ?: throw IllegalStateException("SDK not initialized")
}
