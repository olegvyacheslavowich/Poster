package ru.simaland.poster.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.simaland.poster.MediaUpload
import ru.simaland.poster.PhotoModel
import ru.simaland.poster.auth.AppAuth
import ru.simaland.poster.auth.Auth
import ru.simaland.poster.repository.auth.AuthRepository
import ru.simaland.poster.state.AuthState
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val appAuth: AppAuth
) : ViewModel() {

    val authData: LiveData<Auth> = appAuth.authStateFlow.asLiveData(Dispatchers.Default)

    private val _state: MutableLiveData<AuthState> = MutableLiveData()
    val state: LiveData<AuthState>
        get() = _state

    private val _photo: MutableLiveData<PhotoModel> = MutableLiveData()
    val photo: LiveData<PhotoModel>
        get() = _photo

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }

    fun login(login: String, password: String) {
        viewModelScope.launch {
            try {
                _state.value = AuthState.Loading
                val auth = authRepository.login(login, password)
                auth.collect {
                    appAuth.setAuth(it.id, it.token ?: "")

                }
                _state.value = AuthState.Success
            } catch (e: Exception) {
                _state.value = AuthState.Error(e.message.toString())
            }
        }
    }

    fun register(
        name: String,
        login: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                _state.value = AuthState.Loading
                val file = _photo.value?.file
                val auth = if (file == null) {
                    authRepository.register(name, login, password)
                } else {
                    authRepository.register(MediaUpload(file), name, login, password)
                }
                auth.collect {
                    appAuth.setAuth(it.id, it.token ?: "")
                }
                _state.value = AuthState.Success
            } catch (e: Exception) {
                _state.value = AuthState.Error(e.message.toString())
            }
        }
    }
}

