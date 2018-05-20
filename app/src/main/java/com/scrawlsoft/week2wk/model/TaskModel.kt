package com.scrawlsoft.week2wk.model

import com.scrawlsoft.week2wk.common.naturalString
import java.time.LocalDate


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
    return LocalDate.parse(str).naturalString()
}

