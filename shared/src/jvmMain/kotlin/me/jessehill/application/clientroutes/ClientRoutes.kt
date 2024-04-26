package me.jessehill.application.clientroutes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import me.jessehill.application.index
import me.jessehill.tictactoe.UIRoute

fun Application.clientRoutes() {
    routing {
        get(UIRoute.Home.path) {
            call.respondHtml(HttpStatusCode.OK) {
                this.index(
                    route = UIRoute.Home
                )
            }
        }
        get(UIRoute.Play.path) {
            call.respondHtml(HttpStatusCode.OK) {
                this.index(
                    route = UIRoute.Play
                )
            }
        }
        get(UIRoute.FindMatch.path) {
            call.respondHtml(HttpStatusCode.OK) {
                this.index(
                    route = UIRoute.FindMatch
                )
            }
        }
        get(UIRoute.Leaderboard.path) {
            call.respondHtml(HttpStatusCode.OK) {
                this.index(
                    route = UIRoute.Leaderboard
                )
            }
        }
        get(UIRoute.Profile.path) {
            call.respondHtml(HttpStatusCode.OK) {
                this.index(
                    route = UIRoute.Profile
                )
            }
        }

        staticResources("/static", null)
    }
}