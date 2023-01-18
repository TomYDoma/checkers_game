package com.example.checkers_game


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

const val  TAG = "MainActivity"

/*
    MVC: Model View Controller
 */

class MainActivity : AppCompatActivity(), CheckersDelegate {

    var CheckersModel = CheckersModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       Log.d(TAG, "$CheckersModel")
        val ChessView = findViewById<ChessView>(R.id.checkers_view)
        ChessView.CheckersDelegate = this
    }

    override fun pieceAt(col: Int, row: Int): CheckersPiece? {
        return CheckersModel.pieceAt(col, row)
    }
}