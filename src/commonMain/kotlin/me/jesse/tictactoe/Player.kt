package me.jesse.tictactoe

enum class Player {
    X,
    O,
}

fun Player.next(): Player {
    return when (this) {
        Player.X -> Player.O
        Player.O -> Player.X
    }
}

fun Player.toSquareValue(): SquareValue {
    return when (this) {
        Player.X -> SquareValue.X
        Player.O -> SquareValue.O
    }
}
