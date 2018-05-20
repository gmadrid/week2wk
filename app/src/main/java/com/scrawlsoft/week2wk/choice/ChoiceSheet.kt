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
    var choices: List<T> = arrayListOf()
    var convertFunc: ((T) -> String)? = null
    var clickFunc: ((T) -> Unit)? = null

    companion object {
        fun <T> newInstance(choices: List<T>,
                            convertFunc: (T) -> String,
                            clickFunc: (T) -> Unit
        ): ChoiceSheet<T> {
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
            val view = LayoutInflater.from(parent.context).inflate(R.layout.choice_list_item_layout, parent, false)
            return ChoiceViewHolder(view)
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
        val recyclerView = inflater.inflate(R.layout.choice_sheet_layout, container, false) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = ChoiceAdapter(this, choices, convertFunc!!, clickFunc!!)
        return recyclerView
    }
}