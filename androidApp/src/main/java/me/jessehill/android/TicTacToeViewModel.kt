package me.jessehill.android

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.jessehill.models.Game
import me.jessehill.models.User
import me.jessehill.models.UserStats
import me.jessehill.network.TicTacToeApi

data class TicTacToeState(
    val currentGame: Game?,
    val leaderboard: List<Pair<User, UserStats>>,
    val users: List<User>,
    val user: User?,
    val userHistory: List<Game>,
    val isLoading: Boolean,
    val authStatus: AuthStatus = AuthStatus.INITIAL
)

enum class AuthStatus {
    AUTHENTICATED,
    UNAUTHENTICATED,
    INITIAL
}

class TicTacToeViewModel(
    val ticTacToeApi: TicTacToeApi
) : ViewModel() {
    var state: TicTacToeState by mutableStateOf(
        TicTacToeState(
            currentGame = null,
            leaderboard = emptyList(),
            users = emptyList(),
            user = null,
            userHistory = emptyList(),
            isLoading = false
        )
    )

    init {
        onInitialLoad()
    }

    fun onCompleteOnboarding(user: User) {
        state = state.copy(isLoading = true)

        viewModelScope.launch {
            val userResult = runCatching {
                ticTacToeApi.registerUser(user)
            }

            state = if (userResult.isSuccess) {
                state.copy(
                    user = userResult.getOrNull(),
                    authStatus = AuthStatus.AUTHENTICATED,
                    isLoading = false
                )
            } else {
                state.copy(
                    authStatus = AuthStatus.UNAUTHENTICATED,
                    isLoading = false
                )
            }
        }
    }

    fun onLoadUserProfile(username: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            state = state.copy(
                user = ticTacToeApi.getUserProfileByUsername(username),
                isLoading = false
            )
        }
    }

    fun onInitialLoad() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            state = state.copy(
                leaderboard = ticTacToeApi.getTopFivePlayers(),
                users = ticTacToeApi.getAllUsers(),
            )

            state = state.copy(isLoading = false)
        }
    }

    fun onReloadLeaderboard() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            state = state.copy(
                leaderboard = ticTacToeApi.getTopFivePlayers(),
            )

            state = state.copy(isLoading = false)
        }
    }

    fun onReloadUsers() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            state = state.copy(
                users = ticTacToeApi.getAllUsers(),
            )

            state = state.copy(isLoading = false)
        }
    }

    fun onStartMatch(user: User, opponent: User) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val game = ticTacToeApi.startGame(user, opponent)

            state = state.copy(
                currentGame = game,
                userHistory = state.userHistory + game,
                isLoading = false
            )
        }
    }
}