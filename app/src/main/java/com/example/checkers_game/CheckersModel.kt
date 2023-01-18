package com.example.checkers_game

class CheckersModel {
    var piecesBox = mutableSetOf<CheckersPiece>()

    init {
        reset()
    }

    private fun reset() {
        piecesBox.removeAll(piecesBox)
        for (i in 0..7 step 2){
            piecesBox.add(CheckersPiece(0+i,0, CheckersPlayer.WHITE, CheckersRank.ORDINARY))
            piecesBox.add(CheckersPiece(1+i,1, CheckersPlayer.WHITE, CheckersRank.ORDINARY))
            piecesBox.add(CheckersPiece(0+i,2, CheckersPlayer.WHITE, CheckersRank.ORDINARY))
        }
        for (i in 0..7 step 2){
            piecesBox.add(CheckersPiece(1+i,7, CheckersPlayer.BLACK, CheckersRank.ORDINARY))
            piecesBox.add(CheckersPiece(0+i,6, CheckersPlayer.BLACK, CheckersRank.ORDINARY))
            piecesBox.add(CheckersPiece(1+i,5, CheckersPlayer.BLACK, CheckersRank.ORDINARY))
        }


    }

    private fun pieceAt(col: Int, row: Int) : CheckersPiece? {
        for(piece in piecesBox){
            if (col == piece.col && row == piece.row) {
                return piece
            }
        }
        return null
    }

    override fun toString(): String {
        var desc = " \n"
        for (row in 7 downTo 0) {
            desc += "$row"
            for (col in 0..7) {
                val piece = pieceAt(col, row)
                if (piece == null) {
                    desc += " ."
                } else {
                    val white = piece.player == CheckersPlayer.WHITE
                    desc += " "
                    desc += when (piece.rank) {
                        CheckersRank.ORDINARY -> {
                            if (white) "ш" else "Ш"
                        }
                        CheckersRank.KING -> {
                            if (white) "д" else "Д"
                        }
                    }
                }
            }
            desc += "\n"
        }
        desc += "  0 1 2 3 4 5 6 7"

        return desc
    }
}