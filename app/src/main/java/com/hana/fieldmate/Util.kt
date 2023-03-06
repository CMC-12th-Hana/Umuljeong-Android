package com.hana.fieldmate

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


fun getCurrentTime(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Calendar.getInstance().time)
}

fun LocalDate.getFormattedTime(): String {
    return this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

fun LocalDate.getShortenFormattedTime(): String {
    return this.format(DateTimeFormatter.ofPattern("uu. MM. dd"))
}

fun String.toFormattedPhoneNum(): String {
    val list = this.split('-')
    val phoneNum = list.joinToString(separator = "", limit = 3)

    return "tel:$phoneNum"
}

fun String.toLocalDate(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

fun isValidString(str: String, regEx: String): Boolean {
    return str.matches(regEx.toRegex())
}

fun getFormattedTime(seconds: Int): String {
    val minute = seconds / 60
    val second = seconds % 60

    return String.format("%02d : %02d", minute, second)
}