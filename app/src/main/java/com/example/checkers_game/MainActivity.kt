package com.example.checkers_game


import android.os.Build
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
    private lateinit var resetButtons: Button
    private lateinit var listenButtons: Button
    private lateinit var connectButtons: Button

    private var printWriter: PrintWriter? = null
    private val socketPort: Int = 50000 //для телефона
    private val socketGuestPort: Int = 50001 //для эмулятора
    private val socketHost = "127.0.0.1"

    private val isEmulator = Build.FINGERPRINT.contains("generic")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ChessView = findViewById<ChessView>(R.id.checkers_view)
        resetButtons = findViewById<Button>(R.id.reset_button)
        listenButtons = findViewById<Button>(R.id.listen_buttons)
        connectButtons = findViewById<Button>(R.id.connect_buttons)

        ChessView.CheckersDelegate = this

        //Кнопка сброса

        resetButtons.setOnClickListener {
            ChessGame.reset()
            ChessView.invalidate()
        }

        listenButtons.setOnClickListener {
            listenButtons.isEnabled = false
            val port = if (isEmulator) socketGuestPort else socketPort
            Log.d(TAG, "socket server listening on $port")
            Executors.newSingleThreadExecutor().execute {
                val serverSocket = ServerSocket(port)
                val socket = serverSocket.accept()
                receiveMove(socket)
            }

        }

        connectButtons.setOnClickListener {
            Log.d(TAG, "socket client connecting ...")
            Executors.newSingleThreadExecutor().execute {
                val socket = Socket(socketHost, socketPort)
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