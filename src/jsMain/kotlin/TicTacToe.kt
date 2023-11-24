import csstype.*
import emotion.react.css
import me.jesse.models.Game
import me.jesse.tictactoe.Board
import me.jesse.tictactoe.Square
import me.jesse.tictactoe.SquareValue
import react.*
import react.dom.html.ReactHTML.div

external interface TicTacToeProps : Props {
    var game: Game
}

val TicTacToe = FC<TicTacToeProps> { props ->
    var game by useState(props.game)
    var board by useState(Board())

    // if the game changes then update the board with the new moves
    useEffect(game) {
        board = board.copy(squares = board.squares.mapIndexed { index, square ->
            if (game.moves.containsKey(index)) {
                square.copy(value = game.moves[index]?.moveSymbol ?: SquareValue.EMPTY)
            } else square
        })
    }

    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
            height = 100.vh
        }

        val boardUi = BoardUI.create {
            squares = board.squares
            onSquareClick = { index ->
                game = game.play(index)
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

        if (props.square.value != SquareValue.EMPTY)
            +props.square.value.toString()
    }
}
