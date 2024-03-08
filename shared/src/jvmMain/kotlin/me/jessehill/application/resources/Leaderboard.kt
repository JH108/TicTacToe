package me.jessehill.application.resources

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource(path = "/leaderboard")
class Leaderboard