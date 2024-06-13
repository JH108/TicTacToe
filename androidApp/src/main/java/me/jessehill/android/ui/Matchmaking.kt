package me.jessehill.android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.jessehill.android.ui.components.CenteredColumn
import me.jessehill.models.User

@Composable
fun Matchmaking(
    users: List<User>,
    activeUser: User? = null,
    onStartMatch: (User, User) -> Unit
) {
    CenteredColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        if (activeUser == null) {
            Text(text = "Please log in to start a match.")
        } else {
            val opponents by remember {
                derivedStateOf {
                    users.filter { it.id.toString() != activeUser.id.toString() }
                }
            }

            opponents.forEach { opponent ->
                UserCard(
                    user = opponent,
                    onStartMatch = { onStartMatch(activeUser, opponent) }
                )
            }
        }
    }
}

@Composable
fun UserCard(modifier: Modifier = Modifier, user: User, onStartMatch: (User) -> Unit) {
    Card(modifier = modifier.clickable { onStartMatch(user) }) {
        CenteredColumn(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = user.firstName)
            Text(text = user.lastName)
        }
    }
}
