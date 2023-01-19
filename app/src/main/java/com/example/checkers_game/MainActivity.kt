package com.example.checkers_game


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

const val  TAG = "MainActivity"

class MainActivity : AppCompatActivity(), CheckersDelegate {

    private var CheckersModel = CheckersModel()
    private lateinit var ChessView: ChessView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ChessView = findViewById<ChessView>(R.id.checkers_view)
        ChessView.CheckersDelegate = this

        //Кнопка сброса

        findViewById<Button>(R.id.reset_button).setOnClickListener {
            CheckersModel.reset()
            ChessView.invalidate()

        }
    }

    override fun pieceAt(col: Int, row: Int): CheckersPiece? {
        return CheckersModel.pieceAt(col, row)
    }

    override fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        CheckersModel.movePiece(fromCol, fromRow, toCol, toRow)
        findViewById<ChessView>(R.id.checkers_view).invalidate()
    }
}