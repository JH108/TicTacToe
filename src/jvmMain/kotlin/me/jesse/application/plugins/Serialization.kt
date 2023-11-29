package me.jesse.application.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import me.jesse.serializers.CommonSerializerModule

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            json = CommonSerializerModule.json
        )
    }
}
