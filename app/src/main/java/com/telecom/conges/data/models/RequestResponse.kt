package com.telecom.conges.data.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class RequestResponse(
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("role")
    val role: String,
    @SerializedName("username")
    val username: String = "",
    @SerializedName("firstname")
    val firstname: String = "",
    @SerializedName("lastname")
    val lastname: String = "",
    @SerializedName("cin")
    val cin: String = "",
    @SerializedName("consumedSolde")
    val consumedSolde: Int = 0,
    @SerializedName("solde")
    val solde: Int = 28,
    @SerializedName("registerDate")
    val registerDate: Date,
    @SerializedName("requests")
    val requests: List<Request>
)