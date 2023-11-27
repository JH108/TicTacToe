package me.jesse.models

enum class GameStatus {
    IN_PROGRESS,
    X_WON,
    O_WON,
    DRAW,
}

fun GameStatus.valueOf(value: String): GameStatus {
    return when (value) {
        "IN_PROGRESS" -> GameStatus.IN_PROGRESS
        "X_WON" -> GameStatus.X_WON
        "O_WON" -> GameStatus.O_WON
        "DRAW" -> GameStatus.DRAW
        else -> throw IllegalArgumentException("$value is not a valid GameStatus")
    }
}