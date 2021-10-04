package ru.simaland.poster.state

sealed class AuthState {
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val error: Any, val code: ErrorType) : AuthState()
}

sealed class EventsState {
    object Loading : EventsState()
    object Success : EventsState()
    data class Error(val error: Any, val code: ErrorType) : EventsState()
}


sealed class ErrorType {
    object LoginError : ErrorType()
    object RegistrationError : ErrorType()
    object UnknownError : ErrorType()
}

