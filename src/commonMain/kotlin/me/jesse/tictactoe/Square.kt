package me.jesse.tictactoe

import kotlinx.serialization.Serializable

/**
 * Represents a square on a Tic Tac Toe board.
 *
 * This can be empty, or contain an X or an O.
 */
@Serializable
data class Square(
    val value: SquareValue = SquareValue.EMPTY,
)

enum class SquareValue {
    X,
    O,
    EMPTY,
}