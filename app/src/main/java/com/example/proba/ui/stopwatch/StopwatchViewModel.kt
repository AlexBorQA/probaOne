package com.example.proba.ui.stopwatch

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proba.domain.LapTime
import com.example.proba.domain.SoundManager
import com.example.proba.domain.StopwatchTimer
import com.example.proba.domain.TimeFormatter

class StopwatchViewModel : ViewModel() {

    private var soundManager: SoundManager? = null

    private val _timeText = MutableLiveData<String>().apply {
        value = "00:00.00"
    }
    val timeText: LiveData<String> = _timeText

    private val _isRunning = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isRunning: LiveData<Boolean> = _isRunning

    private val _buttonText = MutableLiveData<String>().apply {
        value = "START"
    }
    val buttonText: LiveData<String> = _buttonText

    private val _buttonColor = MutableLiveData<ButtonState>().apply {
        value = ButtonState.START
    }
    val buttonColor: LiveData<ButtonState> = _buttonColor

    private val _lapTimes = MutableLiveData<List<LapTime>>().apply {
        value = emptyList()
    }
    val lapTimes: LiveData<List<LapTime>> = _lapTimes

    enum class ButtonState {
        START, PAUSE
    }

    private val stopwatchTimer = StopwatchTimer(
        onTick = { timeInMillis ->
            _timeText.value = TimeFormatter.formatTime(timeInMillis)
        },
        onLapAdded = { lapTime ->
            addLapTime(lapTime)
        }
    )

    fun toggleTimer() {
        if (stopwatchTimer.isTimerRunning()) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    fun initializeSoundManager(context: Context) {
        if (soundManager == null) {
            soundManager = SoundManager(context)
        }
    }
    
    fun setSoundEnabled(enabled: Boolean) {
        soundManager?.setSoundEnabled(enabled)
    }
    
    fun setVibrationEnabled(enabled: Boolean) {
        soundManager?.setVibrationEnabled(enabled)
    }
    
    fun isSoundEnabled(): Boolean = soundManager?.isSoundEnabled() ?: true
    
    fun isVibrationEnabled(): Boolean = soundManager?.isVibrationEnabled() ?: true

    private fun startTimer() {
        stopwatchTimer.start()
        _isRunning.value = true
        _buttonText.value = "PAUSE"
        _buttonColor.value = ButtonState.PAUSE
        soundManager?.playStartSound()
    }

    private fun pauseTimer() {
        stopwatchTimer.pause()
        _isRunning.value = false
        _buttonText.value = "START"
        _buttonColor.value = ButtonState.START
        soundManager?.playStopSound()
    }

    fun addLap() {
        stopwatchTimer.addLap()
        soundManager?.playLapSound()
    }

    private fun addLapTime(lapTime: Long) {
        val currentLaps = _lapTimes.value ?: emptyList()
        val lapNumber = currentLaps.size + 1
        val previousTotalTime = currentLaps.lastOrNull()?.totalTime ?: 0L
        val lapDuration = lapTime - previousTotalTime
        
        val newLap = LapTime(
            lapNumber = lapNumber,
            totalTime = lapTime,
            lapTime = lapDuration
        )
        
        _lapTimes.value = currentLaps + newLap
    }

    fun resetTimer() {
        stopwatchTimer.reset()
        _isRunning.value = false
        _buttonText.value = "START"
        _buttonColor.value = ButtonState.START
        _timeText.value = "00:00.00"
        _lapTimes.value = emptyList()
    }

    fun refreshState() {
        // Обновляем отображение текущего времени без сброса таймера
        if (stopwatchTimer.isTimerRunning()) {
            _timeText.value = TimeFormatter.formatTime(stopwatchTimer.getElapsedTime())
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        stopwatchTimer.reset()
        soundManager?.release()
        soundManager = null
    }
}
