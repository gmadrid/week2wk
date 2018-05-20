package com.scrawlsoft.week2wk.choice

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView

class ChoiceSheet<T>() : BottomSheetDialogFragment() {
    var choices : List<T> = arrayListOf()
    var convertFunc : ((T) -> String)? = null
    var clickFunc: ((T) -> Unit)? = null

    companion object {
        fun <T> newInstance(choices: List<T>,
                            convertFunc: (T) -> String,
                            clickFunc: (T) -> Unit
        ) : ChoiceSheet<T> {
            val sheet = ChoiceSheet<T>()
            sheet.choices = choices
            sheet.convertFunc = convertFunc
            sheet.clickFunc = clickFunc
            return sheet
        }
    }

    private class ChoiceViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private class ChoiceAdapter<T>(val fragment: DialogFragment,
                                   val choices: List<T>,
                                   val convertFunc: (T) -> String,
                                   val clickFunc: (T) -> Unit)
        : RecyclerView.Adapter<ChoiceViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoiceViewHolder {
            val textView = TextView(parent.context)
            val lp = ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT)
            textView.layoutParams = lp
            textView.setTextColor(Color.BLACK)
            textView.setBackgroundColor(Color.GREEN)
            textView.textSize = 16.0.toFloat()

            val scale = parent.context.resources.displayMetrics.density
            val dp20 = (20.0 * scale + 0.5f).toInt()
            textView.setPadding(0, dp20, 0, dp20)

            val tv = TypedValue()
            parent.context.theme.resolveAttribute(android.R.attr.selectableItemBackground, tv, true)
            textView.setBackgroundResource(tv.resourceId)

            return ChoiceViewHolder(textView)
        }

        override fun getItemCount(): Int {
            return choices.size
        }

        override fun onBindViewHolder(holder: ChoiceViewHolder, position: Int) {
            val textView = holder.itemView as TextView

            textView.text = convertFunc(choices[position])
            textView.setOnClickListener {
                clickFunc(choices[position])
                fragment.dismiss()
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val lp = ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT)

        val recyclerView = RecyclerView(activity)
        val localActivity = activity
        val scale = if (localActivity == null) { 1.0f } else { localActivity.resources.displayMetrics.density }
        val dp16 = (16 * scale + 0.5f).toInt()
        recyclerView.setPadding(dp16, 8, dp16, 8)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutParams = lp

        recyclerView.adapter = ChoiceAdapter(this, choices, convertFunc!!, clickFunc!!)

        return recyclerView
    }
}