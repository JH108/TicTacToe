package me.jessehill.android.ui

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.jessehill.android.TicTacToeState
import me.jessehill.android.TicTacToeTheme
import me.jessehill.android.ui.components.CenteredColumn
import me.jessehill.models.Game
import me.jessehill.models.GameStatus
import me.jessehill.models.User
import me.jessehill.tictactoe.Board
import me.jessehill.tictactoe.MoveSymbol
import me.jessehill.tictactoe.Square
import me.jessehill.tictactoe.setSquares

@Composable
fun GameBoardHolder(
    state: TicTacToeState,
    onSaveGame: (Game) -> Unit
) {
    CenteredColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        val user = state.user
        val game = state.currentGame
        val isLoading = state.isLoading
        val opponent = remember(user, game) { if (user != null) game?.opponent(user) else null }

        if (user != null && game != null && opponent != null) {
            GameBoard(
                user = user,
                game = game,
                isLoading = isLoading,
                opponent = opponent,
                onSaveGame = onSaveGame
            )
        } else {
            Text(text = "No game data available.")
        }
    }
}

@Composable
fun GameBoard(
    user: User,
    game: Game,
    isLoading: Boolean,
    opponent: User,
    onSaveGame: (Game) -> Unit
) {
    var board by remember {
        mutableStateOf(Board())
    }

    LaunchedEffect(key1 = game, block = {
        board = board.setSquares(game)
    })

    Column {
        CenteredColumn {
            Text(text = "Game Board")
            Text(text = "Player X: ${game.playerX.username}")
            Text(text = "Player O: ${game.playerO.username}")

            Crossfade(targetState = game.status, label = "game-status-animation") { status ->
                when (status) {
                    GameStatus.IN_PROGRESS -> {
                        if (game.playerToMove.id.toString() == user.id.toString()) {
                            Text(text = "It's your turn!")
                        } else {
                            Text(text = "Waiting on ${opponent.username}...")
                        }
                    }

                    GameStatus.DRAW -> {
                        Text(text = "The game is a draw.")
                    }

                    GameStatus.O_WON -> {
                        if (game.playerO.id.toString() == user.id.toString()) {
                            Text(text = "You have won the game.")
                        } else {
                            Text(text = "${opponent.username} has won the game.")
                        }
                    }

                    GameStatus.X_WON -> {
                        if (game.playerX.id.toString() == user.id.toString()) {
                            Text(text = "You have won the game.")
                        } else {
                            Text(text = "${opponent.username} has won the game.")
                        }
                    }
                }

            }
        }

        Crossfade(targetState = isLoading, label = "game-loading-animation") {
            CenteredColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                when (it) {
                    true -> CenteredColumn {
                        Text(text = "Loading the game data...")
                        CircularProgressIndicator()
                    }

                    false -> GameBoardUI(
                        modifier = Modifier,
                        board = board,
                        onClick = { index ->
                            Log.v("GameBoard", "Clicked on cell $index")
                            val gameWithMove = game.play(index)

                            onSaveGame(gameWithMove)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GameBoardUI(
    modifier: Modifier = Modifier,
    board: Board,
    onClick: (Int) -> Unit
) {
    CenteredColumn(modifier = modifier.padding(16.dp)) {
        board.squares.windowed(3, 3).forEachIndexed { rowIndex, row ->
            Row {
                row.forEachIndexed { index, cell ->
                    BoardCell(
                        value = cell.value,
                        onClick = { onClick(rowIndex * 3 + index) }
                    )
                }
            }
        }
    }
}

@Composable
fun BoardCell(
    modifier: Modifier = Modifier,
    value: MoveSymbol,
    test: String? = null,
    onClick: () -> Unit
) {
    // TODO: Figure out how to make these cell sizes dynamic based on the size of the device
    CenteredColumn(
        modifier = modifier
            .sizeIn(
                minWidth = 64.dp,
                minHeight = 64.dp,
                maxWidth = 128.dp,
                maxHeight = 128.dp
            )
            .padding(4.dp)
            .border(1.dp, MaterialTheme.colorScheme.onSurface)
            .clickable(
                enabled = value == MoveSymbol.EMPTY,
            ) {
                onClick()
            }
            .padding(8.dp)
    ) {
        if (value != MoveSymbol.EMPTY)
            Text(text = value.toString())
        if (test != null)
            Text(text = test)
    }
}

@Preview
@Composable
fun PreviewBoardCellX() {
    TicTacToeTheme {
        Surface {
            Row {
                BoardCell(value = MoveSymbol.X, onClick = {})
            }
        }
    }
}

@Preview
@Composable
fun PreviewBoardCellO() {
    TicTacToeTheme {
        Surface {
            Row {
                BoardCell(value = MoveSymbol.O, onClick = {})
            }
        }
    }
}

@Preview
@Composable
fun PreviewBoardCellEmpty() {
    TicTacToeTheme {
        Surface {
            Row {
                BoardCell(value = MoveSymbol.EMPTY, onClick = {})
            }
        }
    }
}

@Preview
@Composable
fun PreviewEmptyGameBoard() {
    TicTacToeTheme {
        Surface {
            GameBoardUI(
                board = Board(),
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
fun PreviewFullGameBoard() {
    TicTacToeTheme {
        Surface {
            GameBoardUI(
                board = Board(
                    squares = List(9) { Square(MoveSymbol.X) }
                ),
                onClick = {}
            )
        }
    }
}
