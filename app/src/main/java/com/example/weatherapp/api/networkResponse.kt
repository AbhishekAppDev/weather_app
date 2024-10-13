package com.example.weatherapp.api


sealed class networkResponse<out T> {
    data class Success<out T>(val data: T) : networkResponse<T>()
    data class Error(val message: String) : networkResponse<Nothing>()
    object Loading : networkResponse<Nothing>()
}
