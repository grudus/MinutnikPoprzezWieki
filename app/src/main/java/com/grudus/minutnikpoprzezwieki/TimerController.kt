package com.grudus.minutnikpoprzezwieki

import android.text.format.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit.SECONDS

class TimerController(private val initialTime: Int) {

    var started = false
        private set

    private var timer: Timer? = null
    private var elapsedTime = 0

    fun initialTime(): String = format(initialTime)

    fun startCounter(action: (TimeState) -> Unit) {
        started = true
        timer = Timer()
        timer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                elapsedTime++
                val remainingTime = initialTime - elapsedTime
                val progress = (100F * remainingTime) / initialTime
                val timeState = TimeState(format(remainingTime), progress)
                action(timeState)
            }

        }, oneSecond, oneSecond)
    }

    fun stopCounter() {
        started = false
        timer?.cancel()
    }

    fun restartTime(action: (TimeState) -> Unit) {
        elapsedTime = 0
        action(TimeState(initialTime(), 100F))
    }

    private fun format(seconds: Int): String =
            DateFormat.format(TIME_FORMAT, SECONDS.toMillis(seconds.toLong())).toString()
}