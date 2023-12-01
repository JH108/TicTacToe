import csstype.*
import emotion.react.css
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import me.jesse.tictactoe.UIRoute
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.router.useNavigate
import react.useContext
import react.useState

external interface ClientApplicationProps : Props {
    var configuration: ClientConfiguration
}

val mainScope = MainScope()

val Home = FC<ClientApplicationProps> { props ->
    val navigate = useNavigate()
    val user = useContext(UserContext)
    var isLoading by useState(false)

    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            height = 100.vh
            width = 100.pct
        }
        div {
            css {
                display = Display.flex
                flexDirection = FlexDirection.row
                justifyContent = JustifyContent.center
                alignItems = AlignItems.center
                height = 100.vh
            }

            button {
                css {
                    margin = 10.px
                    padding = 10.px
                    backgroundColor = Color("#a5d8f5")
                }

                onClick = {
                    mainScope.launch {
                        isLoading = true
                        prefillDatabase()
                        isLoading = false
                    }
                }

                if (isLoading) +"Loading..."
                else +"Load Fake Data"
            }

            if (user != null) {
                button {
                    css {
                        margin = 10.px
                        padding = 10.px
                        backgroundColor = Color("#a5d8f5")
                    }

                    onClick = {
                        navigate(UIRoute.Play.path)
                    }

                    +"Start Game"
                }
            } else {
                button {
                    css {
                        margin = 10.px
                        padding = 10.px
                        backgroundColor = Color("#a5d8f5")
                    }

                    onClick = {
                        navigate(UIRoute.Profile.path)
                    }

                    +"Sign In / Create Account"
                }
            }
        }
    }
}

suspend fun prefillDatabase(): String {
    return window
        .fetch(ClientConfiguration.apiUrl + "/load-fake-data")
        .await()
        .text()
        .await()
}
