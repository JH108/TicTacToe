import com.benasher44.uuid.uuid4
import features.tictactoe.TicTacToe
import kotlinx.browser.document
import me.jesse.models.Game
import me.jesse.models.User
import react.create
import react.dom.client.createRoot

fun main() {
    val container = document.createElement("div")
    document.body!!.appendChild(container)

    val playerX = User("jh", uuid4(), "Jesse", "Hill")
    val playerO = User("js", uuid4(), "John", "Smith")

    val game = TicTacToe.create {
        game = Game(
            playerX = playerX,
            playerO = playerO,
            playerToMove = playerX
        )
    }

    createRoot(container).render(game)
}