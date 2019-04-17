package com.telecom.conges.util

import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("message")
    val message: String,
    @SerializedName("origin")
    val origin: String,
    @SerializedName("path")
    val path: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("validatorArgs")
    val validatorArgs: List<Any>,
    @SerializedName("validatorKey")
    val validatorKey: String,
    @SerializedName("validatorName")
    val validatorName: Any,
    @SerializedName("value")
    val value: String
)