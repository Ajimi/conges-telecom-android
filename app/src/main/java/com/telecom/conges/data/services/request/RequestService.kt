package com.telecom.conges.data.services.request

import com.telecom.conges.data.models.Request
import com.telecom.conges.data.models.RequestRequest
import com.telecom.conges.data.models.RequestResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface RequestService {

    @GET("requests")
    fun getAllRequest(): Deferred<Response<List<Request>>>

    @GET("requests/{id}")
    fun getRequest(@Path("id") id: String): Deferred<Response<Request>>

    @GET("requests/{id}/{type}")
    fun getAllRequestByTypes(@Path("id") userId: String, @Path("type") type: String): Deferred<Response<List<RequestResponse>>>

    @POST("requests")
    @Headers("Accept: application/json")
    fun createRequest(@Body request: RequestRequest): Deferred<Response<List<String>>> // TODO : Check

    @PUT("requests/{id}")
    @Headers("Accept: application/json")
    fun modifyRequest(@Path("id") id: String, @Body request: Request): Deferred<Response<List<String>>> // TODO CHECK Return typpe

}
