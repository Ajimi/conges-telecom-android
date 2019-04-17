package com.telecom.conges.data

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface AuthService {
    @POST("auth/login")
    @Headers("Accept: application/json")
    fun login(@Body user: LoginDTO): Deferred<Response<AccessToken>>
}

class AccessToken {}

data class LoginDTO(val username: String, val password: String) {}