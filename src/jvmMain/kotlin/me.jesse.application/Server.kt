package me.jesse.application

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.Netty
import io.ktor.server.routing.*
import kotlinx.html.*
import me.jesse.application.api.apiRoutes
import me.jesse.application.plugins.configureMonitoring
import me.jesse.application.plugins.configureSerialization
import me.jesse.database.configureDatabases
import me.jesse.tictactoe.UIRoute

fun HTML.index(
    route: UIRoute
) {
    head {
        title("TicTacToe | ${route.title}")
    }
    body {
        main {
            id = "root"
        }
        script(src = "/static/TicTacToe.js") {}
        style(type = "text/css") {
            unsafe {
                raw(
                    """
                    html, body {
                        height: 100%;
                        width: 100%;
                        margin: 0;
                        padding: 0;
                    }
                """.trimIndent()
                )
            }
        }
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        startApplication()
    }.start(wait = true)
}

private fun Application.startApplication() {
    configureDatabases()
    configureSerialization()
    configureMonitoring()
    apiRoutes()
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
        static("/static") {
            resources()
        }
    }
}