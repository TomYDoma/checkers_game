package com.example.checkers_game

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

private const val  TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    var checkersModel = CheckersModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       Log.d(TAG, "$checkersModel")
    }
}