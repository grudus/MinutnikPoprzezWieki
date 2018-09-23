package com.grudus.minutnikpoprzezwieki.timer

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.TextView
import com.grudus.minutnikpoprzezwieki.R
import com.grudus.minutnikpoprzezwieki.circularprogress.CircularProgressBar
import com.grudus.minutnikpoprzezwieki.dto.SoundSettings
import com.grudus.minutnikpoprzezwieki.dto.TimeSettings
import com.grudus.minutnikpoprzezwieki.dto.TimeState
import com.grudus.minutnikpoprzezwieki.settings.SettingsActivity
import com.grudus.minutnikpoprzezwieki.utils.DEFAULT_TIMER_MINUTES
import com.grudus.minutnikpoprzezwieki.utils.TIME_PREFERENCE
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class TimerActivity : AppCompatActivity() {
    private val timeView by lazy { findViewById<TextView>(R.id.timerTime) }
    private val progressCircle by lazy { findViewById<CircularProgressBar>(R.id.timerProgressBar) }
    private val startStopButton by lazy { findViewById<FloatingActionButton>(R.id.timerFloatingButton) }
    private val endTimeTextAnimation by lazy { loadAnimation(this, R.anim.end_time_text_animation) }

    private var initialTime by Delegates.notNull<Int>()

    private val firstAlarmTime: Int
        get() = initialTime / 2

    private val secondAlarmTime: Int
        get() = initialTime / 10

    private var timerController by Delegates.notNull<TimerController>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
    }

    override fun onStart() {
        super.onStart()
        initTime()

        timerController = TimerController(
                TimeSettings(initialTime, firstAlarmTime, secondAlarmTime),
                SoundSettings.fromResources(this,
                        firstAlarm = R.raw.gong, secondAlarm = R.raw.bell, endAlarm = R.raw.end_alarm)
        )


        timeView.text = timerController.initialTime()
        timerController.onTimeEnd {
            onTimeEnd()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.timer_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.timer_bar_menu_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        timerController.stopCounter()
        super.onStop()
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
    fun restartTime(view: View) {
        startStopButton.isEnabled = true
        startStopButton.alpha = 1F
        timeView.clearAnimation()
        timerController.restartTime { state ->
            updateViews(state)
        }
    }


    private fun initTime() {
        val time = PreferenceManager.getDefaultSharedPreferences(this)
                .getLong(TIME_PREFERENCE, DEFAULT_TIMER_MINUTES)

        initialTime = TimeUnit.MINUTES.toSeconds(time).toInt()
    }

    private fun onTimeEnd() {
        runOnUiThread {
            startStopButton.isEnabled = false
            startStopButton.alpha = .4F
            endTimeTextAnimation.repeatCount = Animation.INFINITE
            timeView.startAnimation(endTimeTextAnimation)
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
