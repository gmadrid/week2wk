package com.scrawlsoft.week2wk.tasklist

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class TaskListTouchHelperCallback(private val callback: SwipeCallback) : ItemTouchHelper.Callback() {
    interface SwipeCallback {
        fun swipeTask(viewHolder: TaskListAdapter.ViewHolder)
    }

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        val dragFlags = 0
        val swipeFlags = ItemTouchHelper.END
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        callback.swipeTask(viewHolder as TaskListAdapter.ViewHolder)
    }
}