package me.jessehill.android

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.jessehill.android.ui.GameBoardHolder
import me.jessehill.android.ui.Home
import me.jessehill.android.ui.Leaderboard
import me.jessehill.android.ui.Matchmaking
import me.jessehill.android.ui.Profile
import me.jessehill.network.TicTacToeApi
import me.jessehill.tictactoe.UIRoute

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    private val ticTacToeApi by lazy { TicTacToeApi() }
    private val ticTacToeViewModel by viewModels<TicTacToeViewModel> {
        viewModelFactory {
            initializer {
                TicTacToeViewModel(
                    ticTacToeApi = ticTacToeApi
                )
            }
        }
    }
    private val USER_ID = stringPreferencesKey("user_id")
    val userIdFlow: Flow<String> by lazy {
        applicationContext.dataStore.data
            .map { preferences ->
                Log.v("MainActivity:Datastore", "userIdFlow: ${preferences[USER_ID]}")

                preferences[USER_ID] ?: ""
            }
    }

    suspend fun saveUserId(userId: String) {
        applicationContext.dataStore.edit { settings ->
            Log.v("MainActivity", "saveUserId: $userId")
            settings[USER_ID] = userId
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var backstack by remember {
                mutableStateOf(listOf<UIRoute>(UIRoute.Home))
            }
            val currentRoute by remember(backstack) {
                derivedStateOf { backstack.last() }
            }
            val coroutineScope = rememberCoroutineScope()

            BackHandler {
                if (backstack.size > 1) {
                    backstack = backstack.dropLast(1)
                } else {
                    finish()
                }
            }

            LaunchedEffect(key1 = true, block = {
                userIdFlow.map { userId ->
                    Log.v("MainActivity", "userIdFlow: $userId")
                    if (userId.isNotEmpty()) {
                        ticTacToeViewModel.loadExistingUserById(userId)
                    }
                }
            })

            TicTacToeTheme(
                darkTheme = false
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            title = { Text(text = "Tic Tac Toe - ${currentRoute.title}") }
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Crossfade(
                                targetState = currentRoute,
                                label = "navigation-container"
                            ) { route ->
                                when (route) {
                                    is UIRoute.Home -> Home(
                                        state = ticTacToeViewModel.state,
                                        onLoad = { ticTacToeViewModel.onInitialLoad() },
                                        onSignIn = {
                                            coroutineScope.launch {
                                                val success = ticTacToeViewModel.onLoadUserProfile(it)
                                                if (success)
                                                    backstack = backstack + listOf(UIRoute.Profile)
                                            }
                                        },
                                        onCompleteOnboarding = {
                                            ticTacToeViewModel.onCompleteOnboarding(it)
                                            coroutineScope.launch {
                                                saveUserId(it.id.toString())
                                            }
                                        },
                                        onNavigate = { backstack = backstack + listOf(it) }
                                    )

                                    is UIRoute.FindMatch -> Matchmaking(
                                        users = ticTacToeViewModel.state.users,
                                        activeUser = ticTacToeViewModel.state.user,
                                        onStartMatch = { user, opponent ->
                                            coroutineScope.launch {
                                                ticTacToeViewModel.onStartMatch(
                                                    user = user,
                                                    opponent = opponent
                                                )
                                                backstack = backstack + listOf(UIRoute.Play)
                                            }
                                        }
                                    )

                                    is UIRoute.Profile -> Profile(
                                        user = ticTacToeViewModel.state.user,
                                        userHistory = ticTacToeViewModel.state.userHistory,
                                        onLoadUserHistory = {
                                            coroutineScope.launch {
                                                ticTacToeViewModel.fetchUserHistory()
                                            }
                                        },
                                        onLoadGame = {
                                            coroutineScope.launch {
                                                ticTacToeViewModel.loadGame(it)
                                            }
                                        },
                                        onLogout = {
                                            ticTacToeViewModel.onLogout()

                                            coroutineScope.launch {
                                                saveUserId("")
                                            }
                                        }
                                    )

                                    is UIRoute.Leaderboard -> Leaderboard(state = ticTacToeViewModel.state)
                                    is UIRoute.Play -> GameBoardHolder(
                                        state = ticTacToeViewModel.state,
                                        onSaveGame = { ticTacToeViewModel.onSaveGame(it) })
                                }
                            }
                        }
                        BottomAppBar(
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            BottomNavigation(
                                onNavigate = { backstack = backstack + listOf(it) },
                                route = currentRoute
                            )
                        }
                    }
                }
            }
        }
    }
}
