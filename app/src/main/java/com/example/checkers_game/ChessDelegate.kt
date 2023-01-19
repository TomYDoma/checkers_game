package com.example.checkers_game

interface ChessDelegate {
    fun pieceAt(square: Square) : CheckersPiece?
    fun movePiece(from: Square, to: Square)
}