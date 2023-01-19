package com.example.checkers_game


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import java.io.PrintWriter
import java.net.ConnectException
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.util.Scanner
import java.util.concurrent.Executors

const val  TAG = "MainActivity"

class MainActivity : AppCompatActivity(), ChessDelegate {

    private lateinit var ChessView: ChessView
    private lateinit var resetButtons: Button
    private lateinit var listenButtons: Button
    private lateinit var connectButtons: Button

    private var printWriter: PrintWriter? = null
    private val socketPort: Int = 50000 //для телефона
    private val socketGuestPort: Int = 50001 //для эмулятора
    private val socketHost = "127.0.0.1"
    private  var serverSocket: ServerSocket? = null
    private val isEmulator = Build.FINGERPRINT.contains("generic")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ChessView = findViewById<ChessView>(R.id.checkers_view)
        resetButtons = findViewById<Button>(R.id.reset_button)
        listenButtons = findViewById<Button>(R.id.listen_buttons)
        connectButtons = findViewById<Button>(R.id.connect_buttons)

        ChessView.ChessDelegate = this

        //Кнопка сброса

        resetButtons.setOnClickListener {
            ChessGame.reset()
            ChessView.invalidate()
            serverSocket?.close()
            listenButtons.isEnabled = true
        }

        listenButtons.setOnClickListener {
            listenButtons.isEnabled = false
            val port = if (isEmulator) socketGuestPort else socketPort
            Toast.makeText(this, "listening on $port", Toast.LENGTH_SHORT).show()
            Executors.newSingleThreadExecutor().execute {
                ServerSocket(port).let { srvSKT ->
                    serverSocket = srvSKT
                    try {
                        val socket = srvSKT.accept()
                        receiveMove(socket)
                    } catch (e: SocketException){
                        // ignored, socket closed
                    }

                }

            }

        }

        connectButtons.setOnClickListener {
            Log.d(TAG, "socket client connecting ...")
            Executors.newSingleThreadExecutor().execute {
                try{
                    val socket = Socket(socketHost, socketPort)
                    receiveMove(socket)
                } catch (e: ConnectException){
                    runOnUiThread {
                        Toast.makeText(this, "connection failed", Toast.LENGTH_SHORT).show()
                    }

                }

            }
        }
    }

    private fun receiveMove(socket: Socket){
        val scanner = Scanner(socket.getInputStream())
        printWriter = PrintWriter(socket.getOutputStream(), true)
        while (scanner.hasNextLine()) {
            val move: List<Int> =  scanner.nextLine().split(",").map { it.toInt() }
            runOnUiThread {
                ChessGame.movePiece(Square(move[0], move[1]), Square(move[2], move[3]))
                ChessView.invalidate()
            }
        }
    }

    override fun pieceAt(square: Square): CheckersPiece? = ChessGame.pieceAt(square)


    override fun movePiece(from: Square, to: Square) {
        ChessGame.movePiece(from, to)
        ChessView.invalidate()

        printWriter?.let {
            val moveStr = "${from.col},${from.row},${to.col}.,${to.row}"
            Executors.newSingleThreadExecutor().execute {
                it.println(moveStr)
            }
        }
    }
}