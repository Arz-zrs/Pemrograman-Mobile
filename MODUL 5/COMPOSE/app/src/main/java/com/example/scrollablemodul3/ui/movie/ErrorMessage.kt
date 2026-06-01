package com.example.scrollablemodul3.ui.movie

import androidx.annotation.StringRes

sealed interface ErrorMessage {
    data class ErrorMessageRes(@StringRes val messageRes: Int) : ErrorMessage
    data object None : ErrorMessage
}