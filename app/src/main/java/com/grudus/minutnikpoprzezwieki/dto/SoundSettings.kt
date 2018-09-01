package com.grudus.minutnikpoprzezwieki.dto

import android.content.Context
import android.media.MediaPlayer
import android.support.annotation.RawRes

data class SoundSettings(val firstAlarm: MediaPlayer, val secondAlarm: MediaPlayer) {

    companion object {
        fun fromResources(context: Context, @RawRes firstAlarm: Int, @RawRes secondAlarm: Int): SoundSettings =
                SoundSettings(MediaPlayer.create(context, firstAlarm), MediaPlayer.create(context, secondAlarm))
    }
}