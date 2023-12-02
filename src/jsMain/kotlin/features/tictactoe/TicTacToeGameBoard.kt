package features.tictactoe

import ClientConfiguration
import UserContext
import csstype.*
import emotion.react.css
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.js.get
import kotlinx.serialization.encodeToString
import mainScope
import me.jesse.models.Game
import me.jesse.serializers.CommonSerializerModule
import me.jesse.tictactoe.Board
import me.jesse.tictactoe.MoveSymbol
import me.jesse.tictactoe.Square
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import react.*
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h4
import react.router.useParams

/**
 * This route should only be navigated to after a game has been created and an id has been returned from the server.
 * The id will be used to fetch the game from the server and then the game will be rendered.
 * Every time there is a move made the game will be updated on the server.
 * The user can only make a move if they are the current player.
 */
val TicTacToeGameBoard = FC<Props> { props ->
    val params = useParams()
    val gameId = params["gameId"] ?: ""
    val currentUser = useContext(UserContext)?.first

    var game by useState<Game?>(null)
    var board by useState(Board())

    useEffectOnce {
        if (gameId.isNotEmpty()) {
            mainScope.launch {
                game = fetchGame(gameId)
                board = Board()
            }
        }
    }

    // if the game changes then update the board with the new moves
    useEffect(game) {
        game?.let { safeGame ->
            board = board.copy(squares = board.squares.mapIndexed { index, square ->
                if (safeGame.moves.containsKey(index)) {
                    square.copy(value = safeGame.moves[index]?.moveSymbol ?: MoveSymbol.EMPTY)
                } else square
            })
        }
    }

    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
            height = 100.vh
        }

        if (game?.playerToMove != currentUser) {
            h4 {
                +"Waiting for other player to move..."
            }
        }

        h4 {
            +"Game Status: ${game?.status}"
        }

        button {
            css {
                margin = 10.px
                padding = 10.px
                backgroundColor = Color("#a5d8f5")
            }

            onClick = {
                mainScope.launch {
                    game = fetchGame(gameId)
                }
            }

            +"Refresh"
        }

        val boardUi = BoardUI.create {
            squares = board.squares
            onSquareClick = { index ->
                if (game?.playerToMove == currentUser) {

                    game?.let { safeGame ->
                        val gameWithPlay = safeGame.play(index)

                        mainScope.launch {
                            saveGame(gameWithPlay)
                        }

                        game = gameWithPlay
                    }
                }
            }
        }

        child(boardUi)
    }
}

external interface BoardUIProps : Props {
    var squares: List<Square>
    var onSquareClick: (Int) -> Unit
}

val BoardUI = FC<BoardUIProps> { props ->
    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.row
            flexWrap = FlexWrap.wrap
            maxWidth = 400.px
            margin = Margin(Auto.auto, Auto.auto)
            gap = 10.px
        }

        props.squares.forEachIndexed { index, square ->
            child(
                SquareUI.create {
                    this.square = square
                    onSquareClick = { props.onSquareClick(index) }
                }
            )
        }
    }
}

external interface SquareUIProps : Props {
    var square: Square
    var onSquareClick: () -> Unit
}

val SquareUI = FC<SquareUIProps> { props ->
    div {
        css {
            maxWidth = 90.px
            maxHeight = 90.px
            minWidth = 90.px
            minHeight = 90.px
            border = Border(
                width = 1.px,
                style = LineStyle.solid,
                color = rgb(0, 0, 0)
            )
            display = Display.flex
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
            padding = 10.px
            fontSize = 36.px
        }
        onClick = {
            props.onSquareClick()
        }

        if (props.square.value != MoveSymbol.EMPTY)
            +props.square.value.toString()
    }
}

suspend fun fetchGame(gameId: String): Game {
    val response = window.fetch(
        "${ClientConfiguration.apiUrl}/games/load/$gameId"
    )
        .await()
        .text()
        .await()

    return CommonSerializerModule.json.decodeFromString(response)
}

suspend fun saveGame(game: Game): Game {
    val response = window.fetch(
        input = ClientConfiguration.apiUrl + "/games/save",
        init = RequestInit(
            method = "POST",
            headers = Headers().apply {
                set("Content-Type", "application/json")
            },
            body = CommonSerializerModule.json.encodeToString(game)
        ),
    )
        .await()
        .text()
        .await()

    // TODO: Utilize the errors passed back from the server

    return CommonSerializerModule.json.decodeFromString(response)
}
