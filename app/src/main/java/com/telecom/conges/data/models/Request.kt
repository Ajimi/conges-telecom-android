package com.telecom.conges.data.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Request(
    @SerializedName("dateEnd")
    val dateEnd: Date,
    @SerializedName("dateStart")
    val dateStart: Date,
    @SerializedName("id")
    val id: Int,
    @SerializedName("reason")
    val reason: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("isApproved")
    val isApproved: Boolean,
    var user: User? = null
)