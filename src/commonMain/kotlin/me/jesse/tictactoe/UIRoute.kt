package me.jesse.tictactoe

sealed interface UIRoute {
    val path: String
    val title: String

    data object Home : UIRoute {
        override val path = "/"
        override val title = "Home"
    }
    data object Profile : UIRoute {
        override val path = "/profile"
        override val title = "Profile"
    }
    data object Play : UIRoute {
        override val path = "/play"
        override val title = "Play"
    }
    data object FindMatch : UIRoute {
        override val path = "/find-match"
        override val title = "Find Match"
    }
    data object Leaderboard : UIRoute {
        override val path = "/leaderboard"
        override val title = "Leaderboard"
    }
}
