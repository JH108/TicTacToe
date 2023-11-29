package me.jesse.application.api

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import me.jesse.application.resources.LoadGame
import me.jesse.application.resources.RegisterUser
import me.jesse.application.resources.SaveGame
import me.jesse.application.resources.StartGame
import me.jesse.database.ticTacToeSdk
import me.jesse.models.Game
import me.jesse.models.GameStatus
import me.jesse.models.User

/**
 * Route for registering a new player.
 * Route for fetching a player's profile.
 * Route for starting a new game.
 * Route for saving a game.
 * Route for loading a game.
 * Route for creating a leaderboard.
 */
fun Application.apiRoutes() {
    routing {
        route("/api") {
            post<RegisterUser> { user ->
                val existingUser = withContext(Dispatchers.IO) {
                    application.ticTacToeSdk.database.getUserByUsername(
                        user.username
                    ).firstOrNull()
                }

                if (existingUser != null) {
                    call.respond(existingUser)
                    return@post
                }

                val newUser = User(
                    username = user.username,
                    firstName = user.firstName,
                    lastName = user.lastName
                )

                try {
                    withContext(Dispatchers.IO) { application.ticTacToeSdk.database.insertUser(newUser) }
                } catch (e: Exception) {
                    call.respond(status = HttpStatusCode.InternalServerError, message = e)
                }
                call.respond(newUser)
            }
            get("/profile?username={username}") {
                val username = call.parameters["username"]

                if (username == null) {
                    call.respond(status = HttpStatusCode.NotAcceptable, message = "The username parameter is missing.")
                    return@get
                }

                val user = withContext(Dispatchers.IO) {
                    application.ticTacToeSdk.database.getUserByUsername(username).firstOrNull()
                }

                if (user == null) {
                    call.respond(
                        status = HttpStatusCode.NotFound,
                        message = "The user with the username $username was not found."
                    )
                    return@get
                }

                call.respond(user)
            }
            post<StartGame> { startGame ->
                val playerOne = startGame.playerOne
                val playerTwo = startGame.playerTwo

                val existingGame = withContext(Dispatchers.IO) {
                    application.ticTacToeSdk.database.getGameByPlayerIdsAndGameStatus(
                        playerOne.id.toString(),
                        playerTwo.id.toString(),
                        GameStatus.IN_PROGRESS
                    ).firstOrNull()
                }

                if (existingGame != null) {
                    call.respond(existingGame)
                    return@post
                }

                val firstPlayer = if (listOf(true, false).random()) playerOne else playerTwo
                val newGame = Game(
                    playerX = playerOne,
                    playerO = playerTwo,
                    playerToMove = firstPlayer
                )

                try {
                    withContext(Dispatchers.IO) {
                        application.ticTacToeSdk.database.insertGame(newGame)
                    }
                } catch (e: Exception) {
                    call.respond(status = HttpStatusCode.InternalServerError, message = e)
                }

                call.respond(newGame)
            }
            post<SaveGame> {

            }
            // This one might be redundant since the start game is basically start or load
            post<LoadGame> {

            }
            get("/leaderboard") {
                val topFivePlayers = withContext(Dispatchers.IO) {
                    application.ticTacToeSdk.database.getTopFivePlayers()
                }

                call.respond(topFivePlayers)
            }
            get("/load-fake-data") {
                withContext(Dispatchers.IO) {
                    application.ticTacToeSdk.database.loadFakeData()
                }
                call.respond("Fake data loaded.")
            }
        }
    }
}
