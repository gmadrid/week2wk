package com.scrawlsoft.week2wk.common

import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

/**
 * Set an onFocusChangeListener so that when the View receives focus,
 * the soft keyboard will pop up.
 */
fun View.showSoftKeyboardOnFocus(context: Context) {
    this.setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }
}

fun View.convertPoint(fromPoint: Point, fromView: View): Point {
    val fromCoord = IntArray(2)
    val toCoord = IntArray(2)
    fromView.getLocationOnScreen(fromCoord)
    this.getLocationOnScreen(toCoord)

    return Point(fromCoord[0] - toCoord[0] + fromPoint.x,
            fromCoord[1] - toCoord[1] + fromPoint.y)
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
        return "last ${dayOfWeek}"
    }
    if (this.isBefore(today.plusDays(7)) && this.isAfter(today)) {
        return dayOfWeek
    }

    if (this.year == today.year) {
        return this.format(noYearFormat)
    }

    return this.format(withYearFormat)
}