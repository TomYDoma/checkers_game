package com.example.checkers_game

import android.util.Log

class CheckersModel {
    var piecesBox = mutableSetOf<CheckersPiece>()

    init {
        reset()
        // TODO
        Log.d(TAG, "${piecesBox.size}")
        movePiece(0, 2,0,6)
        movePiece(0, 6,1,3)

        Log.d(TAG, toString())
        Log.d(TAG, "${piecesBox.size}")
    }

    fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int){
        val movingPiece = pieceAt(fromCol, fromRow) ?: return

        pieceAt(toCol, toRow)?.let {
            if (it.player == movingPiece.player){
                return
            }
            piecesBox.remove(it)
        }
        movingPiece.col = toCol
        movingPiece.row = toRow



    }

    private fun reset() {
        piecesBox.removeAll(piecesBox)
        for (i in 0..7 step 2){
            piecesBox.add(CheckersPiece(0+i,0, CheckersPlayer.WHITE, CheckersRank.ORDINARY, R.drawable.white_ordinary))
            piecesBox.add(CheckersPiece(1+i,1, CheckersPlayer.WHITE, CheckersRank.ORDINARY, R.drawable.white_ordinary))
            piecesBox.add(CheckersPiece(0+i,2, CheckersPlayer.WHITE, CheckersRank.ORDINARY, R.drawable.white_ordinary))
        }
        for (i in 0..7 step 2){
            piecesBox.add(CheckersPiece(1+i,7, CheckersPlayer.BLACK, CheckersRank.ORDINARY, R.drawable.black_ordinary))
            piecesBox.add(CheckersPiece(0+i,6, CheckersPlayer.BLACK, CheckersRank.ORDINARY, R.drawable.black_ordinary))
            piecesBox.add(CheckersPiece(1+i,5, CheckersPlayer.BLACK, CheckersRank.ORDINARY, R.drawable.black_ordinary))
        }


    }

    fun pieceAt(col: Int, row: Int) : CheckersPiece? {
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
                            if (white) "б" else "ч"
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