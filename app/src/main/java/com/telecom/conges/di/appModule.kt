package com.telecom.conges.di

import android.preference.PreferenceManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.telecom.conges.data.AuthService
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val myModule = module {
    single { PreferenceManager.getDefaultSharedPreferences(get()) }
    single { getOkHttpClient() }
    single { getRetrofit(get()) }

}

private fun getOkHttpClient(): OkHttpClient {
    val httpBuilder = OkHttpClient.Builder()
    httpBuilder.connectTimeout(30, TimeUnit.SECONDS)
    httpBuilder.readTimeout(30, TimeUnit.SECONDS)
    httpBuilder.writeTimeout(30, TimeUnit.SECONDS)
    return httpBuilder
        .build()
}

private fun getRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl("http://localhost:3000/")
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(okHttpClient)
    .build()


private fun getLoginService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)