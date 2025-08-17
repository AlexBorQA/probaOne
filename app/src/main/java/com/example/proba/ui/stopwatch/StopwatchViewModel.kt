package com.example.proba.ui.stopwatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StopwatchViewModel : ViewModel() {

    private val _timeText = MutableLiveData<String>().apply {
        value = "00:00.000"
    }
    val timeText: LiveData<String> = _timeText

    private val _isRunning = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isRunning: LiveData<Boolean> = _isRunning

    private val _isPaused = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isPaused: LiveData<Boolean> = _isPaused

    fun startTimer() {
        // TODO: Реализовать логику старта
    }

    fun pauseTimer() {
        // TODO: Реализовать логику паузы
    }

    fun resetTimer() {
        // TODO: Реализовать логику сброса
        _timeText.value = "00:00.000"
        _isRunning.value = false
        _isPaused.value = false
    }
}
