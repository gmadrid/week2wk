package com.scrawlsoft.week2wk.choice

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.scrawlsoft.week2wk.R

class ChoiceSheet<T> : BottomSheetDialogFragment() {
    interface ChoiceListener<T> {
        fun itemToString(item: T): String
        fun itemClicked(item: T)
    }

    private lateinit var choices: List<T>
    private lateinit var listener: ChoiceListener<T>

    companion object {
        fun <T> newInstance(choices: List<T>, listener: ChoiceListener<T>): ChoiceSheet<T> {
            val sheet = ChoiceSheet<T>()
            sheet.choices = choices
            sheet.listener = listener
            return sheet
        }
    }

    private class ChoiceViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private class ChoiceAdapter<T>(val fragment: DialogFragment,
                                   val choices: List<T>,
                                   val listener: ChoiceListener<T>)
        : RecyclerView.Adapter<ChoiceViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoiceViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.choice_list_item_layout, parent, false)
            return ChoiceViewHolder(view)
        }

        override fun getItemCount(): Int {
            return choices.size
        }

        override fun onBindViewHolder(holder: ChoiceViewHolder, position: Int) {
            val textView = holder.itemView.findViewById<TextView>(R.id.choice_list_text_view)

            textView.text = listener.itemToString(choices[position])
            textView.setOnClickListener {
                listener.itemClicked(choices[position])
                fragment.dismiss()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val recyclerView = inflater.inflate(R.layout.choice_sheet_layout, container, false) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = ChoiceAdapter(this, choices, listener)
        return recyclerView
    }
}