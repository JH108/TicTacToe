package me.jesse.tictactoe

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class GameTest {

    @Test
    fun play() {
        // create a game
        val game = Game()
        // check that the current player is x
        assertTrue(game.currentPlayer == Player.X)

        // make one play
        val newGame = game.play(0)

        // check that the next user is O and that there is one x on the board
        assertTrue(newGame.currentPlayer == Player.O)
        assertTrue(newGame.board.squares.filter { it.value == SquareValue.X }.size == 1)
    }

    @Test
    fun `play on a square that is already taken`() {
        // create a game
        val game = Game()
        // check that the current player is x
        assertTrue(game.currentPlayer == Player.X)

        // make one play
        val newGame = game.play(0)
        // make another play on the same square
        val newGame2 = newGame.play(0)

        // check that the next user is X and that there is one x on the board
        assertTrue(newGame2.currentPlayer == Player.O)
        assertTrue(newGame2.board.squares.filter { it.value == SquareValue.X }.size == 1)
        // check that there are no o's on the board
        assertTrue(newGame2.board.squares.none { it.value == SquareValue.O })
    }

    @Test
    fun `finish the game`() {
        // create a game
        val game = Game()
        // check that the current player is x
        assertTrue(game.currentPlayer == Player.X)

        // make one play
        val newGame = game.play(0)
        // make another play on the same square
        val newGame2 = newGame.play(1)
        val newGame3 = newGame2.play(3)
        val newGame4 = newGame3.play(4)
        val newGame5 = newGame4.play(6)

        // check that the game is completed
        assertTrue(newGame5.winner == Player.X)
        // check that the winner is X
        assertTrue(newGame5.winner == Player.X)
        // check that you cannot play anymore
        val newGame6 = newGame5.play(2)
        assertTrue(newGame6.winner == Player.X)
        // check that the last play did not change the board
        assertTrue(newGame6.board.squares[2].value == SquareValue.EMPTY)
    }

    @Test
    fun getBoard() {
        val game = Game()
        assertTrue(game.board.squares.size == 9)

        val board = Board()
        assertTrue(board == game.board)
    }

    @Test
    fun getCurrentPlayer() {
        val game = Game()
        assertTrue(game.currentPlayer == Player.X)
        val game2 = game.play(0)
        assertTrue(game2.currentPlayer == Player.O)
    }

    @Test
    fun getWinner() {
        val game = Game()
        assertTrue(game.winner == null)
        val game2 = game.play(0)
        assertTrue(game2.winner == null)
        val game3 = game2.play(1)
        assertTrue(game3.winner == null)
        val game4 = game3.play(3)
        assertTrue(game4.winner == null)
        val game5 = game4.play(4)
        assertTrue(game5.winner == null)
        val game6 = game5.play(6)
        assertTrue(game6.winner == Player.X)
    }
}