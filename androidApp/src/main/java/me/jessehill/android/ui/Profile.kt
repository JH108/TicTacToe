package me.jessehill.android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid
import kotlinx.datetime.*
import me.jessehill.android.TicTacToeTheme
import me.jessehill.android.ui.components.CenteredColumn
import me.jessehill.models.Game
import me.jessehill.models.GameStatus
import me.jessehill.models.User
import java.time.format.DateTimeFormatter

@Composable
fun Profile(
    user: User?,
    userHistory: List<Game>,
    onLoadUserHistory: () -> Unit,
    onLoadGame: (String) -> Unit,
    onLogout: () -> Unit
) {
    LaunchedEffect(key1 = true, block = { onLoadUserHistory() })

    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user == null) {
            Text(text = "Please log in to view your profile.")
        }

        if (user != null) {
            Text(
                text = "Welcome, ${user.username}!",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(text = "Your game history:")
        }

        if (userHistory.isNotEmpty() && user != null) {
            val userHistoryGridColumns = GridCells.Adaptive(200.dp)
            LazyVerticalGrid(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                columns = userHistoryGridColumns,
                content = {
                    userHistory.forEach { game ->
                        item {
                            UserHistoryCard(
                                user = user,
                                game = game,
                                onLoadGame = { onLoadGame(game.id.toString()) }
                            )
                        }
                    }
                }
            )
        }

        if (user != null)
            Button(
                modifier = Modifier.height(40.dp),
                onClick = onLogout
            ) {
                Text(text = "Logout")
            }
    }
}

@Composable
fun UserHistoryCard(
    user: User,
    game: Game,
    onLoadGame: () -> Unit
) {
    Card(
        modifier = Modifier.clickable {
            onLoadGame()
        }
    ) {
        CenteredColumn(
            modifier = Modifier.padding(8.dp)
        ) {
            val timeFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a")

            Text(
                text = "${user.username} vs ${game.opponent(user).username}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Result: ${game.resultMessage()}")
            Text(
                text = "Started At: ${game.startTime.toJavaLocalDateTime().format(timeFormatter)}"
            )
            if (game.endTime != null) {
                Text(text = "Ended At: ${game.endTime?.toJavaLocalDateTime()?.format(timeFormatter)}")
            }
            Text(text = "Game ID: ${game.id}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

fun Game.resultMessage(): String {
    return when (status) {
        GameStatus.X_WON -> "${playerX.username} won!"
        GameStatus.O_WON -> "${playerO.username} won!"
        GameStatus.DRAW -> "It's a draw!"
        else -> "Game in progress..."
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
            ),
            onLoadGame = {}
        )
    }
}
