package ru.simaland.poster.util

import android.app.AlertDialog
import android.content.SharedPreferences
import android.widget.ImageView
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import ru.simaland.poster.R
import ru.simaland.poster.state.ErrorType
import java.text.SimpleDateFormat
import java.util.*

private const val TOKEN_KEY = "token"
private const val ID_KEY = "id"

private const val DATE_FORMAT_WITH_TIME = "yyyy-MM-dd'T'HH:mm:ss'Z'"

const val photoRequestCode = 1
const val cameraRequestCode = 2

var SharedPreferences.token: String?
    get() = getString(TOKEN_KEY, null)
    set(value) {
        edit {
            putString(TOKEN_KEY, value)
            apply()
        }
    }

var SharedPreferences.id: Int
    get() = getInt(ID_KEY, 0)
    set(value) {
        edit {
            putInt(ID_KEY, value)
            apply()
        }
    }

fun ImageView.loadImg(path: String, errorImage: Int) =
    Glide.with(this)
        .load(path)
        .error(errorImage)
        .timeout(10_000)
        .into(this)


fun ImageView.loadLocalImg(name: String) =
    Glide.with(this)
        .load(
            if (name.lowercase() == "online")
                R.drawable.ic_baseline_online_24
            else R.drawable.ic_baseline_place_24
        )
        .error(R.drawable.ic_baseline_person_24)
        .timeout(10_000)
        .into(this)


fun ErrorType.getInfo(): Int =
    when (this) {
        ErrorType.RegistrationError -> R.string.registration_error
        ErrorType.LoginError -> R.string.login_error
        ErrorType.UnknownError -> R.string.unknown_error
    }

fun Fragment.buildPhotoMenu() =
    AlertDialog.Builder(this.requireContext())
        .setTitle(R.string.photo_choose)
        .setPositiveButton(R.string.choose_photo) { _, _ ->
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .galleryOnly()
                .galleryMimeTypes(
                    arrayOf(
                        "image/png", "image/jpeg"
                    )
                )
                .start(photoRequestCode)
        }
        .setNeutralButton(R.string.take_photo) { _, _ ->
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .cameraOnly()
                .start(cameraRequestCode)
        }
        .create()

fun getCurrentDate(): Date? {
    val time = Calendar.getInstance().time
    return SimpleDateFormat(DATE_FORMAT_WITH_TIME, Locale.getDefault())
        .format(time)
        .toDate()
}

fun getCurrentDateAsString(): String {
    val time = Calendar.getInstance().time
    return SimpleDateFormat(DATE_FORMAT_WITH_TIME, Locale.getDefault())
        .format(time)
}

fun String.toDate(): Date? =
    if (this.isEmpty()) {
        getCurrentDate()
    } else {
        SimpleDateFormat(DATE_FORMAT_WITH_TIME, Locale.getDefault()).parse(this)
    }

fun String.parseToStringDate() =
    this.toDate()?.let {
        SimpleDateFormat("d MMMM H:mm", Locale.getDefault()).format(it)
    }

