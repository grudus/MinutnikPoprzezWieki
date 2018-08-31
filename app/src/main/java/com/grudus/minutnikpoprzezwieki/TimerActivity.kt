package com.grudus.minutnikpoprzezwieki

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.TextView
import java.util.*
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS

class TimerActivity : AppCompatActivity() {
    private val tag = "### ${javaClass.simpleName}"

    private val timeView by lazy { findViewById<TextView>(R.id.timerTime) }
    private val progressCircle by lazy { findViewById<CircularProgressBar>(R.id.timerProgressBar) }
    private val startStopButton by lazy { findViewById<FloatingActionButton>(R.id.timerFloatingButton) }

    private val timeFormat = "mm:ss"

    private val maxTime = MINUTES.toSeconds(5)

    private lateinit var timer: Timer

    private var started = false
    private var elapsedTime = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
    }

    fun stopStartClicked(view: View) {
        if (started) {
            timer.cancel()
            startStopButton.backgroundTintList = resources.getColorStateList(R.color.greenColor)
            startStopButton.setImageResource(R.drawable.start_timer)
        } else {
            startStopButton.backgroundTintList = resources.getColorStateList(R.color.redColor)
            startStopButton.setImageResource(R.drawable.stop_timer)
            timer = Timer()
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        elapsedTime++
                        timeView.text = DateFormat.format(timeFormat, SECONDS.toMillis(maxTime - elapsedTime))
                        progressCircle.progress = (100F * (maxTime - elapsedTime)) / maxTime
                    }
                }

            }, SECONDS.toMillis(1), SECONDS.toMillis(1))
        }

        started = !started
    }
}
