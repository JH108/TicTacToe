package me.jessehill.tictactoe

import kotlinx.serialization.Serializable
import me.jessehill.models.Game

/**
 * Represents a board of Tic Tac Toe.
 *
 * This is a 3x3 grid of squares. Where each square can be empty, or contain an X or an O.
 */
@Serializable
data class Board(
    val squares: List<Square> = List(9) { Square() },
)

fun Board.setSquares(game: Game): Board {
    return copy(squares = squares.mapIndexed { index, square ->
        if (game.moves.containsKey(index)) {
            square.copy(value = game.moves[index]?.moveSymbol ?: MoveSymbol.EMPTY)
        } else square
    })
}
