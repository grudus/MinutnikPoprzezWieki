package com.grudus.minutnikpoprzezwieki

data class TimeState(val remainingTime: String, val progress: Float) {
    init {
        require(progress in 0.0..100.0)
    }
}