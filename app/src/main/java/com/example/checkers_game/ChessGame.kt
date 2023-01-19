package com.example.checkers_game

object ChessGame {
    private var piecesBox = mutableSetOf<CheckersPiece>()

    init {
        reset()
    }

    fun clear(){
        piecesBox.removeAll(piecesBox)
    }

    fun addPiece(piece: CheckersPiece){
        piecesBox.add(piece)

    }

    fun canKnight(from: Square, to: Square): Boolean{
        return false
    }

    fun movePiece(from: Square, to: Square){
        movePiece(from.col, from.row, to.col, to.row)
    }

    private fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int){
        if (fromCol == toCol && fromRow == toRow) return
        val movingPiece = pieceAt(fromCol, fromRow) ?: return

        pieceAt(toCol, toRow)?.let {
            if (it.player == movingPiece.player){
                return
            }
            piecesBox.remove(it)
        }
        piecesBox.remove(movingPiece)
        addPiece(movingPiece.copy(col = toCol, row = toRow))

    }

    fun reset() {
        clear()
        piecesBox.removeAll(piecesBox)
        for (i in 0..7 step 2){
            addPiece(CheckersPiece(0+i,0, Player.WHITE, Chessman.ORDINARY, R.drawable.white_ordinary))
            addPiece(CheckersPiece(1+i,1, Player.WHITE, Chessman.ORDINARY, R.drawable.white_ordinary))
            addPiece(CheckersPiece(0+i,2, Player.WHITE, Chessman.ORDINARY, R.drawable.white_ordinary))
        }
        for (i in 0..7 step 2){
            addPiece(CheckersPiece(1+i,7, Player.BLACK, Chessman.ORDINARY, R.drawable.black_ordinary))
            addPiece(CheckersPiece(0+i,6, Player.BLACK, Chessman.ORDINARY, R.drawable.black_ordinary))
            addPiece(CheckersPiece(1+i,5, Player.BLACK, Chessman.ORDINARY, R.drawable.black_ordinary))
        }


    }


    fun pieceAt(square: Square): CheckersPiece? {
        return pieceAt(square.col, square.row)
    }

    private fun pieceAt(col: Int, row: Int): CheckersPiece? {
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
            desc += boardRow(row)
            desc += "\n"
        }
        desc += "  0 1 2 3 4 5 6 7"

        return desc
    }

    private fun boardRow(row: Int): String {
        var desc =""
        for (col in 0..7) {
            desc += " "
            desc += pieceAt(col, row)?.let {
                val white = it.player == Player.WHITE
                when (it.chessman) {
                    Chessman.ORDINARY -> if (white) "б" else "ч"
                    Chessman.KING -> if (white) "д" else "Д"
                }
            } ?: "."
        }

        return desc
    }
}