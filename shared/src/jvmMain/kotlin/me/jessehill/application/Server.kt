package me.jessehill.application

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import me.jessehill.Greeting
import me.jessehill.application.api.apiRoutes
import me.jessehill.application.clientroutes.clientRoutes
import me.jessehill.application.plugins.configureMonitoring
import me.jessehill.application.plugins.configureResources
import me.jessehill.application.plugins.configureSerialization
import me.jessehill.database.configureDatabases
import me.jessehill.tictactoe.UIRoute

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
        script(src = "/static/shared.js") {}
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
    configureResources()
    apiRoutes()
    clientRoutes()

    routing {
        get("/platform") {
            val platform = Greeting()

            call.respondText(platform.greet(), ContentType.Text.Plain)
        }
    }
}