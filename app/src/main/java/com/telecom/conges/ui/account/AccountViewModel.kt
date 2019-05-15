package com.telecom.conges.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.telecom.conges.data.Result
import com.telecom.conges.data.models.User
import com.telecom.conges.data.services.auth.AuthenticationHelper
import com.telecom.conges.extensions.UiState
import com.telecom.conges.extensions.emitUiState
import com.telecom.conges.util.Event
import kotlinx.coroutines.*

class AccountViewModel(
    val authenticationHelper: AuthenticationHelper
) : ViewModel() {

    val user: User?
        get() {
            return authenticationHelper.user?.apply {
                loadUserDetails(id.toString())
            }
        }

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _uiUserState = MutableLiveData<UiState<User>>()
    val uiUserState: LiveData<UiState<User>>
        get() = _uiUserState

    private val _navigateToProductDetailAction = MutableLiveData<Event<String>>()
    val navigateToProductDetailAction: LiveData<Event<String>>
        get() = _navigateToProductDetailAction

    fun loadUserDetails(userId: String) {
        launchLoadUserDetails(userId)
    }

    private fun launchLoadUserDetails(userId: String) = uiScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main) {
            _uiUserState.value = emitUiState(showProgress = true)
        }
        val result = authenticationHelper.getUser(userId)


        when (result) {
            is Result.Success -> {
                withContext(Dispatchers.Main) {
                    _uiUserState.value = emitUiState(showSuccess = Event(result.data))
                }

            }
            is Result.Error -> withContext(Dispatchers.Main) {
                _uiUserState.value = emitUiState(showError = Event(result.exception.toString()))
            }
        }
    }

}