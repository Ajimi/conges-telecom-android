package com.telecom.conges.di

import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.telecom.conges.data.services.auth.AuthService
import com.telecom.conges.data.services.auth.AuthenticationHelper
import com.telecom.conges.data.services.auth.LoginLocalRepository
import com.telecom.conges.data.services.daysoff.DaysOffHelper
import com.telecom.conges.data.services.daysoff.DaysOffService
import com.telecom.conges.data.services.request.RequestHelper
import com.telecom.conges.data.services.request.RequestService
import com.telecom.conges.ui.account.AccountViewModel
import com.telecom.conges.ui.calendar.CalendarViewModel
import com.telecom.conges.ui.ferie.DaysOffViewModel
import com.telecom.conges.ui.login.LoginViewModel
import com.telecom.conges.ui.main.MainViewModel
import com.telecom.conges.ui.request.RequestViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


val myModule = module {
    single { PreferenceManager.getDefaultSharedPreferences(get()) }
    single { getOkHttpClient() }
    single { getGson() }
    single { getRetrofit(get(), get()) }


    single { getAuthService(get()) }
    single { getRequestService(get()) }
    single { getDaysOffService(get()) }

    single { LoginLocalRepository(get()) }
    single { AuthenticationHelper(get(), get()) }
    single { RequestHelper(get()) }
    single { DaysOffHelper(get()) }

    viewModel { MainViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RequestViewModel(get(), get()) }
    viewModel { DaysOffViewModel(get()) }
    viewModel { CalendarViewModel(get(), get(), get(), get()) }
    viewModel { AccountViewModel(get()) }

}

private fun getGson(): Gson = GsonBuilder()
    .setLenient()
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    .registerTypeAdapter(Date::class.java, DateTypeDeserializer()).create()

private fun getOkHttpClient(): OkHttpClient {
    val httpBuilder = OkHttpClient.Builder()
    httpBuilder.connectTimeout(30, TimeUnit.SECONDS)
    httpBuilder.readTimeout(30, TimeUnit.SECONDS)
    httpBuilder.writeTimeout(30, TimeUnit.SECONDS)
    return httpBuilder
        .build()
}

private fun getRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl("http://localhost:3000/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(okHttpClient)
        .build()
}

private fun getAuthService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)
private fun getRequestService(retrofit: Retrofit): RequestService = retrofit.create(RequestService::class.java)
private fun getDaysOffService(retrofit: Retrofit): DaysOffService = retrofit.create(DaysOffService::class.java)