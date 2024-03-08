package me.jessehill.models

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import me.jessehill.tictactoe.MoveSymbol

private val winningLines = listOf(
    // Horizontal
    listOf(0, 1, 2),
    listOf(3, 4, 5),
    listOf(6, 7, 8),
    // Vertical
    listOf(0, 3, 6),
    listOf(1, 4, 7),
    listOf(2, 5, 8),
    // Diagonal
    listOf(0, 4, 8),
    listOf(2, 4, 6),
)

/**
 * Represents a game of Tic Tac Toe.
 *
 * Rules: The game is played on a grid that's 3 squares by 3 squares.
 * You are X , your friend (or the computer in this case) is O .
 * Players take turns putting their marks in empty squares.
 * The first player to get 3 of her marks in a row (up, down, across, or diagonally) is the winner.
 */
@Serializable
data class Game(
    @Contextual
    val id: Uuid = uuid4(),
    val playerX: User,
    val playerO: User,
    val playerToMove: User,
    val status: GameStatus = GameStatus.IN_PROGRESS,
    val startTime: LocalDateTime = Clock.System.now().toLocalDateTime(
        TimeZone.currentSystemDefault()
    ),
    val endTime: LocalDateTime? = null,
    val moves: Map<Int, Move> = emptyMap(),
) {
    private fun checkValidMove(squareIndex: Int): Boolean {
        val validSquare = squareIndex in 0..8
        val squareEmpty = moves[squareIndex] == null

        return status == GameStatus.IN_PROGRESS && validSquare && squareEmpty
    }

    fun play(squareIndex: Int): Game {
        if (!checkValidMove(squareIndex)) {
            return this
        }

        val move = Move(
            squareIndex = squareIndex,
            playerId = playerToMove.id,
            moveSymbol = if (playerToMove == playerX) MoveSymbol.X else MoveSymbol.O
        )
        // All this is needed because this is the new state but the class methods would still look at the old state
        val newMoves = moves + (squareIndex to move)
        val winner = calculateWinner(newMoves)
        val newStatus = getNewStatus(winner, newMoves)

        return copy(
            moves = newMoves,
            playerToMove = if (playerToMove == playerX) playerO else playerX,
            status = newStatus,
            endTime = if (newStatus != GameStatus.IN_PROGRESS) {
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            } else null
        )
    }

    private fun getNewStatus(winner: User?, newMoves: Map<Int, Move>): GameStatus {
        return when {
            winner == playerO -> GameStatus.O_WON
            winner == playerX -> GameStatus.X_WON
            winner == null && newMoves.size == 9 -> GameStatus.DRAW
            else -> GameStatus.IN_PROGRESS
        }
    }

    private fun calculateWinner(newMoves: Map<Int, Move>): User? {
        return winningLines.fold<List<Int>, User?>(null) { player, line ->
            val recordedMoves = line.map { newMoves[it] }

            when {
                // Short Circuit if we already have a winner
                player != null -> player
                recordedMoves.all { it?.playerId == playerX.id } -> playerX
                recordedMoves.all { it?.playerId == playerO.id } -> playerO
                else -> null
            }
        }
    }
}
