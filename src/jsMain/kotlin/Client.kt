import kotlinx.browser.document
import me.jesse.tictactoe.Game
import react.create
import react.dom.client.createRoot

fun main() {
    val container = document.createElement("div")
    document.body!!.appendChild(container)

    val game = TicTacToe.create {
        game = Game()
    }

    createRoot(container).render(game)
}