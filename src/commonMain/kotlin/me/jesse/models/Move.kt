package me.jesse.models

import com.benasher44.uuid.Uuid
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import me.jesse.tictactoe.MoveSymbol

@Serializable
data class Move(
    val squareIndex: Int,
    @Contextual
    val playerId: Uuid,
    val moveSymbol: MoveSymbol
)
