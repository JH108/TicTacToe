package me.jessehill.android.ui

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
import androidx.compose.runtime.*
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
    onSignIn: (String) -> Unit,
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
        val welcomeMessage = if (state.user != null) {
            "Welcome, ${state.user.username}!"
        } else {
            "Welcome to TicTacToe!"
        }

        Text(
            text = welcomeMessage,
            style = MaterialTheme.typography.headlineMedium
        )
        // TODO: Add animations to this

        if (showOnboarding) {
            Onboarding(
                onCompleteOnboarding = onCompleteOnboarding,
                onSignIn = onSignIn,
                error = state.onboardingErrorMessage
            )
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

    // We can force unwrap here because isComplete checks all the values for null
    return User(
        username = username!!,
        firstName = firstName!!,
        lastName = lastName!!
    )
}

@Composable
fun Onboarding(
    modifier: Modifier = Modifier,
    error: String?,
    onSignIn: (String) -> Unit,
    onCompleteOnboarding: (User) -> Unit,
) {
    var onboardingUser by remember {
        mutableStateOf(OnboardingUser())
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Sign in
        Column {
            Text(
                text = "Please sign in",
                style = MaterialTheme.typography.titleMedium
            )

            TextField(
                value = onboardingUser.username ?: "",
                onValueChange = {
                    onboardingUser = onboardingUser.copy(username = it)
                },
                isError = error != null,
                placeholder = {
                    if (error != null) {
                        Text(text = error)
                    } else {
                        Text(text = "Enter your Username")
                    }
                },
                label = { Text(text = "Username") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onboardingUser.username?.let { username ->
                            onSignIn(username)
                        }
                    }
                )
            )

            Button(
                enabled = onboardingUser.username != null,
                content = { Text(text = "Sign In") },
                onClick = {
                    onboardingUser.username?.let { username ->
                        onSignIn(username)
                    }
                }
            )
        }

        // Create a user
        Column {
            Text(
                text = "Please create a user or sign in",
                style = MaterialTheme.typography.titleMedium
            )

            TextField(
                value = onboardingUser.username ?: "",
                onValueChange = {
                    onboardingUser = onboardingUser.copy(username = it)
                },
                placeholder = { Text(text = "Enter a Username") },
                label = { Text(text = "Username") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                )
            )

            TextField(
                value = onboardingUser.firstName ?: "",
                onValueChange = {
                    onboardingUser = onboardingUser.copy(firstName = it)
                },
                placeholder = { Text(text = "Enter your First Name") },
                label = { Text(text = "First Name") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                )
            )

            TextField(
                value = onboardingUser.lastName ?: "",
                onValueChange = {
                    onboardingUser = onboardingUser.copy(lastName = it)
                },
                placeholder = { Text(text = "Enter your Last Name") },
                label = { Text(text = "Last Name") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (onboardingUser.isComplete()) {
                            onboardingUser.toUser()?.let { user ->
                                onCompleteOnboarding(user)
                            }
                        }
                    }
                )
            )

            Button(
                enabled = onboardingUser.isComplete(),
                onClick = {
                    onboardingUser.toUser()?.let { user ->
                        onCompleteOnboarding(user)
                    }
                }
            ) {
                Text(text = "Register User")
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

        Button(
            onClick = {
                onNavigate(UIRoute.FindMatch)
            }
        ) {
            Text(text = if (user == null) "Get Started!" else "Play")
        }
    }
}
