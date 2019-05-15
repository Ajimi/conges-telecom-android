package com.telecom.conges.ui.ferie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.telecom.conges.data.Result
import com.telecom.conges.data.models.DaysOff
import com.telecom.conges.data.services.daysoff.DaysOffHelper
import com.telecom.conges.extensions.UiState
import com.telecom.conges.extensions.emitUiState
import com.telecom.conges.util.Event
import kotlinx.coroutines.*

class DaysOffViewModel(
    val daysOffHelper: DaysOffHelper
) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _uiDaysOffState = MutableLiveData<UiState<List<DaysOff>>>()
    val uiDaysOffState: LiveData<UiState<List<DaysOff>>>
        get() = _uiDaysOffState

    private val _uiCreateDaysOffState = MutableLiveData<UiState<DaysOff>>()
    val uiCreateDaysOffState: LiveData<UiState<DaysOff>>
        get() = _uiCreateDaysOffState


    fun loadAllDaysOff() {
        launchLoadAllDaysOff()
    }

    private fun launchLoadAllDaysOff() = uiScope.launch(Dispatchers.IO) {

        withContext(Dispatchers.Main) {
            _uiDaysOffState.value = emitUiState(showProgress = true)
        }

        val result = daysOffHelper.getAllDaysOff()

        when (result) {
            is Result.Success -> {

                withContext(Dispatchers.Main) {
                    if (result.data.isEmpty()) {
                        // TODO Emit Empty State
                    } else {
                        _uiDaysOffState.value = emitUiState(showSuccess = Event(result.data))
                    }
                }
            }
            is Result.Error -> withContext(Dispatchers.Main) {
                Log.v("ERROR", result.exception.toString())
                _uiDaysOffState.value = emitUiState(showError = Event(result.toString()))
            }
        }
    }

    fun createDaysOff(daysOff: DaysOff) {
        launchCreateDaysOff(daysOff)
    }

    private fun launchCreateDaysOff(daysOff: DaysOff) = uiScope.launch(Dispatchers.IO) {

        withContext(Dispatchers.Main) {
            _uiCreateDaysOffState.value = emitUiState(showProgress = true)
        }

        val result = daysOffHelper.createDaysOff(daysOff)

        when (result) {
            is Result.Success -> {
                withContext(Dispatchers.Main) {
                    _uiCreateDaysOffState.value = emitUiState(showSuccess = Event(result.data))
                }
            }
            is Result.Error -> withContext(Dispatchers.Main) {
                Log.v("ERROR", result.exception.toString())
                _uiCreateDaysOffState.value = emitUiState(showError = Event(result.toString()))
            }
        }
    }

    fun deleteDaysOff(id: String) {
        launchDeleteDaysOff(id)
    }

    private fun launchDeleteDaysOff(id: String) = uiScope.launch(Dispatchers.IO) {

        withContext(Dispatchers.Main) {
            _uiDaysOffState.value = emitUiState(showProgress = true)
        }

        val result = daysOffHelper.deleteDaysOff(id)

        when (result) {
            is Result.Success -> {
                withContext(Dispatchers.Main) {
                    //_uiDaysOffState.value = emitUiState(showSuccess = Event(result.data))
                }
            }
            is Result.Error -> withContext(Dispatchers.Main) {
                //_uiDaysOffState.value = emitUiState(showError = Event(result.toString()))
            }
        }

    }
}