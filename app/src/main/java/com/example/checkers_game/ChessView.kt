package com.example.checkers_game

import  android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs)  {
    val cellSide: Float = 130f
    override fun onDraw(canvas: Canvas?) {
        val paint = Paint()
        paint.color = Color.LTGRAY
        paint.style = Paint.Style.STROKE
        canvas?.drawRect(100f,200f,100f + cellSide, 200f + cellSide, paint)



    }

}