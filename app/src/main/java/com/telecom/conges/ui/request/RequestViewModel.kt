package com.telecom.conges.ui.request

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.telecom.conges.data.Result
import com.telecom.conges.data.models.Request
import com.telecom.conges.data.models.RequestRequest
import com.telecom.conges.data.models.RequestRole
import com.telecom.conges.data.services.auth.AuthenticationHelper
import com.telecom.conges.data.services.request.RequestHelper
import com.telecom.conges.extensions.UiState
import com.telecom.conges.extensions.emitUiState
import com.telecom.conges.util.Event
import com.telecom.conges.util.State
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

    private val _uiAcceptRequestState = MutableLiveData<UiState<String>>()
    val uiAcceptRequestState: LiveData<UiState<String>>
        get() = _uiAcceptRequestState

    private val _uiRefuseRequestState = MutableLiveData<UiState<String>>()
    val uiRefuseRequestState: LiveData<UiState<String>>
        get() = _uiRefuseRequestState


    private val _uiCreateRequestState = MutableLiveData<UiState<Request>>()
    val uiCreateRequestState: LiveData<UiState<Request>>
        get() = _uiCreateRequestState

    private var _currentFiltering = "ALL"

    private var _currentRequests: List<Request> = mutableListOf()

    fun loadRequestsByType(type: String = RequestRole.USER.name) {
        Log.v("USER ID", "${authenticationHelper.user?.id}")
        Log.v("USER ID", type)
        launchRequestsByType(authenticationHelper.user?.id.toString(), type)
    }

    fun getMaxSolde(): Int = authenticationHelper.user?.let {
        it.solde - it.consumedSolde
    } ?: run {
        30
    }

    private fun launchRequestsByType(id: String, type: String = RequestRole.USER.name) =
        uiScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                _uiHistoriesState.value = emitUiState(showProgress = true)
            }

            val result = requestService.getRequestsByType(id, type.toLowerCase())

            when (result) {
                is Result.Success -> {
                    val requests = result.data.flatMap {
                        it.requests
                    }.also {
                        _currentRequests = it
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

    fun filterRequest(filter: String) {
        _currentFiltering = filter.apply {
            if (this == "ALL") {
                _uiHistoriesState.value = emitUiState(showSuccess = Event(_currentRequests))
                return
            }
        }
        _currentRequests.let {
            return@let it.filter { it.state == _currentFiltering }
        }.also {
            _uiHistoriesState.value = emitUiState(showSuccess = Event(it))
            return
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
                dateEnd = request.dateEnd,
                dateStart = request.dateStart,
                id = request.id,
                isApproved = request.isApproved,
                reason = request.reason,
                state = State.WAITING.name,
                userId = authenticationHelper.user?.id.toString()
            )
        )
    }

    private fun launchCreateRequest(request: RequestRequest) = uiScope.launch(Dispatchers.IO) {

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

    fun acceptRequest(id: String) {
        launchAcceptRequest(id)
    }

    private fun launchAcceptRequest(id: String) =
        uiScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _uiAcceptRequestState.value = emitUiState(showProgress = true)
            }

            val result = requestService.acceptRequest(id)
            when (result) {
                is Result.Success -> {
                    withContext(Dispatchers.Main) {
                        _uiAcceptRequestState.value = emitUiState(showSuccess = Event(result.data))
                    }
                }
                is Result.Error -> withContext(Dispatchers.Main) {
                    Log.v("ERROR", result.exception.toString())
                    _uiAcceptRequestState.value = emitUiState(showError = Event(result.toString()))
                }
            }
        }

    fun refuseRequest(id: String) {
        launchRefuseRequest(id)
    }

    private fun launchRefuseRequest(id: String) =
        uiScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _uiRefuseRequestState.value = emitUiState(showProgress = true)
            }

            val result = requestService.refuseRequest(id)
            when (result) {
                is Result.Success -> {
                    withContext(Dispatchers.Main) {
                        _uiRefuseRequestState.value = emitUiState(showSuccess = Event(result.data))
                    }
                }
                is Result.Error -> withContext(Dispatchers.Main) {
                    Log.v("ERROR", result.exception.toString())
                    _uiRefuseRequestState.value = emitUiState(showError = Event(result.toString()))
                }
            }
        }


}