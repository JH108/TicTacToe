import csstype.*
import emotion.react.css
import features.leaderboard.LeaderBoard
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import react.*
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.span

external interface ClientApplicationProps : Props {
    var configuration: ClientConfiguration
}

val mainScope = MainScope()

val ClientApplication = FC<ClientApplicationProps> { props ->
    // load the initial data
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

        // header
        div {
            css {
                display = Display.flex
                flexDirection = FlexDirection.row
                justifyContent = JustifyContent.spaceBetween
                alignItems = AlignItems.center
                height = 8.vh
                width = 100.pct
                padding = 10.px
                // light gray
                backgroundColor = Color("#f5f5f5")
            }

            // side menu button
            button {
                css {
                    backgroundColor = Color("#f5a5a5")
                }
                +"Menu"
            }

            // screen title
            h2 {
                +"The Title"
            }

            // user profile icon
            button {
                css {
                    backgroundColor = Color("#f5a5a5")
                }

                +"Profile"
            }
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
            child(
                LeaderBoard.create()
            )
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

