package me.jesse.tictactoe

/**
 * Represents a game of Tic Tac Toe.
 *
 * Rules: The game is played on a grid that's 3 squares by 3 squares.
 * You are X , your friend (or the computer in this case) is O .
 * Players take turns putting their marks in empty squares.
 * The first player to get 3 of her marks in a row (up, down, across, or diagonally) is the winner.
 */
data class Game(
    val board: Board = Board(),
    val currentPlayer: Player = Player.X
) {
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
    val winner: Player?
        get() = calculateWinner()
    private val isCompleted: Boolean
        get() = (winner != null) || board.squares.all { it.value != SquareValue.EMPTY }

    private fun checkValidMove(squareIndex: Int): Boolean {
        val validSquare = squareIndex in 0..8
        val squareEmpty = board.squares[squareIndex].value == SquareValue.EMPTY

        return !isCompleted && validSquare && squareEmpty
    }

    fun play(squareIndex: Int): Game {
        if (!checkValidMove(squareIndex)) {
            return this
        }

        val newBoard = board.copy(
            squares = board.squares.mapIndexed { index, square ->
                if (index == squareIndex) {
                    Square(currentPlayer.toSquareValue())
                } else {
                    square
                }
            }
        )

        return copy(
            board = newBoard,
            currentPlayer = currentPlayer.next(),
        )
    }

    private fun calculateWinner(): Player? {
        return winningLines.fold<List<Int>, Player?>(null) { player, line ->
            val squareValues = line.map { board.squares[it].value }

            when {
                // Short Circuit if we already have a winner
                player != null -> player
                squareValues.all { it == SquareValue.X } -> Player.X
                squareValues.all { it == SquareValue.O } -> Player.O
                else -> null
            }
        }
    }
}
