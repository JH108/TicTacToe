package me.jessehill.android

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.jessehill.models.Game
import me.jessehill.models.GameStatus
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
        onWatchForGameUpdates()
    }

    // Base this on the game state so that we are creating a state flow whenever the game state changes
    // then if the state is null we won't change anything and if the state is not null we will update the board
    // we can also used distinctUntilChanged to avoid unnecessary recompositions
    fun onWatchForGameUpdates() {
        viewModelScope.launch {
            while (true) {
                if (
                    state.user != null &&
                    state.currentGame?.status == GameStatus.IN_PROGRESS
                ) {
                    // Refresh the current game
                    val updatedGame = ticTacToeApi.getGame(state.currentGame?.id.toString())

                    state = state.copy(
                        currentGame = updatedGame
                    )
                }
                delay(5000)
            }
        }
    }

    fun loadExistingUserById(userId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            state = state.copy()
        }
    }

    fun onCompleteOnboarding(user: User) {
        Log.v("TicTacToeViewModel", "User: $user")
        state = state.copy(isLoading = true)

        viewModelScope.launch {
            val userResult = runCatching {
                ticTacToeApi.registerUser(user)
            }

            state = if (userResult.isSuccess) {
                Log.v("TicTacToeViewModel", "User registered successfully")
                val userResultData = userResult.getOrNull()

                if (userResultData != null) {
                    Log.v("TicTacToeViewModel", "Returned User: $userResultData")
                    onLoadUserProfile(userResultData.username)
                    fetchUserHistory()
                    fetchLastGame()
                }

                state.copy(
                    user = userResultData,
                    authStatus = AuthStatus.AUTHENTICATED,
                    isLoading = false
                )
            } else {
                Log.e("TicTacToeViewModel", "Failed to register user")
                userResult.exceptionOrNull()?.printStackTrace()
                state.copy(
                    authStatus = AuthStatus.UNAUTHENTICATED,
                    isLoading = false
                )
            }
        }
    }

    suspend fun onLoadUserProfile(username: String) {
        state = state.copy(isLoading = true)

        Log.v("TicTacToeViewModel", "Loading user profile for $username")

        state = state.copy(
            user = ticTacToeApi.getUserProfileByUsername(username),
            isLoading = false
        )
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

    suspend fun fetchUserHistory() {
        state = state.copy(isLoading = true)

        Log.v("TicTacToeViewModel", "Fetching user history")
        Log.v("TicTacToeViewModel", "User: ${state.user}")

        val userId = state.user?.id

        if (userId == null) {
            state = state.copy(isLoading = false)
            return
        }

        state = state.copy(
            userHistory = ticTacToeApi.getGamesForUserId(userId.toString()),
            isLoading = false
        )
    }

    suspend fun fetchGame(id: String): Game {
        Log.v("TicTacToeViewModel", "Fetching game with ID: $id")

        return ticTacToeApi.getGame(id)
    }

    // TODO: Remove this. This makes no sense, we should just load the last game from history and display it
    //  that can be handled in the UI layer
    suspend fun fetchLastGame() {
        state = state.copy(isLoading = true)

        Log.v("TicTacToeViewModel", "Fetching active game for user")
        Log.v("TicTacToeViewModel", "User History: ${state.userHistory}")

        if (state.userHistory.isEmpty()) {
            state = state.copy(isLoading = false)
            return
        }

        val lastGame = state.userHistory.last()
        val activeGame = ticTacToeApi.getGame(lastGame.id.toString())

        state = state.copy(
            currentGame = activeGame,
            isLoading = false
        )
    }

    fun onSaveGame(game: Game) {
        state = state.copy(isLoading = true)

        viewModelScope.launch {
            val updatedGame = ticTacToeApi.saveGame(game)

            state = state.copy(
                currentGame = updatedGame,
                userHistory = state.userHistory.map {
                    if (it.id.toString() == updatedGame.id.toString()) updatedGame else it
                },
                isLoading = false
            )
        }
    }

    suspend fun onStartMatch(user: User, opponent: User) {
        state = state.copy(isLoading = true)

        val game = ticTacToeApi.startGame(user, opponent)

        state = state.copy(
            currentGame = game,
            userHistory = state.userHistory + game,
            isLoading = false
        )
    }

    fun onLogout() {
        state = state.copy(
            user = null,
            userHistory = emptyList(),
            currentGame = null,
            authStatus = AuthStatus.UNAUTHENTICATED,
            isLoading = false
        )
    }
}