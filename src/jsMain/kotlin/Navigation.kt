import csstype.*
import emotion.react.css
import features.leaderboard.LeaderBoard
import features.tictactoe.FindMatch
import features.tictactoe.TicTacToeGameBoard
import features.user.Profile
import me.jesse.tictactoe.UIRoute
import react.VFC
import react.create
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.router.Route
import react.router.RouteProps
import react.router.Routes
import react.router.dom.BrowserRouter
import react.router.dom.Link
import react.router.useLocation

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

            to = UIRoute.Home.path

            +UIRoute.Home.title
        }

        Link {
            css {
                margin = 10.px
                padding = 10.px
                color = Color("#000000")
            }

            to = UIRoute.Leaderboard.path

            +UIRoute.Leaderboard.title
        }

        Link {
            css {
                margin = 10.px
                padding = 10.px
                color = Color("#000000")
            }

            to = UIRoute.Profile.path

            +UIRoute.Profile.title
        }

        Link {
            css {
                margin = 10.px
                padding = 10.px
                color = Color("#000000")
            }

            to = UIRoute.FindMatch.path

            +UIRoute.FindMatch.title
        }
    }
}

val Header = VFC {
    val location = useLocation()
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

            to = UIRoute.Home.path

            h2 {
                val pathTitle = when (location.pathname) {
                    UIRoute.Home.path -> "Home"
                    UIRoute.Leaderboard.path -> "Leaderboard"
                    UIRoute.Profile.path -> "Profile"
                    UIRoute.Play.path -> "Play"
                    UIRoute.FindMatch.path -> "Find Match"
                    else -> ""
                }

                +"TicTacToe - $pathTitle"
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
                    path = UIRoute.Home.path
                    element = Home.create()
                }
                Route {
                    path = UIRoute.Leaderboard.path
                    element = LeaderBoard.create()
                }
                Route {
                    path = UIRoute.Profile.path
                    element = Profile.create()
                }
                Route {
                    path = "${UIRoute.Play.path}/:gameId"
                    element = TicTacToeGameBoard.create()
                }
                Route {
                    path = UIRoute.FindMatch.path
                    element = FindMatch.create()
                }
            }
        }
    }
}
