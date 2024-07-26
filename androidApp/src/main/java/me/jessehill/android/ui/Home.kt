package me.jessehill.android.ui

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import me.jessehill.android.TicTacToeState
import me.jessehill.android.ui.components.CenteredColumn
import me.jessehill.models.User
import me.jessehill.tictactoe.UIRoute

@Composable
fun Home(
    state: TicTacToeState,
    onCompleteOnboarding: (User) -> Unit,
    onLoad: () -> Unit,
    onNavigate: (UIRoute) -> Unit
) {
    val showOnboarding by remember(state.user) {
        mutableStateOf(state.user == null)
    }
    LaunchedEffect(key1 = true, block = { onLoad() })

    CenteredColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Welcome to TicTacToe!",
            style = MaterialTheme.typography.headlineMedium
        )
        // TODO: Add animations to this

        if (showOnboarding) {
            Onboarding(onCompleteOnboarding = onCompleteOnboarding, onNavigate = onNavigate)
        } else {
            Crossfade(
                targetState = state.isLoading,
                label = "home-loading-animation"
            ) { isLoading ->
                if (isLoading) {
                    Text(text = "Loading the app data...")
                } else {
                    ReadyToPlay(user = state.user, onNavigate = onNavigate)
                }
            }
        }
    }
}

data class OnboardingUser(
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
)

fun OnboardingUser.isComplete() = username != null && firstName != null && lastName != null
fun OnboardingUser.toUser(): User? {
    if (!isComplete()) return null

    return User(
        username = username!!,
        firstName = firstName!!,
        lastName = lastName!!
    )
}

@Composable
fun Onboarding(
    modifier: Modifier = Modifier,
    onCompleteOnboarding: (User) -> Unit,
    onNavigate: (UIRoute) -> Unit
) {
    var onboardingUser by remember {
        mutableStateOf(OnboardingUser())
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Please create a user or sign in",
            style = MaterialTheme.typography.titleMedium
        )

        // input fields for username, first name, last name
        TextField(
            value = onboardingUser.username ?: "",
            onValueChange = {
                onboardingUser = onboardingUser.copy(username = it)
            },
            placeholder = { Text(text = "Enter a Username") },
            label = { Text(text = "Username") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        TextField(
            value = onboardingUser.firstName ?: "",
            onValueChange = {
                onboardingUser = onboardingUser.copy(firstName = it)
            },
            placeholder = { Text(text = "Enter your First Name") },
            label = { Text(text = "First Name") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )

        TextField(
            value = onboardingUser.lastName ?: "",
            onValueChange = {
                onboardingUser = onboardingUser.copy(lastName = it)
            },
            placeholder = { Text(text = "Enter your Last Name") },
            label = { Text(text = "Last Name") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        )

        Button(
            enabled = onboardingUser.isComplete(),
            onClick = {
                onboardingUser.toUser()?.let { user ->
                    Log.v("Onboarding", "User: $user")
                    onCompleteOnboarding(
                        User(
                            username = user.username,
                            firstName = user.firstName,
                            lastName = user.lastName
                        )
                    )
                    onNavigate(UIRoute.Home)
                }
            }
        ) {
            Text(text = "Continue")
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

        Button(
            onClick = {
                onNavigate(UIRoute.Play)
            }
        ) {
            Text(text = if (user == null) "Get Started!" else "Play")
        }
    }
}
