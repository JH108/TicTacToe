package me.jesse.application.resources

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource(path = "/games/load/{gameId}")
class LoadGame(
    val gameId: String,
)