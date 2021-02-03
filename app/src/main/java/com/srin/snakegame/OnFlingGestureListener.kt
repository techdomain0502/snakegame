package com.srin.snakegame

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs


abstract class onFlingGestureListener(val ctxt: Context) : View.OnTouchListener {
    private val gdt = GestureDetector(ctxt,GestureListener())
    private var view: View? = null
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        view = v
        return gdt.onTouchEvent(event)
    }

    private inner class GestureListener : SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {

            if (e1.x - e2.x > Companion.SWIPE_MIN_DISTANCE && abs(velocityX) > Companion.SWIPE_THRESHOLD_VELOCITY) {
                toLeft(view)

                return true
            } else if (e2.x - e1.x > Companion.SWIPE_MIN_DISTANCE && abs(velocityX) > Companion.SWIPE_THRESHOLD_VELOCITY) {
                toRight(view)
                return true
            }
            if (e1.y - e2.y > Companion.SWIPE_MIN_DISTANCE && abs(velocityY) > Companion.SWIPE_THRESHOLD_VELOCITY) {
                toTop(view)
                return true
            } else if (e2.y - e1.y > Companion.SWIPE_MIN_DISTANCE && abs(velocityY) > Companion.SWIPE_THRESHOLD_VELOCITY) {
                toBottom(view)
                return true
            }
            return false
        }


    }

    abstract fun toLeft(v: View?)
    abstract fun toRight(v: View?)
    abstract fun toTop(v: View?)
    abstract fun toBottom(v: View?)


    companion object {
        private const val SWIPE_MIN_DISTANCE = 60
        private const val SWIPE_THRESHOLD_VELOCITY = 100
    }
}

