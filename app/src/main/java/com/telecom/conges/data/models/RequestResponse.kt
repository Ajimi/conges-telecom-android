package com.telecom.conges.data.models

import com.google.gson.annotations.SerializedName

data class RequestResponse(
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    val passwordHash: String,
    @SerializedName("requests")
    val requests: List<Request>,
    @SerializedName("role")
    val role: String,
    @SerializedName("username")
    val username: String
)