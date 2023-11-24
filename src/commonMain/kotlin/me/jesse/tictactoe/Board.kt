package me.jesse.tictactoe

import kotlinx.serialization.Serializable

/**
 * Represents a board of Tic Tac Toe.
 *
 * This is a 3x3 grid of squares. Where each square can be empty, or contain an X or an O.
 */
@Serializable
data class Board(
    val squares: List<Square> = List(9) { Square() },
)
