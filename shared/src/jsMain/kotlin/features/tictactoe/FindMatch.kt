package features.tictactoe

import UserContext
import components.Card
import csstype.AlignItems
import csstype.Display
import csstype.FlexDirection
import csstype.JustifyContent
import csstype.pct
import emotion.react.css
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import mainScope
import me.jessehill.models.Game
import me.jessehill.models.StartGameRequestBody
import me.jessehill.models.User
import me.jessehill.network.ClientConfiguration
import me.jessehill.serializers.CommonSerializerModule
import me.jessehill.tictactoe.UIRoute
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p
import react.router.useNavigate
import react.useContext
import react.useEffectOnce
import react.useState

/**
 * This is a crud system for finding a match.
 * We will simply create a list of all the users in the system and allow the user to select one.
 * Then we will create a new game instance with the two users as long as they don't have an open game.
 * The new game will be listed under the users profile as an "active game".
 */
val FindMatch = FC<Props> {
    var users by useState<List<User>>(listOf())
    var isLoading by useState(true)
    val currentUser = useContext(UserContext)?.first
    val navigate = useNavigate()

    if (currentUser == null) {
        // Redirect to the profile is there isn't an active user
        navigate(UIRoute.Profile.path)
        return@FC
    }

    useEffectOnce {
        mainScope.launch {
            users = getAllUsers().filter { it.id != currentUser.id }
            isLoading = false
        }
    }

    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
            height = 100.pct
            width = 100.pct
        }

        if (isLoading) {
            p {
                +"Loading..."
            }
            return@div
        }

        if (users.isEmpty()) {
            p {
                +"No other users found"
            }
            return@div
        }

        users.forEach { user ->
            UserCard {
                this.user = user
                this.onSelect = { selectedUser ->
                    mainScope.launch {
                        val game = findMatch(selectedUser, currentUser)

                        navigate(UIRoute.Play.path + "/${game.id}")
                    }
                }
            }
        }
    }
}

external interface UserCardProps : Props {
    var user: User
    var onSelect: (User) -> Unit
}

val UserCard = FC<UserCardProps> { props ->
    Card {
        onClick = {
            props.onSelect(props.user)
        }

        title = "Start Game"
        subtitle = "with: ${props.user.username}"
    }
}

suspend fun findMatch(selectedUser: User, currentUser: User): Game {
    val response = window.fetch(
        input = ClientConfiguration.apiUrl + "/games/start",
        init = RequestInit(
            method = "POST",
            headers = Headers().apply {
                set("Content-Type", "application/json")
            },
            body = CommonSerializerModule.json.encodeToString(
                StartGameRequestBody(
                    playerOne = selectedUser,
                    playerTwo = currentUser
                )
            )
        ),
    )
        .await()
        .text()
        .await()

    return CommonSerializerModule.json.decodeFromString(response)
}

suspend fun getAllUsers(): List<User> {
    val response = window.fetch(
        input = ClientConfiguration.apiUrl + "/users",
        init = RequestInit(
            method = "GET",
            headers = Headers().apply {
                set("Content-Type", "application/json")
            },
        ),
    )
        .await()
        .text()
        .await()

    return CommonSerializerModule.json.decodeFromString(response)
}
