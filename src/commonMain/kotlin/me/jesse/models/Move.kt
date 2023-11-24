package me.jesse.models

import kotlinx.serialization.Serializable
import me.jesse.tictactoe.SquareValue

@Serializable
data class Move(
    val squareIndex: Int,
    val player: User,
    val moveSymbol: SquareValue
)
