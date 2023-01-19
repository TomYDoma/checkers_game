package com.example.checkers_game

interface CheckersDelegate {
    fun pieceAt(col: Int, row: Int) : CheckersPiece?
    fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int)
}