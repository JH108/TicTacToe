package me.jesse.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import me.jesse.models.*
import me.jesse.tictactoe.MoveSymbol

class TicTacToeDatabaseImpl(databaseDriverFactory: DatabaseDriverFactory) {
    private val driver = databaseDriverFactory.createDriver()
    private val database = TicTacToeDatabase(driver)
    private val databaseQueries = database.ticTacToeDatabaseQueries

    fun insertUser(
        user: User
    ) {
        databaseQueries.transaction {
            databaseQueries.insertUser(
                id = user.id.toString(),
                username = user.username,
                first_name = user.firstName,
                last_name = user.lastName
            )
        }
    }

    fun insertGame(
        game: Game
    ) {
        databaseQueries.transaction {
            databaseQueries.insertGame(
                id = game.id.toString(),
                player_x_id = game.playerX.id.toString(),
                player_o_id = game.playerO.id.toString(),
                player_to_move_id = game.playerToMove.id.toString(),
                status = game.status.toString(),
                start_time = game.startTime.toString()
            )
            game.moves.forEach { (_, move) ->
                databaseQueries.upsertMove(
                    move_symbol = move.moveSymbol.toString(),
                    game_id = game.id.toString(),
                    square_index = move.squareIndex.toLong(),
                    player_id = move.playerId.toString()
                )
            }
        }
    }

    fun insertMove(
        move: Move,
        gameId: Uuid
    ) {
        databaseQueries.transaction {
            databaseQueries.insertMove(
                game_id = gameId.toString(),
                square_index = move.squareIndex.toLong(),
                player_id = move.playerId.toString(),
                move_symbol = move.moveSymbol.toString()
            )
        }
    }

    fun updateGameStatus(
        game: Game,
    ) {
        databaseQueries.transaction {
            databaseQueries.updateGameStatus(
                status = game.status.toString(),
                end_time = game.endTime?.toString(),
                player_to_move_id = game.playerToMove.id.toString(),
                id = game.id.toString()
            )
            game.moves.forEach { (_, move) ->
                databaseQueries.upsertMove(
                    move_symbol = move.moveSymbol.toString(),
                    game_id = game.id.toString(),
                    square_index = move.squareIndex.toLong(),
                    player_id = move.playerId.toString()
                )
            }
        }
    }

    fun getUser(
        userId: String
    ): Flow<User?> {
        return databaseQueries.selectUserById(userId, ::mapDatabaseUserToModelUser)
            .asFlow()
            .mapToOne(Dispatchers.Default)
    }

    /**
     * This is used when a user logs in. We want to make sure that the user exists in the database before we log them in.
     * If the user doesn't exist, we will prompt them to create an account.
     * @param forUsername The username to search for.
     */
    fun getUserByUsername(
        forUsername: String
    ): Flow<User?> {
        return databaseQueries.selectUserByUsername(forUsername, ::mapDatabaseUserToModelUser)
            .asFlow()
            .mapToOne(Dispatchers.Default)
    }

    fun getTopFivePlayers(): List<Pair<User, UserStats>> {
        return databaseQueries.selectTopFivePlayers { id, username, first_name, last_name, total_games, total_x_wins, total_o_wins, total_draws ->
            User(
                id = uuidFrom(id),
                username = username,
                firstName = first_name,
                lastName = last_name
            ) to UserStats(
                id = uuidFrom(id),
                totalGames = total_games,
                totalXWins = total_x_wins,
                totalOWins = total_o_wins,
                totalDraws = total_draws
            )
        }.executeAsList()
    }

