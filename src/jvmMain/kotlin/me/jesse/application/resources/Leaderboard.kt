package me.jesse.application.resources

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource(path = "/leaderboard")
class Leaderboard