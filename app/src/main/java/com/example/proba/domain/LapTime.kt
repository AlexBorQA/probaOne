package com.example.proba.domain

data class LapTime(
    val lapNumber: Int,
    val totalTime: Long,
    val lapTime: Long
) {
    val formattedTotalTime: String = TimeFormatter.formatTime(totalTime)
    val formattedLapTime: String = TimeFormatter.formatTime(lapTime)
}
