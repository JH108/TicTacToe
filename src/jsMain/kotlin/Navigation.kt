import csstype.*
import emotion.react.css
import features.leaderboard.LeaderBoard
import features.tictactoe.TicTacToe
import features.user.Profile
import react.VFC
import react.create
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.router.Route
import react.router.Routes
import react.router.dom.BrowserRouter
import react.router.dom.Link

val HeaderLinks = VFC {
    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.row
            justifyContent = JustifyContent.spaceBetween
            alignItems = AlignItems.center
        }

        Link {
            css {
                margin = 10.px
                padding = 10.px
                color = Color("#000000")
            }

            to = "/"

            +"Home"
        }

        Link {
            css {
                margin = 10.px
                padding = 10.px
                color = Color("#000000")
            }

            to = "/leaderboard"

            +"Leaderboard"
        }

        Link {
            css {
                margin = 10.px
                padding = 10.px
                color = Color("#000000")
            }

            to = "/profile"

            +"Profile"
        }

        Link {
            css {
                margin = 10.px
                padding = 10.px
                color = Color("#000000")
            }

            to = "/play"

            +"Find Game"
        }
    }
}

val Header = VFC {
    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.row
            justifyContent = JustifyContent.spaceBetween
            alignItems = AlignItems.center
            width = 100.pct
            padding = 8.px
            backgroundColor = Color("#f5f5f5")
        }

        Link {
            css {
                margin = 10.px
                padding = 10.px
                color = Color("#000000")
            }

            to = "/"

            h2 {
                +"TicTacToe"
            }
        }

        HeaderLinks()
    }
}

val Navigation = VFC {
    BrowserRouter {
        div {
            Header()

            Routes {
                Route {
                    key = "Home"
                    path = "/"
                    element = Home.create()
                }
                Route {
                    key = "Leaderboard"
                    path = "/leaderboard"
                    element = LeaderBoard.create()
                }
                Route {
                    key = "Profile"
                    path = "/profile"
                    element = Profile.create()
                }
                Route {
                    key = "Play"
                    path = "/play"
                    element = TicTacToe.create()
                }
            }
        }
    }
}
