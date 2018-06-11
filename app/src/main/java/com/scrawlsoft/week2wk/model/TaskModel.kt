package com.scrawlsoft.week2wk.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*


data class TaskModel(
        var text: String? = null,
        var dateString: String? = null,
        var done: Boolean = false,
        var deleted: Boolean = false,
        var movedTo: String? = null
) {
    constructor(text: String, date: LocalDate) : this(text, date.toString())
}

fun TaskModel.localDate(): LocalDate = LocalDate.parse(dateString!!)

fun TaskModel.displayDate(): String {
    val str = dateString ?: return ""
    return LocalDate.parse(str).naturalString()
}

private val noYearFormat = DateTimeFormatter.ofPattern("LLL dd")
private val withYearFormat = DateTimeFormatter.ofPattern("LLL dd, yyyy")

fun LocalDate.naturalString(): String {
    val today = LocalDate.now()

    if (this.isEqual(today)) {
        return "Today"
    }
    if (this.isEqual(today.minusDays(1))) {
        return "Yesterday"
    }
    if (this.isEqual(today.plusDays(1))) {
        return "Tomorrow"
    }

    val dayOfWeek = this.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    if (this.isAfter(today.minusDays(7)) && this.isBefore(today)) {
        return "last $dayOfWeek"
    }
    if (this.isBefore(today.plusDays(7)) && this.isAfter(today)) {
        return dayOfWeek
    }

    if (this.year == today.year) {
        return this.format(noYearFormat)
    }

    return this.format(withYearFormat)
}