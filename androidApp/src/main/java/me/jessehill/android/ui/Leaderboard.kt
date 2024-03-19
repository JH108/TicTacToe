package me.jessehill.android.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.jessehill.android.TicTacToeState

@Composable
fun Leaderboard(
    modifier: Modifier = Modifier
    state: TicTacToeState
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        state.leaderboard.forEach { (user, userStats) ->
            Card {
                Text(
                    text = "Total Games: ${userStats.totalGames}"
                )
                Row {
                    Text(
                        text = "Wins: ${userStats.totalWins}"
                    )
                    Text(
                        text = "Draws: ${userStats.totalDraws}"
                    )
                }
                Row {
                    Text(
                        text = "Wins as X: ${userStats.totalXWins}"
                    )
                    Text(
                        text = "Wins as O: ${userStats.totalOWins}"
                    )
                }
            }
        }
    }
}