package me.jesse.application.api

import io.ktor.server.application.*
import io.ktor.server.routing.*

/**
 * Route for registering a new player.
 * Route for starting a new game.
 * Route for saving a game.
 * Route for loading a game.
 * Route for creating a leaderboard.
 */
fun Application.apiRoutes() {
    routing {
        route("/api") {
            post("/register") {

            }
            post("/game") {

            }
            post("/save") {

            }
            post("/load") {

            }
            post("/leaderboard") {

            }
        }
    }
}
