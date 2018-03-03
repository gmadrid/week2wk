package com.scrawlsoft.week2wk.tasklist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.scrawlsoft.week2wk.R
import com.scrawlsoft.week2wk.base.SignedInActivity
import com.scrawlsoft.week2wk.common.W2WApp
import com.scrawlsoft.week2wk.model.TaskModel
import kotlinx.android.synthetic.main.activity_main.*

//////////////////
//
// Things to do
// * Capitalization in input form
// * Add date picker to input form
// * Date picker in list
// * Day of the week headers
// * Filter popdown in list header
// * Weekly review screen
// * Move model access functions to separate place.
//
/////////////////////
class MainActivity : SignedInActivity() {
    private lateinit var taskFrame: TaskFrame

    override fun onCreateWithUser(savedInstanceState: Bundle?) {
        (application as W2WApp).appComponent.inject(this)
        setContentView(R.layout.activity_main)

        val uid = getUid()
        if (uid != null) {
            setupActionBar()
            setupRecycler(uid)
            setupFab()
            setupTaskFrame(uid)
        }
    }

    private fun setupTaskFrame(uid: String) {
        taskFrame = TaskFrame(add_task_frame, uid, main_container, task_fab)
    }

    private fun setupRecycler(uid: String) {
        val query = FirebaseFirestore.getInstance().collection("users").document(uid)
                .collection("tasks")
                .whereEqualTo("done", false)
                .orderBy("dateString")
                .limit(50)
        val options = FirestoreRecyclerOptions.Builder<TaskModel>()
                .setQuery(query, TaskModel::class.java)
                .setLifecycleOwner(this)
                .build()
        val adapter = TaskListAdapter(options)
        task_recycler.adapter = adapter
        task_recycler.layoutManager = LinearLayoutManager(this)
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupFab() {
        task_fab.setOnClickListener {
            if (taskFrame.isShown()) {
                taskFrame.save()
            } else {
                taskFrame.show()
            }
        }
    }

    override fun onBackPressed() {
        if (taskFrame.isShown()) {
            taskFrame.hide()
        } else {
            super.onBackPressed()
        }
    }
}
