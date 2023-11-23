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

fun HTML.index() {
    head {
        title("TicTacToe")
    }
    body {
        main {
            id = "root"
        }
        script(src = "/static/TicTacToe.js") {}
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        startApplication()
    }.start(wait = true)
}

private fun Application.startApplication() {
    apiRoutes()
    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }
        static("/static") {
            resources()
        }
    }
}