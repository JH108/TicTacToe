package features.user

import ClientConfiguration
import UserContext
import components.Card
import csstype.*
import emotion.react.css
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import mainScope
import me.jessehill.models.Game
import me.jessehill.models.User
import me.jessehill.serializers.CommonSerializerModule
import me.jessehill.tictactoe.UIRoute
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import react.*
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section
import react.router.useNavigate

external interface ProfileProps : Props

external interface UserProfileProps : Props {
    var userDetails: User
}

val UserProfile = FC<UserProfileProps> { props ->
    var games by useState<List<Game>>(listOf())
    val navigate = useNavigate()

    useEffectOnce {
        mainScope.launch {
            games = fetchGamesForCurrentUser(props.userDetails)
        }
    }

    section {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
            height = 100.pct
            width = 100.pct
        }

        Card {
            title = "User Details"

            div {
                css {
                    display = Display.flex
                    flexDirection = FlexDirection.column
                    justifyContent = JustifyContent.center
                    alignItems = AlignItems.center
                }

                p {
                    +"Username: ${props.userDetails.username}"
                }
                p {
                    +"First Name: ${props.userDetails.firstName}"
                }
                p {
                    +"Last Name: ${props.userDetails.lastName}"
                }
            }
        }

        div {
            css {
                display = Display.flex
                flexDirection = FlexDirection.row
                justifyContent = JustifyContent.center
                alignItems = AlignItems.center
                height = 100.pct
                width = 100.pct
            }

            button {
                css {
                    margin = 10.px
                    padding = 10.px
                    backgroundColor = Color("#a5d8f5")
                }

                onClick = {
                    mainScope.launch {
                        games = fetchGamesForCurrentUser(props.userDetails)
                    }
                }

                +"Fetch Games"
            }

            if (games.isEmpty()) {
                p {
                    +"No active games"
                }
            }

            games.forEach { game ->
                Card {
                    onClick = {
                        navigate(UIRoute.Play.path + "/${game.id}")
                    }

                    title = "Game ID: ${game.id} - ${game.status}"

                    div {
                        css {
                            display = Display.flex
                            flexDirection = FlexDirection.column
                            justifyContent = JustifyContent.center
                            alignItems = AlignItems.center
                        }

                        p {
                            +"Player X: ${game.playerX.username}"
                        }
                        p {
                            +"Player O: ${game.playerO.username}"
                        }
                    }
                }
            }
        }
    }
}

val Profile = FC<ProfileProps> {
    val user = useContext(UserContext)

    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
            height = 100.pct
            width = 100.pct
        }

        user?.first?.let {
            UserProfile {
                userDetails = it
            }
        }

        if (user?.first == null) {
            child(Onboarding.create())
        }
    }
}

suspend fun fetchGamesForCurrentUser(user: User): List<Game> {
    val response = window.fetch(
        input = ClientConfiguration.apiUrl + "/users/${user.id}/games",
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
