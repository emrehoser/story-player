package com.example.storyplayer.listener

import android.annotation.SuppressLint
import android.app.Activity
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

open class CustomTouchListener(context: Activity) : View.OnTouchListener {

    private val gestureDetector: GestureDetector
    private var touchDownTime = 0L

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        onTouchView(view, event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchDownTime = now()
            }
            MotionEvent.ACTION_UP -> {
                if (isAClick()){
                    onClick(view)
                }
            }

        }
        return gestureDetector.onTouchEvent(event)
    }

    private fun isAClick(): Boolean {
        val isTouchDuration = now() - touchDownTime < CLICK_TIME_THRESHOLD
        return isTouchDuration
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onLongPress(e: MotionEvent?) {
            onLongClick()
        }
    }

    private fun now() = System.currentTimeMillis()

    open fun onClick(view: View) {}

    open fun onLongClick() {}

    open fun onTouchView(view: View, event: MotionEvent): Boolean {
        return false
    }

    companion object {
        private const val CLICK_TIME_THRESHOLD = 200L

    }
}