package com.example.proba.domain

import android.os.Handler
import android.os.Looper

class StopwatchTimer(
    private val onTick: (Long) -> Unit,
    private val onLapAdded: (Long) -> Unit
) {
    
    private var startTime: Long = 0L
    private var elapsedTime: Long = 0L
    private var isRunning: Boolean = false
    private val lapTimes = mutableListOf<Long>()
    private val handler = Handler(Looper.getMainLooper())
    
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                val currentTime = System.currentTimeMillis()
                val totalElapsed = elapsedTime + (currentTime - startTime)
                onTick(totalElapsed)
                // Планируем следующее обновление
                handler.postDelayed(this, UPDATE_INTERVAL)
            }
        }
    }
    
    fun start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis()
            isRunning = true
            handler.post(updateRunnable)
        }
    }
    
    fun pause() {
        if (isRunning) {
            elapsedTime += System.currentTimeMillis() - startTime
            isRunning = false
            handler.removeCallbacks(updateRunnable)
        }
    }
    
    fun stop() {
        pause()
    }
    
    fun addLap() {
        if (isRunning) {
            val currentLapTime = getElapsedTime()
            lapTimes.add(currentLapTime)
            onLapAdded(currentLapTime)
        }
    }
    
    fun getLapTimes(): List<Long> = lapTimes.toList()
    
    fun reset() {
        isRunning = false
        elapsedTime = 0L
        startTime = 0L
        lapTimes.clear()
        handler.removeCallbacks(updateRunnable)
        onTick(0L)
    }
    
    fun getElapsedTime(): Long {
        return if (isRunning) {
            elapsedTime + (System.currentTimeMillis() - startTime)
        } else {
            elapsedTime
        }
    }
    
    fun isTimerRunning(): Boolean = isRunning
    
    companion object {
        private const val UPDATE_INTERVAL = 10L // Обновление каждые 10ms
    }
}
