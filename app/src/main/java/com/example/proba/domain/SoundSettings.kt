package com.example.proba.domain

import android.content.Context
import android.content.SharedPreferences

class SoundSettings(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "stopwatch_sound_settings", 
        Context.MODE_PRIVATE
    )
    
    var isSoundEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOUND_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_SOUND_ENABLED, value).apply()
    
    var isVibrationEnabled: Boolean
        get() = prefs.getBoolean(KEY_VIBRATION_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_VIBRATION_ENABLED, value).apply()
    
    companion object {
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_VIBRATION_ENABLED = "vibration_enabled"
    }
}
