package com.grudus.minutnikpoprzezwieki.settings

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.grudus.minutnikpoprzezwieki.R
import com.grudus.minutnikpoprzezwieki.utils.DEFAULT_TIMER_MINUTES
import com.grudus.minutnikpoprzezwieki.utils.TIME_PREFERENCE

class SettingsActivity : AppCompatActivity() {

    private val saveButton by lazy { findViewById<Button>(R.id.settings_save_button) }
    private val timeView by lazy { findViewById<TextView>(R.id.settings_timer_time) }

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)

        initViews()

        saveButton.setOnClickListener {
            saveTime()
            Toast.makeText(this, getString(R.string.settings_toast), Toast.LENGTH_SHORT).show()
        }
    }

    private fun initViews() {
        val time = preferences.getLong(TIME_PREFERENCE, DEFAULT_TIMER_MINUTES)
        timeView.text = time.toString()
    }

    private fun saveTime() {
        val time = timeView.text.toString().toLong()

        preferences.edit()
                .putLong(TIME_PREFERENCE, time)
                .apply()
    }
}
