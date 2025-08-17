package com.example.proba.domain

object TimeFormatter {
    
    fun formatTime(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val millis = (milliseconds % 1000) / 10 // Показываем только сотые доли секунды
        
        return "%02d:%02d.%02d".format(minutes, seconds, millis)
    }
    
    fun formatTimeWithMillis(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val millis = milliseconds % 1000
        
        return "%02d:%02d.%03d".format(minutes, seconds, millis)
    }
}
