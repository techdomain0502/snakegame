package com.srin.snakegame

import android.util.Log

class Logger {
    companion object{
        fun debug(TAG:String, message:String){
            Log.d(TAG,message)
        }

    }
}