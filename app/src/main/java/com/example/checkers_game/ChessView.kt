package com.example.checkers_game

import  android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs)  {
    private val scaleFactor = 1.0f //изменить для изменения размера клетки
    private var originX: Float = 20f
    private var originY: Float = 200f
    private var cellSide: Float = 130f
    private val paint = Paint()

    private var movingPieceBitmap: Bitmap? = null
    private var movingPiece: CheckersPiece? = null
    private val imgResIds = setOf(
            R.drawable.white_ordinary,
            R.drawable.black_ordinary,
            R.drawable.white_king,
            R.drawable.black_king
    )
    private  val bitmaps = mutableMapOf<Int, Bitmap>()
    private var fromCol: Int = -1
    private var fromRow: Int = -1

    private var movingPieceX: Float = -1f
    private var movingPieceY: Float = -1f

    var CheckersDelegate: CheckersDelegate? = null
    init {
        loadBitmaps()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val smaller = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(smaller, smaller)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        val chessBoardSide = min(width, height) * scaleFactor
        cellSide = chessBoardSide  / 8f
        originX = (width - chessBoardSide) / 2f
        originY= (height - chessBoardSide) / 2f

        drawableChessBoard(canvas)
        drawPieces(canvas)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        when (event.action){
            MotionEvent.ACTION_DOWN -> {
                fromCol = ((event.x - originX) / cellSide).toInt()
                fromRow = 7 - ((event.y - originY) / cellSide).toInt()

                CheckersDelegate?.pieceAt(fromCol, fromRow)?.let {
                    movingPiece = it
                    movingPieceBitmap = bitmaps[it.resID]
                }
            }

            MotionEvent.ACTION_MOVE -> {
                movingPieceX = event.x
                movingPieceY = event.y
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                val col = ((event.x - originX) / cellSide).toInt()
                val row = 7 - ((event.y - originY) / cellSide).toInt()
                if (fromCol != col || fromRow != row) {
                    CheckersDelegate?.movePiece(fromCol, fromRow, col, row)
                }

                movingPiece = null
                movingPieceBitmap = null
                invalidate()
            }
        }
        return true
    }


    private fun drawPieces(canvas: Canvas){

        for (row in 0..7){
            for (col in 0..7){
                CheckersDelegate?.pieceAt(col, row)?.let {
                    if (it != movingPiece){
                        drawPiecesAt(canvas, col, row, it.resID) }
                    }

            }
        }

        movingPieceBitmap?.let {
            canvas.drawBitmap(it, null, RectF(movingPieceX - cellSide / 2,
                movingPieceY - cellSide / 2,movingPieceX + cellSide/2,
                movingPieceY + cellSide/2), paint)
        }
    }

    private fun drawPiecesAt(canvas: Canvas, col: Int, row: Int, resId: Int){
        val bitmap = bitmaps[resId]!!
        canvas.drawBitmap(bitmap, null, RectF(originX + col * cellSide,
            originY + (7 - row) * cellSide,originX + (col + 1) * cellSide,
            originY + ((7 - row) + 1) * cellSide), paint)
    }

    private fun  loadBitmaps(){
        imgResIds.forEach {
            bitmaps[it] = BitmapFactory.decodeResource(resources, it)
        }
    }

    //Рисование доски
    private fun drawableChessBoard(canvas: Canvas){
        for (row in 0..7){
            for (col in 0..7){
               drawSquareAt(canvas, col, row, (col + row) % 2 == 1)
            }
        }
    }
    //Рисование квадратов
    private fun drawSquareAt(canvas: Canvas, col: Int, row: Int, isDark:Boolean){
        paint.color = if  (isDark) Color.DKGRAY else Color.LTGRAY
        canvas.drawRect(originX + col * cellSide, originY + row * cellSide,originX + (col + 1) * cellSide, originY + (row + 1) * cellSide, paint)


    }

}