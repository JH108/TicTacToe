package me.jesse.tictactoe

import kotlinx.serialization.Serializable

/**
 * Represents a square on a Tic Tac Toe board.
 *
 * This can be empty, or contain an X or an O.
 */
@Serializable
data class Square(
    val value: MoveSymbol = MoveSymbol.EMPTY,
)

enum class MoveSymbol {
    X,
    O,
    EMPTY,
}

fun MoveSymbol.valueOf(value: String): MoveSymbol {
    return when (value) {
        "X" -> MoveSymbol.X
        "O" -> MoveSymbol.O
        "EMPTY" -> MoveSymbol.EMPTY
        else -> throw IllegalArgumentException("$value is not a valid MoveSymbol")
    }
}