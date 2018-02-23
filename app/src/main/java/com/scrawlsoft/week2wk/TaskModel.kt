package com.scrawlsoft.week2wk

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class TaskModel(
        var text: String? = null,
        var onDateAtNoon: LocalDateTime? = null,
        var done: Boolean = false,
        var movedTo: String? = null
) {
    constructor() : this(null, null, false, null)
    constructor(text: String, date: LocalDate) : this(text, date.atTime(LocalTime.NOON))

//    fun onDate() = onDateAtNoon?.toLocalDate()

//    @Exclude
//    var onDate: LocalDate?
//        get() = onDateAtNoon?.toLocalDate()
//        set(value) {
//            onDateAtNoon = value?.atTime(LocalTime.NOON)
//        }
}
