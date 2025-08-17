package com.example.proba.domain

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class SoundManager(context: Context) {
    
    private val soundSettings = SoundSettings(context)
    
    private val soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .build()
    } else {
        @Suppress("DEPRECATION")
        SoundPool(4, android.media.AudioManager.STREAM_MUSIC, 0)
    }
    
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    
    private var startSoundId: Int = 0
    private var lapSoundId: Int = 0
    private var stopSoundId: Int = 0
    
    init {
        loadSounds(context)
    }
    
    private fun loadSounds(context: Context) {
        // Используем системные звуки уведомлений
        startSoundId = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)
        lapSoundId = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)
        stopSoundId = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)
    }
    
    fun playStartSound() {
        if (soundSettings.isSoundEnabled) {
            playSystemNotification()
        }
        if (soundSettings.isVibrationEnabled) {
            vibrateShort()
        }
    }
    
    fun playLapSound() {
        if (soundSettings.isSoundEnabled) {
            playSystemNotification()
        }
        if (soundSettings.isVibrationEnabled) {
            vibrateShort()
        }
    }
    
    fun playStopSound() {
        if (soundSettings.isSoundEnabled) {
            playSystemNotification()
        }
        if (soundSettings.isVibrationEnabled) {
            vibrateLong()
        }
    }
    
    private fun playSystemNotification() {
        // Воспроизводим системный звук уведомления
        if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) > 0) {
            // Используем ToneGenerator для простого звука
            try {
                val toneGen = android.media.ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
                toneGen.startTone(android.media.ToneGenerator.TONE_PROP_BEEP, 100)
                toneGen.release()
            } catch (e: Exception) {
                // Fallback - просто вибрация
                vibrateShort()
            }
        }
    }
    
    private fun vibrateShort() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(50)
        }
    }
    
    private fun vibrateLong() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(200)
        }
    }
    
    fun setSoundEnabled(enabled: Boolean) {
        soundSettings.isSoundEnabled = enabled
    }
    
    fun setVibrationEnabled(enabled: Boolean) {
        soundSettings.isVibrationEnabled = enabled
    }
    
    fun isSoundEnabled(): Boolean = soundSettings.isSoundEnabled
    
    fun isVibrationEnabled(): Boolean = soundSettings.isVibrationEnabled
    
    fun release() {
        soundPool.release()
    }
}
