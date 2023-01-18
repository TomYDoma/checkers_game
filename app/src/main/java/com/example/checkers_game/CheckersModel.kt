package com.example.checkers_game

class CheckersModel {
    var piecesBox = mutableSetOf<CheckersPiece>()

    init {
        piecesBox.add(CheckersPiece(0,0, CheckersPlayer.WHITE, CheckersRank.ORDINARY))
    }
    override fun toString(): String {
        var desc = " \n"
        for (row in 0..7){
            val r = 7 - row
            desc +="$r"
            for (col in 0..7) {
                desc += " ."
            }
            desc += "\n"
        }
        desc += " 0 1 2 3 4 5 6 7"

        return desc
    }
}