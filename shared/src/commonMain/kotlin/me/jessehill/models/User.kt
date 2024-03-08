package me.jessehill.models

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    @Contextual
    val id: Uuid = uuid4(),
    val firstName: String,
    val lastName: String,
)
