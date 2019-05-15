package com.telecom.conges.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.telecom.conges.util.Event

fun <T> ViewModel.emitUiState(
    showProgress: Boolean = false,
    showError: Event<String>? = null,
    showSuccess: Event<T>? = null
): UiState<T>? = UiState<T>(
    showProgress = showProgress,
    showError = showError,
    showSuccess = showSuccess
)


data class UiState<T>(
    var showProgress: Boolean,
    var showError: Event<String>?,
    var showSuccess: Event<T>?
)


fun <T> Fragment.observeUIState(
    uiModel: UiState<T>,
    progress: (Boolean) -> Unit,
    error: (String) -> Unit,
    success: (T) -> Unit
) {

    progress(uiModel.showProgress)

    if (uiModel.showError != null && !uiModel.showError!!.consumed) {
        uiModel.showError!!.consume()?.let {
            error(it)
        }
    }
    if (uiModel.showSuccess != null && !uiModel.showSuccess!!.consumed) {
        uiModel.showSuccess!!.consume()?.let {
            success(it)
        }
    }

}

fun <T> AppCompatActivity.observeUIState(
    uiModel: UiState<T>,
    progress: (Boolean) -> Unit,
    error: (String) -> Unit,
    success: (T) -> Unit
) {

    progress(uiModel.showProgress)

    if (uiModel.showError != null && !uiModel.showError!!.consumed) {
        uiModel.showError!!.consume()?.let {
            error(it)
        }
    }
    if (uiModel.showSuccess != null && !uiModel.showSuccess!!.consumed) {
        uiModel.showSuccess!!.consume()?.let {
            success(it)
        }
    }

}