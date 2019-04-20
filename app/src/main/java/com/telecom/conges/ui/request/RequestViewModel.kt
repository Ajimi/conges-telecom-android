package com.telecom.conges.ui.request

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.esprit.core.extensions.UiState
import com.esprit.core.extensions.emitUiState
import com.telecom.conges.data.Result
import com.telecom.conges.data.models.Request
import com.telecom.conges.data.models.RequestRequest
import com.telecom.conges.data.models.RequestType
import com.telecom.conges.data.services.auth.AuthenticationHelper
import com.telecom.conges.data.services.request.RequestHelper
import com.telecom.conges.util.Event
import kotlinx.coroutines.*

class RequestViewModel(
    val requestService: RequestHelper,
    val authenticationHelper: AuthenticationHelper
) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToHistoryItemAction = MutableLiveData<Event<String>>()
    val navigateToHistoryItemAction: LiveData<Event<String>>
        get() = _navigateToHistoryItemAction

    private val _uiHistoriesState = MutableLiveData<UiState<List<Request>>>()
    val uiHistoriesState: LiveData<UiState<List<Request>>>
        get() = _uiHistoriesState

    private val _uiRequestState = MutableLiveData<UiState<Request>>()
    val uiRequestState: LiveData<UiState<Request>>
        get() = _uiRequestState


    private val _uiCreateRequestState = MutableLiveData<UiState<String>>()
    val uiCreateRequestState: LiveData<UiState<String>>
        get() = _uiCreateRequestState


    fun loadRequestsByType(type: String = RequestType.USER.name) {
        Log.v("USER ID", "${authenticationHelper.user?.id}")
        Log.v("USER ID", type)
        launchRequestsByType(authenticationHelper.user?.id.toString(), type)
    }


    private fun launchRequestsByType(id: String, type: String = RequestType.USER.name) =
        uiScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                _uiHistoriesState.value = emitUiState(showProgress = true)
            }

            val result = requestService.getRequestsByType(id, type.toLowerCase())

            when (result) {
                is Result.Success -> {
                    val requests = result.data.flatMap {
                        it.requests
                    }
                    Log.v("Succes", requests.toString())
                    withContext(Dispatchers.Main) {

                        if (requests.isEmpty()) {
                            // TODO Emit Empty State
                        } else {
                            _uiHistoriesState.value = emitUiState(showSuccess = Event(requests))
                        }
                    }

                }
                is Result.Error -> withContext(Dispatchers.Main) {
                    Log.v("ERROR", result.exception.toString())
                    _uiHistoriesState.value = emitUiState(showError = Event(result.toString()))
                }
            }
        }

    fun launchLoadRequest(id: String) =
        uiScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                _uiRequestState.value = emitUiState(showProgress = true)
            }

            val result = requestService.getRequest(id)

            when (result) {
                is Result.Success -> {
                    withContext(Dispatchers.Main) {
                        _uiRequestState.value = emitUiState(showSuccess = Event(result.data))
                    }
                }
                is Result.Error -> withContext(Dispatchers.Main) {
                    Log.v("ERROR", result.exception.toString())
                    _uiRequestState.value = emitUiState(showError = Event(result.toString()))
                }
            }
        }

    fun createRequest(request: Request) {
        launchCreateRequest(
            RequestRequest(
                request.dateEnd,
                request.dateStart,
                request.id,
                request.isApproved,
                authenticationHelper.user?.id.toString()
            )
        )
    }

    fun launchCreateRequest(request: RequestRequest) = uiScope.launch(Dispatchers.IO) {

        withContext(Dispatchers.Main) {
            _uiCreateRequestState.value = emitUiState(showProgress = true)
        }

        val result = requestService.createRequest(request)

        when (result) {
            is Result.Success -> {
                withContext(Dispatchers.Main) {
                    _uiCreateRequestState.value = emitUiState(showSuccess = Event(result.data))
                }
            }
            is Result.Error -> withContext(Dispatchers.Main) {
                Log.v("ERROR", result.exception.toString())
                _uiCreateRequestState.value = emitUiState(showError = Event(result.toString()))
            }
        }
    }

}