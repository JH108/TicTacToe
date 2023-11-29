package me.jesse.application.resources

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import me.jesse.models.Game

@Serializable
@Resource(path = "/games/save")
class SaveGame(
    val game: Game
)