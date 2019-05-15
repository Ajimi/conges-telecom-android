package com.telecom.conges.util

import com.google.gson.annotations.SerializedName

data class ErrorHandler(
    @SerializedName("className")
    val className: String,
    @SerializedName("code")
    val code: Int,
    @SerializedName("errors")
    val errors: List<Error>,
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String
)