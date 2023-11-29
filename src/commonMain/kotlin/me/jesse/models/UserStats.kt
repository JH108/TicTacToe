package me.jesse.models

import com.benasher44.uuid.Uuid
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class UserStats(
    @Contextual
    val id: Uuid,
    val totalGames: Long,
    val totalXWins: Long?,
    val totalOWins: Long?,
    val totalDraws: Long?
)