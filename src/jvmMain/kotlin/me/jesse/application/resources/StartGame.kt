package me.jesse.application.resources

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import me.jesse.models.User

@Serializable
@Resource(path = "/games/start")
class StartGame(
    val playerOne: User,
    val playerTwo: User
)