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
    private val firstAlarmTime = initialTime / 2
    private val secondAlarmTime = initialTime / 10

    private val timerController by lazy {
        TimerController(
                TimeSettings(initialTime, firstAlarmTime, secondAlarmTime),
                SoundSettings.fromResources(this, firstAlarm = R.raw.gong, secondAlarm = R.raw.bell)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        timeView.text = timerController.initialTime()
    }

    @Suppress("UNUSED_PARAMETER")
    fun stopStartStopClicked(view: View) {
        if (timerController.started) {
            timerController.stopCounter()
            toggleStartStopButton(R.color.greenColor, R.drawable.start_timer)
        } else {
            toggleStartStopButton(R.color.redColor, R.drawable.stop_timer)
            timerController.startCounter { state ->
                updateViews(state)
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun restartTime( view: View) {
        timerController.restartTime { state ->
            updateViews(state)
        }
    }


    private fun updateViews(state: TimeState) {
        runOnUiThread {
            timeView.text = state.remainingTime
            progressCircle.progress = state.progress
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
