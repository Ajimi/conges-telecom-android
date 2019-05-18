package com.telecom.conges.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.telecom.conges.R
import com.telecom.conges.data.models.RequestRole
import com.telecom.conges.data.services.auth.AuthenticationHelper

class MainViewModel(private val authenticationHelper: AuthenticationHelper) : ViewModel() {

    fun getLayoutByRole(): Int {
        val user = authenticationHelper.user
        return when (getRole()?.toUpperCase()) {
            RequestRole.USER.name -> R.layout.activity_main_user
            RequestRole.HR.name -> R.layout.activity_main_rh
            RequestRole.SUPERVISOR.name -> R.layout.activity_main_supervisor
            else -> {
                Log.v("Hayaa", "bina")
                return R.layout.activity_main_user
            }
        }
    }

    fun logout() {
        authenticationHelper.logout()
    }


    fun isLoggedIn(): Boolean = authenticationHelper.isLoggedIn

    fun getRole() = authenticationHelper.user?.role
}