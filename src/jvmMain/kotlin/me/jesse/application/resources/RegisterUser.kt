package me.jesse.application.resources

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource(path = "/register")
class RegisterUser(
    val username: String,
    val firstName: String,
    val lastName: String
)