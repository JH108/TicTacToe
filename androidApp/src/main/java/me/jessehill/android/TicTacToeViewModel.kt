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
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            state = state.copy(leaderboard = ticTacToeApi.getTopFivePlayers())
            state = state.copy(isLoading = false)
        }
    }
}