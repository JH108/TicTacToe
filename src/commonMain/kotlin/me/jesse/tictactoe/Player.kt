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

fun Player.toSquareValue(): MoveSymbol {
    return when (this) {
        Player.X -> MoveSymbol.X
        Player.O -> MoveSymbol.O
    }
}
