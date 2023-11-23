package me.jesse.models

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4

data class User(
    val username: String,
    val id: Uuid = uuid4(),
    val firstName: String,
    val lastName: String,
)
