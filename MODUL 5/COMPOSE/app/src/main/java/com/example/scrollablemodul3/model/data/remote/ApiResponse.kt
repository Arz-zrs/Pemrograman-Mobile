package com.example.scrollablemodul3.model.data.remote

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(
        val message: String? = null,
        val throwable: Throwable? = null,
        val statusCode: Int? = null
    ) : ApiResponse<Nothing>()
    data object Loading : ApiResponse<Nothing>()
}