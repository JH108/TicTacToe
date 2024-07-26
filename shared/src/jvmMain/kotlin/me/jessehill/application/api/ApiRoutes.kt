package me.jessehill.application.api

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import me.jessehill.application.resources.Leaderboard
import me.jessehill.application.resources.LoadGame
import me.jessehill.database.ticTacToeSdk
import me.jessehill.models.Game
import me.jessehill.models.GameStatus
import me.jessehill.models.StartGameRequestBody
import me.jessehill.models.User

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
            post("/register") {
                val user = try {
                    call.receive<User>()
                } catch (e: Exception) {
                    call.respond(
                        status = HttpStatusCode.NotAcceptable,
                        message = "The request body is missing or malformed."
                    )
                    return@post
                }

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
                    id = user.id, // Keep the id that the client assigned to the user
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
            get("/users") {
                val users = withContext(Dispatchers.IO) {
                    application.ticTacToeSdk.database.getAllUsers().firstOrNull()
                }

                if (users == null) {
                    call.respond(
                        status = HttpStatusCode.NotFound,
                        message = "No users were found."
                    )
                    return@get
                }

                call.respond(users)
            }
            get("/user") {
                val username = call.request.queryParameters["username"]
                val userId = call.request.queryParameters["userId"]
                println("username: $username")
                println("userId: $userId")

                if (username == null && userId == null) {
                    call.respond(
                        status = HttpStatusCode.NotAcceptable,
                        message = "The username and userId parameters are missing."
                    )
                    return@get
                }

                val user = if (username != null) {
                    withContext(Dispatchers.IO) {
                        application.ticTacToeSdk.database.getUserByUsername(username).firstOrNull()
                    }
                } else if (userId != null) {
                    withContext(Dispatchers.IO) {
                        application.ticTacToeSdk.database.getUserById(userId).firstOrNull()
                    }
                } else null

                if (user == null) {
                    call.respond(
                        status = HttpStatusCode.NotFound,
                        message = "The user with the username $username or userId $userId was not found."
                    )
                    return@get
                }

                call.respond(user)
            }
            get("/users/{userId}/games") {
                val userId = call.parameters["userId"]

                if (userId == null) {
                    call.respond(status = HttpStatusCode.NotAcceptable, message = "The userId parameter is missing.")
                    return@get
                }

                val games = try {
                    withContext(Dispatchers.IO) {
                        application.ticTacToeSdk.database.getGamesByPlayerId(userId).firstOrNull()
                    }
                } catch (e: Exception) {
                    println(e)
                    null
                }

                if (games == null) {
                    call.respond(
                        status = HttpStatusCode.NotFound,
                        message = "No games were found for the user with the id $userId."
                    )
                    return@get
                }

                call.respond(games)
            }
            post("/games/start") {
                val startGame = try {
                    call.receive<StartGameRequestBody>()
                } catch (e: Exception) {
                    call.respond(
                        status = HttpStatusCode.NotAcceptable,
                        message = "The request body is missing or malformed."
                    )
                    return@post
                }
                val playerOne = startGame.playerOne
                val playerTwo = startGame.playerTwo

                val existingGame = withContext(Dispatchers.IO) {
                    application.ticTacToeSdk.database.getGameByPlayerIdsAndGameStatus(
                        playerOne.id.toString(),
                        playerTwo.id.toString(),
                        GameStatus.IN_PROGRESS
                    ).firstOrNull()
                }

                if (!existingGame.isNullOrEmpty()) {
                    call.respond(existingGame)
                    return@post
                }

                val firstPlayer = if (listOf(true, false).random()) playerOne else playerTwo

                val newGame = Game(
                    playerX = firstPlayer,
                    playerO = if (firstPlayer == playerOne) playerTwo else playerOne,
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
            post("/games/save") {
                val game = try {
                    call.receive<Game>()
                } catch (e: Exception) {
                    call.respond(
                        status = HttpStatusCode.NotAcceptable,
                        message = "The request body is missing or malformed."
                    )
                    return@post
                }

                try {
                    withContext(Dispatchers.IO) {
                        application.ticTacToeSdk.database.updateGameStatus(game)
                    }
                } catch (e: Exception) {
                    call.respond(status = HttpStatusCode.InternalServerError, message = e)
                }

                call.respond(game)
            }
            get<LoadGame> {
                val gameId = it.gameId

                val game = withContext(Dispatchers.IO) {
                    application.ticTacToeSdk.database.getGameById(gameId).firstOrNull()
                }

                if (game == null) {
                    call.respond(
                        status = HttpStatusCode.NotFound,
                        message = "The game with the id $gameId was not found."
                    )
                    return@get
                }

                call.respond(game)
            }
            get<Leaderboard> {
                val topFivePlayers = withContext(Dispatchers.IO) {
                    application.ticTacToeSdk.database.getTopFivePlayers()
                }

                call.respond(topFivePlayers)
            }
            get("/load-fake-data") {
                withContext(Dispatchers.IO) {
                    application.ticTacToeSdk.database.loadFakeData()
                }
                call.respond("ok")
            }
            get("/reset-database") {
                withContext(Dispatchers.IO) {
                    application.ticTacToeSdk.database.clearAll()
                }

                call.respond("ok")
            }
        }
    }
}
