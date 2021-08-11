package ru.simaland.poster.util

import android.content.SharedPreferences
import android.widget.ImageView
import androidx.core.content.edit
import com.bumptech.glide.Glide
import ru.simaland.poster.R

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
    get() = getInt(idKey, 0)
    set(value) {
        edit {
            putInt(idKey, value)
            apply()
        }
    }

fun ImageView.loadImg(path: String) =
    Glide.with(this)
        .load(path)
        .error(R.drawable.ic_baseline_person_24)
        .timeout(10_000)
        .into(this)

fun Int.getInfo(): Int =
    when (this) {
        1 -> R.string.registration_error
        2 -> R.string.login_error
        else -> R.string.unknown_error
    }
