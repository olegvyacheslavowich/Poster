package ru.simaland.poster.state

sealed class AuthState {
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val error: String, val code: Int = 0) : AuthState()
}
