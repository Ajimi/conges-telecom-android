package com.telecom.conges.data.models

import java.util.*

data class DaysOff(
    val id: String,
    val name: String,
    val dateStart: Date,
    val dateEnd: Date
)