package me.jessehill.android.ui

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import me.jessehill.tictactoe.Board
import me.jessehill.tictactoe.MoveSymbol
import me.jessehill.tictactoe.setSquares

@Composable
fun GameBoard(state: TicTacToeState) {
    var board by remember {
        mutableStateOf(Board())
    }

    LaunchedEffect(key1 = state.currentGame, block = {
        if (state.currentGame != null) {
            board = board.setSquares(state.currentGame)
        }
    })

    CenteredColumn {
        Column {
            Text(text = "Game Board")
        }

        GameBoardUI(
            modifier = Modifier,
            board = board,
            onClick = { index ->
                Log.v("GameBoard", "Clicked on cell $index")
            }
        )
    }
}

@Composable
fun GameBoardUI(
    modifier: Modifier = Modifier,
    board: Board,
    onClick: (Int) -> Unit
) {
    Column(modifier = modifier.padding(16.dp)) {
        board.squares.windowed(3, 3).forEachIndexed { rowIndex, row ->
            Row {
                row.forEachIndexed { index, cell ->
                    BoardCell(
                        value = cell.value,
                        test = (rowIndex * 3 + index).toString(),
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
    CenteredColumn(
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.onSurface)
            .padding(8.dp)
            .clickable(
                enabled = value == MoveSymbol.EMPTY,
            ) {
                onClick()
            }
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
            BoardCell(value = MoveSymbol.X, onClick = {})
        }
    }
}

@Preview
@Composable
fun PreviewBoardCellO() {
    TicTacToeTheme {
        Surface {
            BoardCell(value = MoveSymbol.O, onClick = {})
        }
    }
}

@Preview
@Composable
fun PreviewBoardCellEmpty() {
    TicTacToeTheme {
        Surface {
            BoardCell(value = MoveSymbol.EMPTY, onClick = {})
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
