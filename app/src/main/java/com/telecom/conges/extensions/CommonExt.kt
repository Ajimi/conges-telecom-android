package com.telecom.conges.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import java.util.*

val Int.isEven: Boolean
    get() = this % 2 == 0


inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)

fun Date.FormatDate(): String {
    val localDate = LocalDate(this)
    return DateTimeFormat.forPattern("E dd MMM Y").withLocale(Locale.FRENCH).print(localDate)
}

fun Date.FormatedDate(): String {
    val localDate = LocalDate(this)
    return DateTimeFormat.forPattern("dd MMM Y").withLocale(Locale.FRENCH).print(localDate)
}

fun Date.numberOfDays(endDate: Date): Pair<LocalDate, Int> {
    val dateStart = LocalDate(this)
    val dateEnd = LocalDate(endDate)
    val days = Days.daysBetween(dateStart, dateEnd)
    return Pair(dateStart, days.days)
}

fun String.getEmptyMessageFromFilter(): String =
    when (this.toLowerCase()) {
        "refused" -> "Liste des refusé congés est vides"
        "waiting" -> "Liste de congés en attente est vides"
        "accepted" -> "Il n'ya pas aucune congés accepter"
        else -> "Il n'ya aucune congés"
    }

