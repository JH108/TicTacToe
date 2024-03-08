package me.jessehill.database

import com.benasher44.uuid.Uuid
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.jessehill.models.Game
import me.jessehill.models.GameStatus
import me.jessehill.models.Move
import me.jessehill.models.User
import me.jessehill.tictactoe.MoveSymbol

// X wins if the moves are 0, 1, 3, 4, 6
// O wins if the moves are 0, 1, 2, 4, 3, 7,

object SampleData {
    // create five users
    val johnSmith = User(
        username = "johnsmith",
        firstName = "John",
        lastName = "Smith"
    )
    val janeDoe = User(
        username = "janedoe",
        firstName = "Jane",
        lastName = "Doe"
    )
    val jesseHill = User(
        username = "jessehill",
        firstName = "Jesse",
        lastName = "Hill"
    )
    val jessicaHill = User(
        username = "jessicahill",
        firstName = "Jessica",
        lastName = "Hill"
    )
    val jacobHill = User(
        username = "jacobhill",
        firstName = "Jacob",
        lastName = "Hill"
    )

    val users = listOf(
        johnSmith,
        janeDoe,
        jesseHill,
        jessicaHill,
        jacobHill
    )

    private val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    // create five games where X won
    val game1 = Game(
        playerX = johnSmith,
        playerO = janeDoe,
        playerToMove = johnSmith,
        status = GameStatus.X_WON,
        startTime = now,
        endTime = now,
        moves = mapOf(
            0 to moveX(johnSmith.id, 0),
            1 to moveO(janeDoe.id, 1),
            3 to moveX(johnSmith.id, 3),
            4 to moveO(janeDoe.id, 4),
            6 to moveX(johnSmith.id, 6),
        )
    )
    val game2 = Game(
        playerX = jesseHill,
        playerO = janeDoe,
        playerToMove = jesseHill,
        status = GameStatus.X_WON,
        startTime = now,
        endTime = now,
        moves = mapOf(
            0 to moveX(jesseHill.id, 0),
            1 to moveO(janeDoe.id, 1),
            3 to moveX(jesseHill.id, 3),
            4 to moveO(janeDoe.id, 4),
            6 to moveX(jesseHill.id, 6),
        )
    )
    val game3 = Game(
        playerX = jessicaHill,
        playerO = janeDoe,
        playerToMove = jessicaHill,
        status = GameStatus.X_WON,
        startTime = now,
        endTime = now,
        moves = mapOf(
            0 to moveX(jessicaHill.id, 0),
            1 to moveO(janeDoe.id, 1),
            3 to moveX(jessicaHill.id, 3),
            4 to moveO(janeDoe.id, 4),
            6 to moveX(jessicaHill.id, 6),
        )
    )
    val game4 = Game(
        playerX = jacobHill,
        playerO = janeDoe,
        playerToMove = jacobHill,
        status = GameStatus.X_WON,
        startTime = now,
        endTime = now,
        moves = mapOf(
            0 to moveX(jacobHill.id, 0),
            1 to moveO(janeDoe.id, 1),
            3 to moveX(jacobHill.id, 3),
            4 to moveO(janeDoe.id, 4),
            6 to moveX(jacobHill.id, 6),
        )
    )
    val game5 = Game(
        playerX = jacobHill,
        playerO = jesseHill,
        playerToMove = jacobHill,
        status = GameStatus.X_WON,
        startTime = now,
        endTime = now,
        moves = mapOf(
            0 to moveX(jacobHill.id, 0),
            1 to moveO(jesseHill.id, 1),
            3 to moveX(jacobHill.id, 3),
            4 to moveO(jesseHill.id, 4),
            6 to moveX(jacobHill.id, 6),
        )
    )

    // Make five games where O won
    val game6 = Game(
        playerX = johnSmith,
        playerO = janeDoe,
        playerToMove = johnSmith,
        status = GameStatus.O_WON,
        startTime = now,
        endTime = now,
        moves = mapOf(
            0 to moveX(johnSmith.id, 0),
            1 to moveO(janeDoe.id, 1),
            2 to moveX(johnSmith.id, 2),
            4 to moveO(janeDoe.id, 4),
            3 to moveX(johnSmith.id, 3),
            7 to moveO(janeDoe.id, 7),
        )
    )
    val game7 = Game(
        playerX = jesseHill,
        playerO = janeDoe,
        playerToMove = jesseHill,
        status = GameStatus.O_WON,
        startTime = now,
        endTime = now,
        moves = mapOf(
            0 to moveX(jesseHill.id, 0),
            1 to moveO(janeDoe.id, 1),
            2 to moveX(jesseHill.id, 2),
            4 to moveO(janeDoe.id, 4),
            3 to moveX(jesseHill.id, 3),
            7 to moveO(janeDoe.id, 7),
        )
    )
    val game8 = Game(
        playerX = jessicaHill,
        playerO = janeDoe,
        playerToMove = jessicaHill,
        status = GameStatus.O_WON,
        startTime = now,
        endTime = now,
        moves = mapOf(
            0 to moveX(jessicaHill.id, 0),
            1 to moveO(janeDoe.id, 1),
            2 to moveX(jessicaHill.id, 2),
            4 to moveO(janeDoe.id, 4),
            3 to moveX(jessicaHill.id, 3),
            7 to moveO(janeDoe.id, 7),
        )
    )
    val game9 = Game(
        playerX = jacobHill,
        playerO = janeDoe,
        playerToMove = jacobHill,
        status = GameStatus.O_WON,
        startTime = now,
        endTime = now,
        moves = mapOf(
            0 to moveX(jacobHill.id, 0),
            1 to moveO(janeDoe.id, 1),
            2 to moveX(jacobHill.id, 2),
            4 to moveO(janeDoe.id, 4),
            3 to moveX(jacobHill.id, 3),
            7 to moveO(janeDoe.id, 7),
        )
    )
    val game10 = Game(
        playerX = jacobHill,
        playerO = jesseHill,
        playerToMove = jacobHill,
        status = GameStatus.O_WON,
        startTime = now,
        endTime = now,
        moves = mapOf(
            0 to moveX(jacobHill.id, 0),
            1 to moveO(jesseHill.id, 1),
            2 to moveX(jacobHill.id, 2),
            4 to moveO(jesseHill.id, 4),
            3 to moveX(jacobHill.id, 3),
            7 to moveO(jesseHill.id, 7),
        )
    )

    val games = listOf(
        game1,
        game2,
        game3,
        game4,
        game5,
        game6,
        game7,
        game8,
        game9,
        game10
    )

    private fun moveO(userId: Uuid, squareIndex: Int): Move {
        return Move(
            squareIndex = squareIndex,
            playerId = userId,
            moveSymbol = MoveSymbol.O
        )
    }

    private fun moveX(userId: Uuid, squareIndex: Int): Move {
        return Move(
            squareIndex = squareIndex,
            playerId = userId,
            moveSymbol = MoveSymbol.X
        )
    }
}