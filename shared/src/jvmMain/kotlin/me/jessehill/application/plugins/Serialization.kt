package me.jessehill.application.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import me.jessehill.serializers.CommonSerializerModule

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            json = CommonSerializerModule.json
        )
    }
}
