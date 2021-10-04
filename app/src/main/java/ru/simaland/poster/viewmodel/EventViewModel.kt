package ru.simaland.poster.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.simaland.poster.MediaUpload
import ru.simaland.poster.PhotoModel
import ru.simaland.poster.model.Event
import ru.simaland.poster.model.User
import ru.simaland.poster.repository.auth.AuthRepository
import ru.simaland.poster.repository.event.EventRepository
import ru.simaland.poster.repository.user.UserRepository
import ru.simaland.poster.state.ErrorType
import ru.simaland.poster.state.EventsState
import ru.simaland.poster.util.getCurrentDateAsString
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _editMode: MutableLiveData<Boolean> = MutableLiveData(false)
    val editMode: LiveData<Boolean> get() = _editMode

    private val _dataState: MutableLiveData<EventsState> = MutableLiveData<EventsState>()
    val dataState: LiveData<EventsState>
        get() = _dataState

    private val _currentEvent: MutableLiveData<Event?> = MutableLiveData()
    val currentEvent: LiveData<Event?> get() = _currentEvent

    private val _speakers: MutableLiveData<List<User>> = MutableLiveData()
    val speakers: LiveData<List<User>> get() = _speakers

    private val _photo: MutableLiveData<PhotoModel> = MutableLiveData(PhotoModel(null, null))
    val photo: LiveData<PhotoModel>
        get() = _photo

    val data = repository.data.flowOn(Dispatchers.IO)

    fun save(savedEvent: Event) {
        val event = currentEvent.value ?: return
        viewModelScope.launch {
            try {
                _dataState.value = EventsState.Loading
                _currentEvent.value = if (_photo.value == null) {
                    repository.save(
                        event.copy(
                            content = savedEvent.content,
                            type = savedEvent.type,
                            published = getCurrentDateAsString()
                        )
                    )
                } else {
                    repository.save(
                        event.copy(
                            content = savedEvent.content,
                            type = savedEvent.type,
                            published = getCurrentDateAsString()
                        ),
                        _photo.value?.file?.let { MediaUpload(it) })
                }
                _dataState.value = EventsState.Success
                changeEditMode(false)
            } catch (e: Exception) {
                changeEditMode(true)
                _dataState.value = EventsState.Error(e.message ?: "", ErrorType.UnknownError)
            }
        }
    }

    fun loadLast() {
        viewModelScope.launch {
            try {
                _dataState.value = EventsState.Loading
                repository.readLast(10)
                _dataState.value = EventsState.Success
            } catch (e: Exception) {
                _dataState.value = EventsState.Error(e.message ?: "", ErrorType.UnknownError)
            }
        }
    }

    fun loadSpeakers(speakersId: List<Int>) {
        viewModelScope.launch {
            try {
                _speakers.value = userRepository.getUsersById(speakersId)
            } catch (e: Exception) {
            }
        }
    }

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }

    fun onEventClicked(event: Event) {
        _currentEvent.value = event
    }

    fun onBackPressed() {
        _currentEvent.value = null
    }

    fun changeEditMode(isEditMode: Boolean) {
        _editMode.value = isEditMode
    }

    fun isEditMode(): Boolean = _editMode.value ?: false

    fun addEvent() {
        viewModelScope.launch {
            _editMode.value = true
            authRepository.getCurrentUser().let {
                _currentEvent.value =
                    Event(
                        author = it.name,
                        authorId = it.id,
                        authorAvatar = it.avatar ?: "",
                        datetime = getCurrentDateAsString()
                    )
            }
        }
    }

    init {
        viewModelScope.launch {
            try {
                _dataState.value = EventsState.Loading
                repository.readAll()
                _dataState.value = EventsState.Success
            } catch (e: Exception) {
                _dataState.value = EventsState.Error(e.message ?: "", ErrorType.UnknownError)
            }
        }
    }

}