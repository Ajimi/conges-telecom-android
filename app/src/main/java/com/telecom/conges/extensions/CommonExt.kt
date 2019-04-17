package com.telecom.conges.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

val Int.isEven: Boolean
    get() = this % 2 == 0


inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)