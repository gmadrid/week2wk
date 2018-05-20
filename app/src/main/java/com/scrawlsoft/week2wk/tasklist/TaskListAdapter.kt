package com.scrawlsoft.week2wk.tasklist

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
                      private val rowClickedHandler: RowClicked)
    : FirestoreRecyclerAdapter<TaskModel, TaskListAdapter.ViewHolder>(options),
        TaskListTouchHelperCallback.SwipeCallback {

    private var lastX: Float = 0.0.toFloat()
    private var lastY: Float = 0.0.toFloat()

    interface RowClicked {
        fun onRowClicked(snapshot: DocumentSnapshot, view: View, x: Float, y: Float)
        fun onDateClicked(snapshot: DocumentSnapshot)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val descView: TextView = view.findViewById(R.id.list_desc)
        val dateView: TextView = view.findViewById(R.id.list_date)
        val dateButton: ImageButton = view.findViewById(R.id.list_date_button)
        //val doneView: CheckBox = view.findViewById(R.id.list_done)
        var task: TaskModel? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_list_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, task: TaskModel) {

        val snapshot = snapshots.getSnapshot(position)
        holder.task = task
        holder.descView.text = task.text
        // TODO: make this depend on DEBUG build.
        @SuppressLint("SetTextI18n")
        holder.dateView.text = "${task.displayDate()} - ${snapshot.id.substring(0, 3)}"

        val resources = holder.view.resources
        val color = if (task.localDate().isBefore(LocalDate.now())) {
            resources.getColor(R.color.secondaryDarkColor, holder.view.context.theme)
        } else {
            resources.getColor(R.color.primaryTextColor, holder.view.context.theme)
        }
        holder.dateView.setTextColor(color)

        holder.view.setOnTouchListener { _, motionEvent ->
            lastX = motionEvent.x
            lastY = motionEvent.y
            false
        }

        holder.view.setOnClickListener { view ->
            rowClickedHandler.onRowClicked(snapshot, view, lastX, lastY)
        }

        holder.dateButton.setOnClickListener {
            rowClickedHandler.onDateClicked(snapshot)
        }
    }

    override fun swipeTask(viewHolder: ViewHolder) {
        val task = viewHolder.task
        if (task != null) {
            val position = viewHolder.adapterPosition
            val snapshop = snapshots.getSnapshot(position)
            task.done = true
            snapshop.reference.set(task)
                    .addOnSuccessListener { Log.d("TODO", "SUCCESS") }
                    .addOnFailureListener { Log.d("TODO", "FAILURE") }
        }
    }
}