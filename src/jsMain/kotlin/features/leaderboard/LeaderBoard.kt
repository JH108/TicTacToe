package features.leaderboard

import ClientConfiguration
import csstype.*
import emotion.react.css
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import me.jesse.models.User
import me.jesse.models.UserStats
import me.jesse.serializers.CommonSerializerModule
import react.*
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h4
import react.dom.html.ReactHTML.p

val mainScope = MainScope()

val LeaderBoard = FC<Props> {
    var topFivePlayers by useState<List<Pair<User, UserStats>>>(listOf())

    useEffectOnce {
        mainScope.launch {
            topFivePlayers = fetchTopFivePlayers()
        }
    }

    div {
        css {
            display = Display.flex
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
            height = 100.pct
            width = 100.pct
        }

        if (topFivePlayers.isEmpty()) {
            button {
                css {
                    margin = 10.px
                    padding = 10.px
                    backgroundColor = Color("#f5a5a5")
                }

                +"Refresh"
            }
            p {
                +"Loading..."
            }
            return@div
        }

        topFivePlayers.forEach { (user, stats) ->
            UserStatCard {
                this.user = user
                this.stats = stats
            }
        }
    }
}

external interface UserStatProps : Props {
    var user: User
    var stats: UserStats
}

val UserStatCard = FC<UserStatProps> { props ->
    div {
        css {
            margin = 10.px
            padding = 10.px
            display = Display.flex
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
            backgroundColor = Color("#f5f5f5")
            borderRadius = 10.px
            width = 300.px
            height = 300.px
        }
        h4 {
            +"${props.user.username} Statistics | ${props.stats.winPercent}%"
        }
        p {
            +"Wins: ${props.stats.totalWins}"
        }
        p {
            +"Draws: ${props.stats.totalDraws}"
        }
        p {
            +"Wins as X: ${props.stats.totalXWins}"
        }
        p {
            +"Wins as O: ${props.stats.totalOWins}"
        }
    }
}

suspend fun fetchTopFivePlayers(): List<Pair<User, UserStats>> {
    val response = window
        .fetch(ClientConfiguration.apiUrl + "/leaderboard")
        .await()
        .text()
        .await()

    return CommonSerializerModule.json.decodeFromString(response)
}
