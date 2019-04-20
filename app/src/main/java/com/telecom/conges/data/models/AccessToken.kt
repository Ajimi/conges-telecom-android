package com.telecom.conges.data.models

import com.google.gson.annotations.SerializedName

data class AccessToken(@SerializedName("expires_in") val expiresIn: Int, val token: String) {}