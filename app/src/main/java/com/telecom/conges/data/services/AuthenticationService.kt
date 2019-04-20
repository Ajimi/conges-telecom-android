package com.telecom.conges.data.services

import android.util.Log
import com.auth0.android.jwt.JWT
import com.telecom.conges.data.AuthService
import com.telecom.conges.data.LoginDTO
import com.telecom.conges.extensions.safeApiCall
import java.io.IOException
import com.telecom.conges.data.Result;
import com.telecom.conges.data.models.User

class AuthenticationService(
    private val auth: AuthService,
    private val loginLocalRepository: LoginLocalRepository
) {

    var user: User? = null
        private set
    val isLoggedIn: Boolean
        get() = user != null

    suspend fun login(username: String, password: String) = safeApiCall(
        call = { requestLogin(username, password) },
        errorMessage = "Error logging in"
    )


    private suspend fun requestLogin(username: String, password: String): Result<User> {
        val response = auth.login(LoginDTO(username, password)).await()

        Log.v("Hello", response.toString())
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                val token = body.token
                Log.v("ACCESS_TOKEN", token)
//                tokenLocalDataSource.authToken = token
                val result = requestUser(token)
                if (result is Result.Success) {
                    setLoggedInUser(result.data)
                }
                return result
            }
        }

        Log.v("Helo", "Access token retrieval failed ${response.code()} ${response.message()}")
        return Result.Error(IOException("Access token retrieval failed ${response.code()} ${response.message()}"))
    }


    private fun getUserIdFrom(token: String) = JWT(token).getClaim("id").asString().toString()

    private suspend fun requestUser(token: String): Result<User> {

        val userID = getUserIdFrom(token = token)

        val response = auth.get(userID).await()

        if (response.isSuccessful) {
            val user = response.body()
            if (user != null) {
                return Result.Success(user)
            }
        }

//        v { response.errorBody().toString() }

        return Result.Error(IOException("Failed to get authed user ${response.code()} ${response.message()}"))
    }

    fun logout() {
        user = null
        loginLocalRepository.logout()
    }

    private fun setLoggedInUser(loggedInUser: User) {
        user = loggedInUser
        loginLocalRepository.user = user
    }
}