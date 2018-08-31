package com.grudus.minutnikpoprzezwieki

import android.os.Build
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import java.util.concurrent.TimeUnit.MINUTES

class TimerActivity : AppCompatActivity() {
    private val timeView by lazy { findViewById<TextView>(R.id.timerTime) }
    private val progressCircle by lazy { findViewById<CircularProgressBar>(R.id.timerProgressBar) }
    private val startStopButton by lazy { findViewById<FloatingActionButton>(R.id.timerFloatingButton) }
    private val initialTime = MINUTES.toSeconds(5).toInt()

    private val timerController = TimerController(initialTime)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        timeView.text = timerController.initialTime()
    }

    fun stopStartStopClicked(view: View) {
        if (timerController.started) {
            timerController.stopCounter()
            toggleStartStopButton(R.color.greenColor, R.drawable.start_timer)
        } else {
            toggleStartStopButton(R.color.redColor, R.drawable.stop_timer)
            timerController.startCounter { (remainingTime, progress) ->
                runOnUiThread {
                    timeView.text = remainingTime
                    progressCircle.progress = progress
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun toggleStartStopButton(@ColorRes color: Int, @DrawableRes image: Int) {
        startStopButton.backgroundTintList =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    resources.getColorStateList(color, theme)
                } else resources.getColorStateList(color)

        startStopButton.setImageResource(image)
    }
}
