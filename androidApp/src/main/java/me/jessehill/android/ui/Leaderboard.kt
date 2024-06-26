package me.jessehill.android.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid
import me.jessehill.android.TicTacToeState
import me.jessehill.android.TicTacToeTheme
import me.jessehill.models.User
import me.jessehill.models.UserStats

@Composable
fun Leaderboard(state: TicTacToeState) {
    // list of users and their stats
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        columns = GridCells.Adaptive(200.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        state.leaderboard.forEach { (user, userStats) ->
            item {
                UserStatCard(
                    user = user,
                    userStats = userStats
                )
            }
        }
    }
}

@Composable
fun UserStatCard(
    modifier: Modifier = Modifier,
    user: User,
    userStats: UserStats
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = user.username)
            Text(text = "Win Percent: ${userStats.winPercentString}")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SuggestionChip(
                    onClick = { },
                    label = {
                        Text(text = "Total Games: ${userStats.totalGames}")
                    }
                )

                SuggestionChip(
                    onClick = { },
                    label = {
                        Text(text = "Wins: ${userStats.totalWins}")
                    }
                )

                SuggestionChip(
                    onClick = { },
                    label = {
                        Text(text = "Draws: ${userStats.totalDraws}")
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SuggestionChip(
                    onClick = { },
                    label = {
                        Text(text = "Wins as X: ${userStats.totalXWins}")
                    }
                )

                SuggestionChip(
                    onClick = { },
                    label = {
                        Text(text = "Wins as O: ${userStats.totalOWins}")
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewLeaderboard() {
    val userOne = User(
        id = Uuid.randomUUID(),
        username = "Jesse",
        firstName = "Jesse",
        lastName = "Hill"
    )
    val userTwo = User(
        id = Uuid.randomUUID(),
        username = "John",
        firstName = "John",
        lastName = "Doe"
    )
    val state = TicTacToeState(
        currentGame = null,
        leaderboard = listOf(
            Pair(
                userOne,
                UserStats(
                    id = userOne.id,
                    totalGames = 10,
                    totalDraws = 2,
                    totalXWins = 3,
                    totalOWins = 2
                )
            ),
            Pair(
                userTwo,
                UserStats(
                    id = userTwo.id,
                    totalGames = 5,
                    totalDraws = 1,
                    totalXWins = 1,
                    totalOWins = 1
                )
            )
        ),
        users = emptyList(),
        user = null,
        userHistory = emptyList(),
        isLoading = false
    )

    TicTacToeTheme {
        Surface {
            Leaderboard(state = state)
        }
    }
}
