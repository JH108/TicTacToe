package features.leaderboard

import components.Card
import csstype.*
import emotion.react.css
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import mainScope
import me.jessehill.models.User
import me.jessehill.models.UserStats
import me.jessehill.network.ClientConfiguration
import me.jessehill.serializers.CommonSerializerModule
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p
import react.useEffectOnce
import react.useState

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
    Card {
        title = props.user.username
        subtitle = "Win Percent: ${props.stats.winPercentString}"

        div {
            css {
                display = Display.flex
                flexDirection = FlexDirection.column
                justifyContent = JustifyContent.center
                alignItems = AlignItems.center
            }

            p {
                +"Total Games: ${props.stats.totalGames}"
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
}

suspend fun fetchTopFivePlayers(): List<Pair<User, UserStats>> {
    val response = window
        .fetch(ClientConfiguration.apiUrl + "/leaderboard")
        .await()
        .text()
        .await()

    return CommonSerializerModule.json.decodeFromString(response)
}
