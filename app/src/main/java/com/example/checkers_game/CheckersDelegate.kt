package com.example.checkers_game

interface CheckersDelegate {
    fun pieceAt(col: Int, row: Int) : CheckersPiece?
}