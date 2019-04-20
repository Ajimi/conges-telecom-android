package com.telecom.conges.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.esprit.core.extensions.UiState
import com.esprit.core.extensions.emitUiState
import com.telecom.conges.data.Result
import com.telecom.conges.data.services.AuthenticationService
import com.telecom.conges.util.Event
import kotlinx.coroutines.*

class LoginViewModel(
    private val authService: AuthenticationService
) : ViewModel() {


    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _uiState = MutableLiveData<UiState<String>>()
    val uiState: LiveData<UiState<String>>
        get() = _uiState

    private val _dialogState = MutableLiveData<Event<Boolean>>()
    val dialogState: LiveData<Event<Boolean>>
        get() = _dialogState

    private val _navigateToMainAction = MutableLiveData<Event<Boolean>>()
    val navigateToMainAction: LiveData<Event<Boolean>>
        get() = _navigateToMainAction

    fun login(username: String, password: String, hasNetwork: Boolean = true) {
        if (hasNetwork) {
            launchLogin(username, password)
        } else {
            _uiState.value = emitUiState(showError = Event("Check your network"))
        }
    }

    private fun launchLogin(username: String, password: String) = uiScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) { _dialogState.value = Event(true) }

        val result = authService.login(username, password)

        if (result is Result.Success) {
            withContext(Dispatchers.Main) {
                Log.v("Hello", "Message")
                _dialogState.value = Event(false)
                _navigateToMainAction.value = Event(true)
            }
        } else {
            Log.v("Hello", "${result}")
            withContext(Dispatchers.Main) {
                _dialogState.value = Event(false)
                _uiState.value = emitUiState(showError = Event("Password or username is wrong"))
            }
        }
    }


}