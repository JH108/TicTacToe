package me.jessehill.tictactoe

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import me.jessehill.models.Game
import me.jessehill.models.GameStatus
import me.jessehill.models.User
import me.jessehill.models.UserStats
import me.jessehill.network.TicTacToeApi

interface TicTacToeRepository {
    suspend fun loadUserById(userId: String): Flow<User?>

    suspend fun loadUserByUsername(username: String): Flow<User?>

    suspend fun registerUser(user: User): Flow<User>

    suspend fun saveGame(game: Game): Flow<Game>

    suspend fun startGame(user: User, opponent: User): Flow<Game>

    suspend fun loadGame(gameId: String): Flow<Game?>

    suspend fun loadGamesForUserId(userId: String): Flow<List<Game>>

    suspend fun loadUsers(): Flow<List<User>>

    suspend fun loadTopFivePlayers(): Flow<List<Pair<User, UserStats>>>
}

// TODO: I think all the responses should be a flow of a Result type instead of just the data
// TODO: There should ideally be a way for this repository to check the internet connection, I think 🤔
// TODO: Put all the database calls on the IO dispatcher
// TODO: Extract the business logic into UseCases
// TODO: Ensure that whenever data is fetched from the API that we also save it to the database
class SimpleTicTacToeRepository(
    private val api: TicTacToeApi,
    private val sdk: TicTacToeSdk
) : TicTacToeRepository {
    override suspend fun loadUserById(userId: String): Flow<User?> {
        try {
            val user = api.getUserProfileById(userId)

            sdk.database.insertUser(user).runCatching {
                println("Inserted user $user")
            }.onFailure {
                println("Error inserting user $user: $it")
            }

            return flow {
                emit(user)
            }
        } catch (e: Exception) {
            println("Error fetching user by id: $e")
            return flow {
                emitAll(sdk.database.getUserById(userId))
            }
        }
    }

    override suspend fun loadUserByUsername(username: String): Flow<User?> {
        try {
            val user = api.getUserProfileByUsername(username)

            sdk.database.insertUser(user).runCatching {
                println("Inserted user $user")
            }.onFailure {
                println("Error inserting user $user: $it")
            }

            return flow {
                emit(user)
            }
        } catch (e: Exception) {
            println("Error fetching user by username: $e")
            return flow {
                emitAll(sdk.database.getUserByUsername(username))
            }
        }
    }

    override suspend fun registerUser(user: User): Flow<User> {
        val existingUser = sdk.database.getUserByUsername(user.username).firstOrNull()

        if (existingUser != null) {
            return flow {
                emit(existingUser)
            }
        }

        sdk.database.insertUser(user)

        try {
            api.registerUser(user)

            return flow {
                emit(user)
            }
        } catch (e: Exception) {
            println("Error registering user: $e")

            return flow {
                emit(user)
            }
        }
    }

    override suspend fun saveGame(game: Game): Flow<Game> {
        sdk.database.updateGameStatus(game)

        try {
            val savedGame = api.saveGame(game)

            return flow {
                emit(savedGame)
            }
        } catch (e: Exception) {
            println("Error saving game: $e")
            return flow {
                emit(game)
            }
        }
    }

    override suspend fun startGame(user: User, opponent: User): Flow<Game> {
        val existingOpponent = sdk.database.getUserById(opponent.id.toString()).firstOrNull()

        // If we don't have the opponent in the database then we might get an error
        if (existingOpponent == null) {
            sdk.database.insertUser(opponent).runCatching {
                println("Inserted user $opponent")
            }.onFailure {
                println("Error inserting user $opponent: $it")
            }
        }

        val existingGame = sdk.database.getGameByPlayerIdsAndGameStatus(
            user.id.toString(),
            opponent.id.toString(),
            GameStatus.IN_PROGRESS
        ).firstOrNull()

        if (!existingGame.isNullOrEmpty()) {
            return flow {
                emit(existingGame.first())
            }
        }

        val firstPlayer = if (listOf(true, false).random()) user else opponent;

        val newGame = Game(
            playerX = firstPlayer,
            playerO = if (firstPlayer == user) opponent else user,
            playerToMove = firstPlayer
        )

        sdk.database.insertGame(newGame)

        return try {
            val savedGame = api.saveGame(newGame)

            flow { emit(savedGame) }
        } catch (e: Exception) {
            flow { emit(newGame) }
        }
    }

    override suspend fun loadGame(gameId: String): Flow<Game?> {
        return try {
            val game = api.getGame(gameId)

            flow { emit(game) }
        } catch (e: Exception) {
            flow { emitAll(sdk.database.getGameById(gameId)) }
        }
    }

    override suspend fun loadGamesForUserId(userId: String): Flow<List<Game>> {
        try {
            val games = api.getGamesForUserId(userId)

            return flow {
                emit(games)
            }
        } catch (e: Exception) {
            println("Error fetching games for user: $e")
            return flow {
                emitAll(sdk.database.getGamesByPlayerId(userId))
            }
        }
    }

    override suspend fun loadUsers(): Flow<List<User>> {
        try {
            val users = api.getAllUsers()

            users.forEach {
                sdk.database.insertUser(it).runCatching {
                    println("Inserted user $it")
                }.onFailure {
                    println("Error inserting user $it: $it")
                }
            }

            return flow {
                emit(users)
            }
        } catch (e: Exception) {
            println("Error fetching users: $e")
            return flow {
                emitAll(sdk.database.getAllUsers())
            }
        }
    }

    override suspend fun loadTopFivePlayers(): Flow<List<Pair<User, UserStats>>> {
        return flow {
            emit(api.getTopFivePlayers())
        }.catch { e ->
            println("Error fetching top five players: $e")
            val topFivePlayers = sdk.database.getTopFivePlayers()

            println("Top five players from database: $topFivePlayers")
        }
    }
}