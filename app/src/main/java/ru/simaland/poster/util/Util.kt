package ru.simaland.poster.util

import android.content.SharedPreferences
import androidx.core.content.edit

private const val tokenKey = "token"
private const val idKey = "id"

var SharedPreferences.token: String?
    get() = getString(tokenKey, null)
    set(value) {
        edit {
            putString(tokenKey, value)
            apply()
        }
    }

var SharedPreferences.id: Int
    get() = getInt(tokenKey, 0)
    set(value) {
        edit {
            putInt(tokenKey, value)
            apply()
        }
    }