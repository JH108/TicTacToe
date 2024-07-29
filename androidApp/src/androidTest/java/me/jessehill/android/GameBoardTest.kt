package me.jessehill.android

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.benasher44.uuid.uuid4
import me.jessehill.android.ui.GameBoardHolder
import me.jessehill.models.Game
import me.jessehill.models.GameStatus
import me.jessehill.models.User
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

    @Test
    fun `test that the loading state is displayed when the game is loading`() {
        composeTestRule.setContent {
            TicTacToeTheme {
                GameBoardHolder(
                    state = EMPTY_GAME_BOARD_STATE.copy(
                        isLoading = true,
                        currentGame = DEFAULT_GAME,
                        user = PLAYER_X
                    ),
                    onSaveGame = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Loading the game data...").assertIsDisplayed()
    }

    @Test
    fun `test that the game board renders with the opponents move`() {
        composeTestRule.setContent {
            TicTacToeTheme {
                GameBoardHolder(
                    state = EMPTY_GAME_BOARD_STATE.copy(
                        currentGame = DEFAULT_GAME.copy(
                            playerToMove = PLAYER_O
                        ),
                        user = PLAYER_X
                    ),
                    onSaveGame = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Waiting on ${PLAYER_O.username}...").assertIsDisplayed()
    }

    @Test
    fun `test that the game board renders with the users move`() {
        composeTestRule.setContent {
            TicTacToeTheme {
                GameBoardHolder(
                    state = EMPTY_GAME_BOARD_STATE.copy(
                        currentGame = DEFAULT_GAME.copy(
                            playerToMove = PLAYER_X
                        ),
                        user = PLAYER_X
                    ),
                    onSaveGame = {}
                )
            }
        }

        composeTestRule.onNodeWithText("It's your turn!").assertIsDisplayed()
    }

    @Test
    fun `test that the game board renders with the draw state`() {
        composeTestRule.setContent {
            TicTacToeTheme {
                GameBoardHolder(
                    state = EMPTY_GAME_BOARD_STATE.copy(
                        currentGame = DEFAULT_GAME.copy(
                            status = GameStatus.DRAW
                        ),
                        user = PLAYER_X
                    ),
                    onSaveGame = {}
                )
            }
        }

        composeTestRule.onNodeWithText("The game is a draw.").assertIsDisplayed()
    }

    @Test
    fun `test that the game board renders with the O win state when the user is O`() {
        composeTestRule.setContent {
            TicTacToeTheme {
                GameBoardHolder(
                    state = EMPTY_GAME_BOARD_STATE.copy(
                        currentGame = DEFAULT_GAME.copy(
                            status = GameStatus.O_WON
                        ),
                        user = PLAYER_O
                    ),
                    onSaveGame = {}
                )
            }
        }

        composeTestRule.onNodeWithText("You have won the game.").assertIsDisplayed()
    }

    @Test
    fun `test that the game board renders with the O win state when the user is X`() {
        composeTestRule.setContent {
            TicTacToeTheme {
                GameBoardHolder(
                    state = EMPTY_GAME_BOARD_STATE.copy(
                        currentGame = DEFAULT_GAME.copy(
                            status = GameStatus.O_WON
                        ),
                        user = PLAYER_X
                    ),
                    onSaveGame = {}
                )
            }
        }

        composeTestRule.onNodeWithText("${PLAYER_O.username} has won the game.").assertIsDisplayed()
    }

    @Test
    fun `test that the game board renders with the X win state when the user is X`() {
        composeTestRule.setContent {
            TicTacToeTheme {
                GameBoardHolder(
                    state = EMPTY_GAME_BOARD_STATE.copy(
                        currentGame = DEFAULT_GAME.copy(
                            status = GameStatus.X_WON
                        ),
                        user = PLAYER_X
                    ),
                    onSaveGame = {}
                )
            }
        }

        composeTestRule.onNodeWithText("You have won the game.").assertIsDisplayed()
    }

    @Test
    fun `test that the game board renders with the X win state when the user is O`() {
        composeTestRule.setContent {
            TicTacToeTheme {
                GameBoardHolder(
                    state = EMPTY_GAME_BOARD_STATE.copy(
                        currentGame = DEFAULT_GAME.copy(
                            status = GameStatus.X_WON
                        ),
                        user = PLAYER_O
                    ),
                    onSaveGame = {}
                )
            }
        }

        composeTestRule.onNodeWithText("${PLAYER_X.username} has won the game.").assertIsDisplayed()
    }

    companion object {
        private val PLAYER_X_USER_ID = uuid4()
        private val PLAYER_O_USER_ID = uuid4()
        private val PLAYER_X = User(
            id = PLAYER_X_USER_ID,
            firstName = "Jesse",
            lastName = "Hill",
            username = "jessehill"
        )
        private val PLAYER_O = User(
            id = PLAYER_O_USER_ID,
            firstName = "John",
            lastName = "Doe",
            username = "johndoe"
        )
        private val GAME_ID = uuid4()
        private val DEFAULT_GAME = Game(
            id = GAME_ID,
            playerX = PLAYER_X,
            playerO = PLAYER_O,
            playerToMove = PLAYER_X,
        )

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