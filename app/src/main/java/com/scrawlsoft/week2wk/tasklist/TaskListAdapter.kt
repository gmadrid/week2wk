package com.scrawlsoft.week2wk.tasklist

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.scrawlsoft.week2wk.R
import com.scrawlsoft.week2wk.model.TaskModel
import com.scrawlsoft.week2wk.model.displayDate
import com.scrawlsoft.week2wk.model.localDate
import java.time.LocalDate

class TaskListAdapter(options: FirestoreRecyclerOptions<TaskModel>,
                      val rowClickedHandler: RowClicked)
    : FirestoreRecyclerAdapter<TaskModel, TaskListAdapter.ViewHolder>(options) {

    interface RowClicked {
        fun onRowClicked(snapshot: DocumentSnapshot)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val descView: TextView = view.findViewById(R.id.list_desc)
        val dateView: TextView = view.findViewById(R.id.list_date)
        val doneView: CheckBox = view.findViewById(R.id.list_done)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_list_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, task: TaskModel) {
        holder.descView.text = task.text
        holder.dateView.text = task.displayDate()
        holder.doneView.isChecked = task.done

        val resources = holder.view.resources
        val color = if (task.localDate().isBefore(LocalDate.now())) {
            resources.getColor(R.color.secondaryDarkColor)
        } else {
            resources.getColor(R.color.primaryTextColor)
        }
        holder.dateView.setTextColor(color)

        val snapshot = snapshots.getSnapshot(position)
        holder.doneView.setOnClickListener { view ->
            task.done = (view as CheckBox).isChecked
            snapshot.reference.set(task)
                    .addOnSuccessListener { Log.d("TODO", "SUCCESS") }
                    .addOnFailureListener { Log.d("TODO", "FAILURE") }
        }

        holder.view.setOnClickListener {
            rowClickedHandler.onRowClicked(snapshot)
        }
    }
}