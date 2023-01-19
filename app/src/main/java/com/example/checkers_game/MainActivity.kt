package com.example.checkers_game


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.Scanner
import java.util.concurrent.Executors

const val  TAG = "MainActivity"

class MainActivity : AppCompatActivity(), CheckersDelegate {

    private lateinit var ChessView: ChessView
    private var printWriter: PrintWriter? = null
    private val PORT: Int = 50001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ChessView = findViewById<ChessView>(R.id.checkers_view)
        ChessView.CheckersDelegate = this

        //Кнопка сброса

        findViewById<Button>(R.id.reset_button).setOnClickListener {
            ChessGame.reset()
            ChessView.invalidate()
        }

        findViewById<Button>(R.id.listen_buttons).setOnClickListener {
            Log.d(TAG, "socket server listening on port ...")
            Executors.newSingleThreadExecutor().execute {
                val serverSocket = ServerSocket(PORT)
                val socket = serverSocket.accept()
                receiveMove(socket)
            }

        }

        findViewById<Button>(R.id.connect_buttons).setOnClickListener {
            Log.d(TAG, "socket client connecting to addr:port ...")
            Executors.newSingleThreadExecutor().execute {
                val socket = Socket("192.168.0.17", PORT)
                receiveMove(socket)
            }
        }
    }

    private fun receiveMove(socket: Socket){
        val scanner = Scanner(socket.getInputStream())
        printWriter = PrintWriter(socket.getOutputStream(), true)
        while (scanner.hasNextLine()) {
            val move: List<Int> =  scanner.nextLine().split(",").map { it.toInt() }
            runOnUiThread {
                ChessGame.movePiece(move[0], move[1], move[2], move[3])
                ChessView.invalidate()
            }
        }
    }

    override fun pieceAt(col: Int, row: Int): CheckersPiece? {
        return ChessGame.pieceAt(col, row)
    }

    override fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        if (fromCol == toCol && fromRow == toRow){
            return
        }
        ChessGame.movePiece(fromCol, fromRow, toCol, toRow)
        ChessView.invalidate()

        printWriter?.let {
            val moveStr = "$fromCol,$fromRow,$toCol,$toRow"
            Executors.newSingleThreadExecutor().execute {
                it.println(moveStr)
            }
        }
    }
}