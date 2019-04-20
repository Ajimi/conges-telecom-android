package com.telecom.conges.data.services.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import com.telecom.conges.data.models.User

class LoginLocalRepository(private val prefs: SharedPreferences) {

    /**
     * Instance of the logged in user. If missing, null is returned
     */
    var user: User?
        get() {
            val userId = prefs.getString(KEY_USER_ID, "")
            val username = prefs.getString(KEY_USER_NAME, null)
            val email = prefs.getString(KEY_EMAIL, null)
            val role = prefs.getString(KEY_ROLE, null)
            if (userId == "" || userId == null || username == null) {
                return null
            }
            // TODO save the entire user
            val user = User(
                email = email!!,
                id = userId.toInt(),
                username = username,
                role = role!!
            )

            return user
        }
        set(value) {
            if (value != null) {
                prefs.edit {
                    putString(KEY_USER_ID, value.id.toString())
                    putString(KEY_USER_NAME, value.username)
                    putString(KEY_EMAIL, value.email)
                    putString(KEY_ROLE, value.role)
                }
            }
        }


    /**
     * Clear all data related to this Designer News instance: user data and access token
     */
    fun logout() {
        prefs.edit {
            putString(KEY_USER_ID, null)
            putString(KEY_USER_NAME, null)
            putString(KEY_EMAIL, null)
            putString(KEY_ROLE, null)
        }
    }

    companion object {
        private const val KEY_USER_ID = "KEY_USER_ID"
        private const val KEY_USER_NAME = "KEY_USER_NAME"
        private const val KEY_EMAIL = "KEY_EMAIL"
        private const val KEY_ROLE = "KEY_ROLE"
    }
}