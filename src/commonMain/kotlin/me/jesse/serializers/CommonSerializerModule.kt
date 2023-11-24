package me.jesse.serializers

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

object CommonSerializerModule {
    val json = Json {
        serializersModule = SerializersModule {
            contextual(UuidSerializer)
        }
        ignoreUnknownKeys = true
        prettyPrint = true
    }
}