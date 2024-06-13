package me.jessehill.android.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.jessehill.android.TicTacToeTheme
import me.jessehill.android.ui.components.CenteredColumn
import me.jessehill.models.Game
import me.jessehill.models.GameStatus
import me.jessehill.models.User

@Composable
fun Profile(
    user: User?,
    userHistory: List<Game>,
    onLogout: () -> Unit
) {
    CenteredColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        if (user == null) {
            Text(text = "Please log in to view your profile.")
            return@CenteredColumn
        }

        Text(
            text = "Welcome, ${user.username}!",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Your game history:"
        )

        val userHistoryGridColumns = GridCells.Adaptive(200.dp)

        LazyVerticalGrid(columns = userHistoryGridColumns, content = {
            userHistory.forEach { game ->
                item {
                    UserHistoryCard(
                        user = user,
                        game = game
                    )
                }
            }
        })

        Button(
            onClick = onLogout
        ) {
            Text(text = "Logout")
        }
    }
}

@Composable
fun UserHistoryCard(
    user: User,
    game: Game
) {
    Card {
        CenteredColumn(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "${user.username} vs ${game.opponent(user).username}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Result: ${game.status}")
            Text(text = "Started At: ${game.startTime}")
            if (game.endTime != null) {
                Text(text = "Ended At: ${game.endTime}")
            }
            Text(text = "Game ID: ${game.id}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

fun Game.opponent(user: User) =
    if (playerX.id.toString() == user.id.toString()) playerO else playerX

@Preview
@Composable
fun PreviewUserHistoryCard() {
    TicTacToeTheme {
        val userOneId = Uuid.randomUUID()
        val userTwoId = Uuid.randomUUID()

        UserHistoryCard(
            user = User(
                id = userOneId,
                username = "gymbro",
                firstName = "Jim",
                lastName = "Bro"
            ),
            game = Game(
                id = userOneId,
                playerX = User(
                    id = userOneId,
                    username = "gymbro",
                    firstName = "Jim",
                    lastName = "Bro"
                ),
                playerO = User(
                    id = userTwoId,
                    username = "yogurl",
                    firstName = "Sue",
                    lastName = "Per"
                ),
                status = GameStatus.IN_PROGRESS,
                startTime = Clock.System.now().toLocalDateTime(
                    TimeZone.currentSystemDefault()
                ),
                endTime = null,
                moves = emptyMap(),
                playerToMove = User(
                    id = userOneId,
                    username = "gymbro",
                    firstName = "Jim",
                    lastName = "Bro"
                )
            )
        )
    }
}