    fun getGamesByPlayerId(
        playerId: String
    ): Flow<List<Game>> {
        return databaseQueries.selectGamesByPlayerId(
            player_id = playerId
        ) { id, player_x_id, player_o_id, player_to_move_id, start_time, end_time, status ->
            buildGame(
                Games(
                    id = id,
                    player_x_id = player_x_id,
                    player_o_id = player_o_id,
                    player_to_move_id = player_to_move_id,
                    status = status,
                    start_time = start_time,
                    end_time = end_time
                )
            )
        }
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    // TODO: Reduce the amount of database queries needed to build a game now that I've figured out the syntax for
    //  doing joins in SQLDelight.
    fun getGameByPlayerIdsAndGameStatus(
        playerOneId: String,
        playerTwoId: String,
        gameStatus: GameStatus
    ): Flow<List<Game>> {
        return databaseQueries.selectGameByPlayerIdsAndGameStatus(
            player_one_id = playerOneId,
            player_two_id = playerTwoId,
            game_status = gameStatus.toString()
        ) { id, player_x_id, player_o_id, player_to_move_id, start_time, end_time, status ->
            buildGame(
                Games(
                    id = id,
                    player_x_id = player_x_id,
                    player_o_id = player_o_id,
                    player_to_move_id = player_to_move_id,
                    status = status,
                    start_time = start_time,
                    end_time = end_time
                )
            )
        }
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    /**
     * Get all games for a given player and game status.
     * @param playerId The player to get games for.
     * @param gameStatus The status of the games to get.
     */
    fun getGamesByPlayerIdAndGameStatus(
        playerId: String,
        gameStatus: GameStatus
    ): Flow<List<Game>> {
        return databaseQueries.selectGamesByPlayerIdAndStatus(
            player_id = playerId,
            status = gameStatus.toString()
        ) { id, player_x_id, player_o_id, player_to_move_id, start_time, end_time, status ->
            buildGame(
                Games(
                    id = id,
                    player_x_id = player_x_id,
                    player_o_id = player_o_id,
                    player_to_move_id = player_to_move_id,
                    status = status,
                    start_time = start_time,
                    end_time = end_time
                )
            )
        }
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    /**
     * For a given user check if they have any open games. If they have an open game, return it and prompt the other user to join.
     * @param userId The user to check for.
     */
    fun getOpenGamesForUser(
        userId: String
    ): Flow<List<Game>> {
        return databaseQueries.selectOpenGamesForUser(
            player_id = userId,
        ).asFlow().mapToList(Dispatchers.Default).map { games ->
            games.map { buildGame(it) }
        }
    }

    /**
     * Map a database game to a model game fetching the users and moves for the game.
     */
    private fun buildGame(
        game: Games
    ): Game {
        val (userX, userO) = getUsersForGame(game)
        val moves = getMovesForGame(game)

        // parse start time and end time
        val startTime = LocalDateTime.parse(game.start_time)
        val endTime = game.end_time?.let { LocalDateTime.parse(it) }

        return Game(
            id = uuidFrom(game.id),
            playerX = userX,
            playerO = userO,
            playerToMove = if (game.player_to_move_id == userX.id.toString()) userX else userO,
            status = GameStatus.valueOf(game.status),
            startTime = startTime,
            endTime = endTime,
            moves = moves
        )
    }

    private fun getUsersForGame(
        game: Games
    ): Pair<User, User> {
        val userX = databaseQueries.selectUserById(game.player_x_id, ::mapDatabaseUserToModelUser).executeAsOne()
        val userO = databaseQueries.selectUserById(game.player_o_id, ::mapDatabaseUserToModelUser).executeAsOne()

        return userX to userO
    }

    private fun getMovesForGame(
        game: Games
    ): Map<Int, Move> {
        return databaseQueries
            .selectMovesForGame(game.id, ::mapDatabaseMoveToModelMove)
            .executeAsList()
            .fold(emptyMap()) { acc, move ->
                acc + (move.squareIndex to move)
            }
    }

    fun loadFakeData() {
        // insert users from SampleData
        SampleData.users.forEach { insertUser(it) }
        SampleData.games.forEach { insertGame(it) }
    }
}

private fun mapDatabaseUserToModelUser(
    id: String,
    username: String,
    first_name: String,
    last_name: String
): User {
    return User(
        id = uuidFrom(id),
        username = username,
        firstName = first_name,
        lastName = last_name
    )
}

private fun mapDatabaseMoveToModelMove(
    id: Long,
    game_id: String,
    square_index: Long,
    player_id: String,
    move_symbol: String
): Move {
    return Move(
        squareIndex = square_index.toInt(),
        playerId = uuidFrom(player_id),
        moveSymbol = MoveSymbol.valueOf(move_symbol)
    )
}
