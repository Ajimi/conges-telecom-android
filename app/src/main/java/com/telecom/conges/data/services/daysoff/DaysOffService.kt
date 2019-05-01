package com.telecom.conges.data.services.daysoff

import com.telecom.conges.data.models.DaysOff
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*


interface DaysOffService {

    @GET("daysoff")
    fun getAllDaysOff(): Deferred<Response<List<DaysOff>>>

    @POST("daysoff")
    fun createDaysOff(@Body() daysOff: DaysOff): Deferred<Response<DaysOff>>

    @DELETE("daysoff/{id}")
    fun deleteDaysOff(@Path("id") id: String): Deferred<Response<String>>

    @DELETE("daysoff")
    fun editDaysOff(@Path("id") id: String): Deferred<Response<String>>

}
