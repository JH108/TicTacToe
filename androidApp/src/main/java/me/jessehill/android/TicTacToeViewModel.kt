package me.jessehill.android

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.jessehill.models.Game
import me.jessehill.models.User
import me.jessehill.models.UserStats
import me.jessehill.network.TicTacToeApi
import me.jessehill.tictactoe.UIRoute

data class TicTacToeState(
    val board: Game?,
    val leaderboard: List<Pair<User, UserStats>>,
    val users: List<User>,
    val user: User?,
    val userHistory: List<Game>,
    val isLoading: Boolean
)

class TicTacToeViewModel(
    val ticTacToeApi: TicTacToeApi
) : ViewModel() {
    var state: TicTacToeState by mutableStateOf(
        TicTacToeState(
            board = null,
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
        state = state.copy(user = user)
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
                board = game,
                userHistory = state.userHistory + game,
                isLoading = false
            )
        }
    }
}