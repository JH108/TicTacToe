package me.jessehill.android

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import me.jessehill.android.ui.GameBoardHolder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameBoardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `test that the game board renders with an empty UI`() {
        composeTestRule.setContent {
            TicTacToeTheme {
                GameBoardHolder(
                    state = EMPTY_GAME_BOARD_STATE,
                    onSaveGame = {}
                )
            }
        }

        composeTestRule.onNodeWithText("No game data available.").assertIsDisplayed()
    }

    companion object {
        private val EMPTY_GAME_BOARD_STATE = TicTacToeState(
            currentGame = null,
            leaderboard = emptyList(),
            users = emptyList(),
            user = null,
            userHistory = emptyList(),
            isLoading = false
        )
    }
}