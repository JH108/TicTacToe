package me.jessehill.android.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import me.jessehill.android.TicTacToeState
import me.jessehill.android.ui.components.CenteredColumn
import me.jessehill.models.User
import me.jessehill.tictactoe.UIRoute

@Composable
fun Home(
    state: TicTacToeState,
    onLoad: () -> Unit,
    onNavigate: (UIRoute) -> Unit
) {
    LaunchedEffect(key1 = true, block = { onLoad() })

    CenteredColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Welcome to TicTacToe!")

        Crossfade(targetState = state.isLoading, label = "home-loading-animation") { isLoading ->
            if (isLoading) {
                Text(text = "Loading the app data...")
            } else {
                ReadyToPlay(user = state.user, onNavigate = onNavigate)
            }
        }
    }
}

@Composable
fun ReadyToPlay(
    user: User?,
    onNavigate: (UIRoute) -> Unit
) {
    CenteredColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Ready to play?")
        Button(onClick = { onNavigate(UIRoute.Play) }) {
            Text(text = if (user == null) "Get Started!" else "Play")
        }
    }
}
