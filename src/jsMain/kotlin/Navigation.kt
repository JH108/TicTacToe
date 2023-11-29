import csstype.*
import emotion.react.css
import features.leaderboard.LeaderBoard
import features.tictactoe.TicTacToe
import features.user.Profile
import react.VFC
import react.create
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.router.Route
import react.router.Routes
import react.router.dom.BrowserRouter
import react.router.dom.Link
import react.router.useLocation

val Navigation = VFC {
    BrowserRouter {
        div {
            // header
            div {
                css {
                    display = Display.flex
                    flexDirection = FlexDirection.row
                    justifyContent = JustifyContent.spaceBetween
                    alignItems = AlignItems.center
                    width = 100.pct
                    padding = 25.px
                    // light gray
                    backgroundColor = Color("#f5f5f5")
                }

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
                        }

                        to = "/"

                        +"Home"
                    }

                    Link {
                        css {
                            margin = 10.px
                            padding = 10.px
                        }

                        to = "/leaderboard"

                        +"Leaderboard"
                    }

                    Link {
                        css {
                            margin = 10.px
                            padding = 10.px
                        }

                        to = "/profile"

                        +"Profile"
                    }

                    Link {
                        css {
                            margin = 10.px
                            padding = 10.px
                        }

                        to = "/play"

                        +"Find Game"
                    }
                }

                // screen title
                h2 {
                    +"TicTacToe"
                }
            }
            Routes {
                Route {
                    key = "Home"
                    path = "/"
                    element = ClientApplication.create()
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
