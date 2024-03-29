package com.telecom.conges.data.services.auth

import com.telecom.conges.data.models.AccessToken
import com.telecom.conges.data.models.LoginDTO
import com.telecom.conges.data.models.User
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface AuthService {
    @POST("auth/login")
    @Headers("Accept: application/json")
    fun login(@Body user: LoginDTO): Deferred<Response<AccessToken>>

    @GET("auth/user/{id}")
    @Headers("Accept: application/json")
    fun get(@Path("id") id: String): Deferred<Response<User>>
}
