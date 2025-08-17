package com.example.proba.ui.stopwatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proba.domain.StopwatchTimer
import com.example.proba.domain.TimeFormatter

class StopwatchViewModel : ViewModel() {

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

    private val stopwatchTimer = StopwatchTimer { timeInMillis ->
        _timeText.value = TimeFormatter.formatTime(timeInMillis)
    }

    fun toggleTimer() {
        if (stopwatchTimer.isTimerRunning()) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        stopwatchTimer.start()
        _isRunning.value = true
        _buttonText.value = "PAUSE"
    }

    private fun pauseTimer() {
        stopwatchTimer.pause()
        _isRunning.value = false
        _buttonText.value = "START"
    }

    fun resetTimer() {
        stopwatchTimer.reset()
        _isRunning.value = false
        _buttonText.value = "START"
        _timeText.value = "00:00.00"
    }

    override fun onCleared() {
        super.onCleared()
        stopwatchTimer.reset()
    }
}
