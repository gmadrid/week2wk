package com.scrawlsoft.week2wk.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

val noYearFormat = DateTimeFormatter.ofPattern("LLL dd")
val withYearFormat = DateTimeFormatter.ofPattern("LLL dd, yyyy")

data class TaskModel(
        var text: String? = null,
        var dateString: String? = null,
        var done: Boolean = false,
        var movedTo: String? = null
) {
    constructor(text: String, date: LocalDate) : this(text, date.toString())
}

fun TaskModel.localDate(): LocalDate = LocalDate.parse(dateString!!)

fun TaskModel.displayDate(): String {
    val str = dateString ?: return ""

    val localDate = LocalDate.parse(str)
    val today = LocalDate.now()

    if (localDate.isEqual(today)) {
        return "Today"
    }
    if (localDate.isEqual(today.minusDays(1))) {
        return "Yesterday"
    }
    if (localDate.isEqual(today.plusDays(1))) {
        return "Tomorrow"
    }

    val dayOfWeek = localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    if (localDate.isAfter(today.minusDays(7)) && localDate.isBefore(today)) {
        return "last ${dayOfWeek}"
    }
    if (localDate.isBefore(today.plusDays(7)) && localDate.isAfter(today)) {
        return dayOfWeek
    }

    if (localDate.year == today.year) {
        return localDate.format(noYearFormat)
    }

    return localDate.format(withYearFormat)
}