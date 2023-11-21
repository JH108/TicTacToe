package me.jesse.tictactoe

/**
 * Represents a board of Tic Tac Toe.
 *
 * This is a 3x3 grid of squares. Where each square can be empty, or contain an X or an O.
 */
data class Board(
    val squares: List<Square> = List(9) { Square() },
)
