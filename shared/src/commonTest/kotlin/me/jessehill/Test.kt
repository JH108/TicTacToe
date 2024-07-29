package me.jessehill

import kotlin.test.Test
import kotlin.test.assertTrue

class GameTest {
    @Test
    fun play() {
        // create a game
        val game = Game(
            playerX = playerX,
            playerO = playerO,
            playerToMove = playerX
        )
        // check that the current player is x
        assertTrue(game.playerToMove == playerX)

        // make one play
        val newGame = game.play(0)

        // check that the next user is O and that there is one x on the board
        assertTrue(newGame.playerToMove == playerO)
        assertTrue(newGame.moves.size == 1)
        assertTrue(newGame.moves[0]?.playerId == playerX.id && newGame.moves[0]?.squareIndex == 0)
    }

    @Test
    fun `play on a square that is already taken`() {
        // create a game
        val game = Game(
            playerX = playerX,
            playerO = playerO,
            playerToMove = playerX
        )
        // check that the current player is x
        assertTrue(game.playerToMove == playerX)

        // make one play
        val newGame = game.play(0)
        // make another play on the same square
        val newGame2 = newGame.play(0)

        // check that the next user is O and that there is one x on the board
        assertTrue(newGame2.playerToMove == playerO)
        assertTrue(newGame2.moves.size == 1)

        // check that there are no o's on the board
        assertTrue(newGame2.moves.values.none { it.playerId == playerO.id })
    }

    @Test
    fun `finish the game`() {
        // create a game
        val game = Game(
            playerX = playerX,
            playerO = playerO,
            playerToMove = playerX
        )
        // check that the current player is x
        assertTrue(game.playerToMove == playerX)

        // make one play
        val newGame = game.play(0)
        // make another play on the same square
        val newGame2 = newGame.play(1)
        val newGame3 = newGame2.play(3)
        val newGame4 = newGame3.play(4)
        val newGame5 = newGame4.play(6)

        // check that the game is completed and that the winner is X
        assertTrue(newGame5.status == GameStatus.X_WON)
        // check that you cannot play anymore
        val newGame6 = newGame5.play(2)
        assertTrue(newGame5.status == GameStatus.X_WON)
        // check that the last play did not change the board
        assertTrue(newGame6.moves.size == 5)
        assertTrue(newGame6.moves.values.none { it.squareIndex == 2 })
    }

    @Test
    fun `finish a game where O wins`() {
        // create a game
        val game = Game(
            playerX = playerX,
            playerO = playerO,
            playerToMove = playerX
        )
        // check that the current player is x
        assertTrue(game.playerToMove == playerX)

        // make one play
        val newGame = game.play(0)
        // make another play on the same square
        val newGame2 = newGame.play(1)
        val newGame3 = newGame2.play(2)
        val newGame4 = newGame3.play(4)
        val newGame5 = newGame4.play(3)
        val newGame6 = newGame5.play(7)

        // check that the game is completed and that the winner is X
        assertTrue(newGame6.status == GameStatus.O_WON)
        // check that you cannot play anymore
        val newGame7 = newGame6.play(8)
        assertTrue(newGame6.status == GameStatus.O_WON)
        // check that the last play did not change the board
        assertTrue(newGame7.moves.size == 6)
        assertTrue(newGame7.moves.values.none { it.squareIndex == 8 })
    }

    @Test
    fun getCurrentPlayer() {
        val game = Game(
            playerX = playerX,
            playerO = playerO,
            playerToMove = playerX
        )
        assertTrue(game.playerToMove == playerX)

        val game2 = game.play(0)
        assertTrue(game2.playerToMove == playerO)
    }

    @Test
    fun `check that a game is completed when drawn`() {
        val game = Game(
            playerX = playerX,
            playerO = playerO,
            playerToMove = playerX
        )
        // play a game that ends in a draw
        val game2 = game.play(0) // x
        val game3 = game2.play(1) // o
        val game4 = game3.play(2) // x
        val game5 = game4.play(3) // o
        val game6 = game5.play(4) // x
        val game7 = game6.play(6) // o
        val game8 = game7.play(5) // x
        val game9 = game8.play(8) // o
        val game10 = game9.play(7) // x

        assertTrue(game10.status == GameStatus.DRAW)
        // check that another play doesn't change the board
        val game11 = game10.play(0)
        assertTrue(game11.status == GameStatus.DRAW)
        assertTrue(game11.moves.size == 9)
    }

    @Test
    fun `check that the end time is set when the game is completed`() {
        val game = Game(
            playerX = playerX,
            playerO = playerO,
            playerToMove = playerX
        )
        // play a game that ends in a draw
        val game2 = game.play(0) // x
        val game3 = game2.play(1) // o
        val game4 = game3.play(2) // x
        val game5 = game4.play(3) // o
        val game6 = game5.play(4) // x
        val game7 = game6.play(6) // o
        val game8 = game7.play(5) // x
        val game9 = game8.play(8) // o
        val game10 = game9.play(7) // x

        assertTrue(game10.status == GameStatus.DRAW)
        // check that the end time is set
        assertTrue(game10.endTime != null)
        // check that another play doesn't change the board
        val game11 = game10.play(0)
        assertTrue(game11.status == GameStatus.DRAW)
        assertTrue(game11.moves.size == 9)
    }

    companion object {
        val playerX = User("jh", uuid4(), "Jesse", "Hill")
        val playerO = User("js", uuid4(), "John", "Smith")
    }
}