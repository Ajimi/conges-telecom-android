package com.telecom.conges.ui.main

import androidx.lifecycle.ViewModel
import com.telecom.conges.R
import com.telecom.conges.data.services.auth.AuthenticationHelper

class MainViewModel(private val authenticationHelper: AuthenticationHelper) : ViewModel() {

    fun getLayoutByRole(): Int {
        val user = authenticationHelper.user
        return when (getRole()) {
            "USER" -> R.layout.activity_main
            "RH" -> R.layout.activity_main
            "SUPERVISOR" -> R.layout.activity_main
            else -> R.layout.activity_main
        }
    }

    fun getRole() = authenticationHelper.user?.role
}