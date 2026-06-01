package com.example.scrollablemodul3.model.data.remote

sealed interface ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>
    data class Error(
        val errorType: ErrorType = ErrorType.UNKNOWN,
        val statusCode: Int? = null
    ) : ApiResponse<Nothing>
    data object Loading : ApiResponse<Nothing>
}