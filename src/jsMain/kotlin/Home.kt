import csstype.*
import emotion.react.css
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import react.*
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.router.useNavigate

external interface ClientApplicationProps : Props {
    var configuration: ClientConfiguration
}

val mainScope = MainScope()

val Home = FC<ClientApplicationProps> { props ->
    val navigate = useNavigate()
    // load the initial data
    val user = useContext(UserContext)
    var initialDataHasLoaded by useState(false)

    useEffectOnce {
        mainScope.launch {
            prefillDatabase()

            initialDataHasLoaded = true
        }
    }

    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            height = 100.vh
            width = 100.pct
        }

        if (!initialDataHasLoaded) {
            div {
                css {
                    display = Display.flex
                    flexDirection = FlexDirection.row
                    justifyContent = JustifyContent.center
                    alignItems = AlignItems.center
                    height = 100.vh
                }

                h2 {
                    +"Loading the Initial Data..."
                }
            }
        } else {
            div {
                css {
                    display = Display.flex
                    flexDirection = FlexDirection.row
                    justifyContent = JustifyContent.center
                    alignItems = AlignItems.center
                    height = 100.vh
                }

                if (user != null) {
                    button {
                        css {
                            margin = 10.px
                            padding = 10.px
                            backgroundColor = Color("#a5d8f5")
                        }

                        onClick = {
                            navigate("/play")
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
                            navigate("/profile")
                        }

                        +"Sign In / Create Account"
                    }
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
