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
import me.jessehill.tictactoe.Board
import me.jessehill.tictactoe.MoveSymbol
import me.jessehill.tictactoe.Square
import me.jessehill.tictactoe.setSquares

@Composable
fun GameBoard(
    state: TicTacToeState,
    onSaveGame: (Game) -> Unit
) {
    // TODO: This would be better as two separate composable functions
    //  this one should take only a non-null game and a function to save the game
    //  the other would take a nullable game and a function to save the game
    var board by remember {
        mutableStateOf(Board())
    }

    LaunchedEffect(key1 = state.currentGame, block = {
        if (state.currentGame != null) {
            board = board.setSquares(state.currentGame)
        }
    })

    CenteredColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        CenteredColumn {
            Text(text = "Game Board")
            Text(text = "Player X: ${state.currentGame?.playerX?.username}")
            Text(text = "Player O: ${state.currentGame?.playerO?.username}")

            if (state.currentGame?.playerToMove?.id?.toString() == state.user?.id?.toString()) {
                Text(text = "It's your turn!")
            } else {
                Text(text = "Waiting for the other player to move...")
            }
        }

        Crossfade(targetState = state.isLoading, label = "game-loading-animation") { isLoading ->
            CenteredColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                when (isLoading) {
                    true -> CenteredColumn {
                        Text(text = "Loading the game data...")
                        CircularProgressIndicator()
                    }

                    false -> GameBoardUI(
                        modifier = Modifier,
                        board = board,
                        onClick = { index ->
                            Log.v("GameBoard", "Clicked on cell $index")
                            val gameWithMove = state.currentGame?.play(index)

                            if (gameWithMove != null) {
                                onSaveGame(gameWithMove)
                            }
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
