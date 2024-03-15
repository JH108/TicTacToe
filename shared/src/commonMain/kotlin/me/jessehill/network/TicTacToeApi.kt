package me.jessehill.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.jessehill.models.Game
import me.jessehill.models.StartGameRequestBody
import me.jessehill.models.User
import me.jessehill.models.UserStats

// TODO: Have this write to the database potentially
class TicTacToeApi {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    // TODO: Add error handling
    suspend fun getGame(gameId: String): Game {
        val game: Game = httpClient.get("${ClientConfiguration.apiUrl}/games/load/$gameId").body()

        return game
    }

    suspend fun saveGame(game: Game): Game {
        val savedGame = httpClient.post("${ClientConfiguration.apiUrl}/games/save") {
            setBody(game)
        }.body<Game>()

        return savedGame
    }

    suspend fun getUserProfileByUsername(username: String): User {
        val user = httpClient.get("${ClientConfiguration.apiUrl}/users/$username").body<User>()

        return user
    }

    suspend fun getGamesForUserId(userId: String): List<Game> {
        val games = httpClient.get("${ClientConfiguration.apiUrl}/users/$userId/games").body<List<Game>>()

        return games
    }

    suspend fun getAllUsers(): List<User> {
        val users = httpClient.get("${ClientConfiguration.apiUrl}/users").body<List<User>>()

        return users
    }

    suspend fun startGame(playerOne: User, playerTwo: User): Game {
        val game = httpClient.post("${ClientConfiguration.apiUrl}/games/start") {
            setBody(
               StartGameRequestBody(
                   playerOne = playerOne,
                   playerTwo = playerTwo
               )
            )
        }.body<Game>()

        return game
    }

    suspend fun getTopFivePlayers(): List<Pair<User, UserStats>> {
        val users = httpClient.get("${ClientConfiguration.apiUrl}/leaderboard").body<List<Pair<User, UserStats>>>()

        return users
    }
}