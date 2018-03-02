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
import com.scrawlsoft.week2wk.R
import com.scrawlsoft.week2wk.model.TaskModel

class TaskListAdapter(options: FirestoreRecyclerOptions<TaskModel>)
    : FirestoreRecyclerAdapter<TaskModel, TaskListAdapter.ViewHolder>(options) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
        holder.dateView.text = task.dateString
        holder.doneView.isChecked = task.done

        val snapshot = snapshots.getSnapshot(position)
        holder.doneView.setOnClickListener { view ->
            task.done = (view as CheckBox).isChecked
            snapshot.reference.set(task)
                    .addOnSuccessListener { Log.d("TODO", "SUCCESS") }
                    .addOnFailureListener { Log.d("TODO", "FAILURE") }
        }
    }
}