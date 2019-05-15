package com.telecom.conges.data.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class RequestRequest(
    @SerializedName("dateEnd")
    val dateEnd: Date,
    @SerializedName("dateStart")
    val dateStart: Date,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isApproved")
    val isApproved: Boolean,
    @SerializedName("reason")
    val reason: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("userId")
    val userId: String
)