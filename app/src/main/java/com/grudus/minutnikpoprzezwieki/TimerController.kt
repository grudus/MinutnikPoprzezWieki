package com.grudus.minutnikpoprzezwieki

import android.media.MediaPlayer
import android.text.format.DateFormat
import android.util.Log
import java.util.*
import java.util.concurrent.TimeUnit.SECONDS

class TimerController(
        private val settings: TimeSettings,
        private val soundSettings: SoundSettings) {

    private val tag = "### ${javaClass.simpleName}"

    var started = false
        private set

    private var timer: Timer? = null
    private var elapsedTime = 0

    fun initialTime(): String = format(settings.initialTime)

    fun startCounter(action: (TimeState) -> Unit) {
        started = true
        timer = Timer()
        timer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                elapsedTime++
                val remainingTime = settings.initialTime - elapsedTime
                val progress = (100F * remainingTime) / settings.initialTime
                val timeState = TimeState(format(remainingTime), progress)

                if (remainingTime == settings.firstAlarmTime)
                    playSound(soundSettings.firstAlarm)
                else if (remainingTime == settings.secondAlarmTime)
                    playSound(soundSettings.secondAlarm)

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
        pausePlayer(soundSettings.firstAlarm)
        pausePlayer(soundSettings.secondAlarm)
        action(TimeState(initialTime(), 100F))
    }

    private fun playSound(mediaPlayer: MediaPlayer) {
        Log.i(tag, "Playing sound")
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            Log.i(tag, "Playing sound completed")
            pausePlayer(it)
        }
    }

    private fun pausePlayer(it: MediaPlayer) {
        if (it.isPlaying) {
            it.pause()
            it.seekTo(0)
        }
    }

    private fun format(seconds: Int): String =
            DateFormat.format(TIME_FORMAT, SECONDS.toMillis(seconds.toLong())).toString()
}