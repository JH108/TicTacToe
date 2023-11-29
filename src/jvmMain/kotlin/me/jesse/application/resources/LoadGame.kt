package me.jesse.application.resources

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource(path = "/games/load")
class LoadGame(
    val gameId: String,
)