package me.jessehill.android

import android.os.Bundle
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import me.jessehill.android.ui.GameBoard
import me.jessehill.android.ui.Home
import me.jessehill.android.ui.Leaderboard
import me.jessehill.android.ui.Matchmaking
import me.jessehill.android.ui.Profile
import me.jessehill.network.TicTacToeApi
import me.jessehill.tictactoe.UIRoute

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

            BackHandler {
                if (backstack.size > 1) {
                    backstack = backstack.dropLast(1)
                } else {
                    finish()
                }
            }

            TicTacToeTheme {
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
                        Crossfade(targetState = currentRoute, label = "navigation-container") { route ->
                            when (route) {
                                is UIRoute.Home -> Home(
                                    state = ticTacToeViewModel.state,
                                    onLoad = { ticTacToeViewModel.onInitialLoad() },
                                    onNavigate = { backstack = backstack + listOf(it) }
                                )
                                is UIRoute.FindMatch -> Matchmaking()
                                is UIRoute.Profile -> Profile()
                                is UIRoute.Leaderboard -> Leaderboard(state = ticTacToeViewModel.state)
                                is UIRoute.Play -> GameBoard(state = ticTacToeViewModel.state)
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
