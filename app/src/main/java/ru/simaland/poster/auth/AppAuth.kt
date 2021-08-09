package ru.simaland.poster.auth

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.simaland.poster.util.id
import ru.simaland.poster.util.token
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    private val prefs: SharedPreferences
) {

    private val _authStateFlow: MutableStateFlow<Auth>

    init {
        val id = prefs.id
        val token = prefs.token

        if (id == 0 || token == null) {
            _authStateFlow = MutableStateFlow(Auth())
            with(prefs.edit()) {
                clear()
                apply()
            }
        } else {
            _authStateFlow = MutableStateFlow(Auth(id, token))
        }

    }

    val authStateFlow = _authStateFlow.asStateFlow()

    @Synchronized
    fun setAuth(id: Int, token: String) {
        _authStateFlow.value = Auth(id, token)
        prefs.token = token
        prefs.id = id
    }

    @Synchronized
    fun removeAuth() {
        with(prefs.edit()) {
            clear()
            commit()
        }
    }

}

data class Auth(val id: Int = 0, val token: String? = null)