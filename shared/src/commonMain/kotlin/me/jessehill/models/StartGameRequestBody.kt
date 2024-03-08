package me.jessehill.models

import kotlinx.serialization.Serializable

@Serializable
data class StartGameRequestBody(
    val playerOne: User,
    val playerTwo: User
)
