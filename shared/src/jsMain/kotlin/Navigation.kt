import csstype.*
import emotion.react.css
import features.leaderboard.LeaderBoard
import features.tictactoe.FindMatch
import features.tictactoe.TicTacToeGameBoard
import features.user.Profile
import me.jessehill.tictactoe.UIRoute
import react.VFC
import react.create
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.router.Route
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
                val pathTitle = when  {
                    location.pathname.startsWith("/${UIRoute.Home.path}") -> UIRoute.Home.title
                    location.pathname.startsWith("/${UIRoute.Leaderboard.path}") -> UIRoute.Leaderboard.title
                    location.pathname.startsWith("/${UIRoute.Profile.path}") -> UIRoute.Profile.title
                    location.pathname.startsWith("/${UIRoute.Play.path}") -> "Play"
                    location.pathname.startsWith("/${UIRoute.FindMatch.path}") -> UIRoute.FindMatch.title
                    else -> ""
                }

                val title = buildString {
                    append("TicTacToe")
                    if (pathTitle.isNotEmpty()) {
                        append(" - $pathTitle")
                    }
                }

                +title
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
