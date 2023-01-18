package com.example.checkers_game

import  android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.min

class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs)  {
    private final  val scaleFactor = .9f
    private final var originX: Float = 20f
    private final var originY: Float = 200f
    private final var cellSide: Float = 130f //изменить для изменения размера клетки (85 для телефона, 130 для pixel 4a, api 33)
    private final val paint = Paint()
    private final val imgResIds = setOf(
            R.drawable.white_ordinary,
            R.drawable.black_ordinary,
            R.drawable.black_king,
            R.drawable.white_king
    )
    private final val bitmaps = mutableMapOf<Int, Bitmap>()
    var CheckersDelegate: CheckersDelegate? = null
    init {
        loadBitmaps()
    }

    override fun onDraw(canvas: Canvas?) {

        Log.d(TAG, "${canvas?.width}, ${canvas?.height}")
        canvas?.let {
            val chessBoardSide = min(it.width, it.height) * scaleFactor
            cellSide = chessBoardSide  / 8f
            originX = (it.width - chessBoardSide) / 2f
            originY= (it.height - chessBoardSide) / 2f
        }
        drawableChessBoard(canvas)
        drawPieces(canvas)
    }

    private fun drawPieces(canvas: Canvas?){

        for (row in 0..7){
            for (col in 0..7){
                CheckersDelegate?.pieceAt(col, row)?.let { drawPiecesAt(canvas, col, row, it.resID) }
            }
        }


    }

    private fun drawPiecesAt(canvas: Canvas?, col: Int, row: Int, resId: Int){
        val bitmap = bitmaps[resId]!!
        canvas?.drawBitmap(bitmap, null, RectF(originX + col * cellSide,originY + (7 - row) * cellSide,originX + (col + 1) * cellSide,originY + ((7 - row) + 1) * cellSide), paint)
    }

    private fun  loadBitmaps(){
        imgResIds.forEach {
            bitmaps[it] = BitmapFactory.decodeResource(resources, it)
        }
    }

    //Рисование доски
    private fun drawableChessBoard(canvas: Canvas?){
        for (row in 0..7){
            for (col in 0..7){
               drawSquareAt(canvas, col, row, (col + row) % 2 == 1)
            }
        }
    }
    //Рисование квадратов
    private fun drawSquareAt(canvas: Canvas?, col: Int, row: Int, isDark:Boolean){
        paint.color = if  (isDark) Color.DKGRAY else Color.LTGRAY
        canvas?.drawRect(originX + col * cellSide, originY + row * cellSide,originX + (col + 1) * cellSide, originY + (row + 1) * cellSide, paint)


    }

}