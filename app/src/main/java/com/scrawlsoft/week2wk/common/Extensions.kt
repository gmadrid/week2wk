package com.scrawlsoft.week2wk.common

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

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
