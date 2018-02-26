package com.scrawlsoft.week2wk

import java.time.LocalDate

data class TaskModel(
        var text: String? = null,
        var dateString: String? = null,
        var done: Boolean = false,
        var movedTo: String? = null
) {
    constructor(text: String, date: LocalDate) : this(text, date.toString())
}
