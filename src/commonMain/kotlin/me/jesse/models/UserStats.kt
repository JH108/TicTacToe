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
) {
    val totalWins = (totalXWins ?: 0) + (totalOWins ?: 0)
    val winPercent = if (totalGames > 0) totalWins.toDouble() / totalGames.toDouble() else 0.0
    val winPercentString = "${(winPercent * 100).toInt()}%"
}
