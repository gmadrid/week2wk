package com.scrawlsoft.week2wk.tasklist

import android.graphics.Point
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.View
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.scrawlsoft.week2wk.R
import com.scrawlsoft.week2wk.base.SignedInActivity
import com.scrawlsoft.week2wk.common.W2WApp
import com.scrawlsoft.week2wk.common.convertPoint
import com.scrawlsoft.week2wk.model.TaskModel
import kotlinx.android.synthetic.main.activity_main.*

//////////////////
//
// Things to do
// * Add date picker to input form
// * Date picker in list
// * Day of the week headers
// * Filter popdown in list header
// * Weekly review screen
// * Move model access functions to separate place.
// * Clear keyboard when done with entry view.
// * Add Logout somewhere.
// * Nav drawer
//
/////////////////////
class MainActivity : SignedInActivity(), TaskListAdapter.RowClicked {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSignedIn() {
        super.onSignedIn()
        task_recycler.adapter.notifyDataSetChanged()
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
        val adapter = TaskListAdapter(options, this)
        task_recycler.adapter = adapter
        task_recycler.layoutManager = LinearLayoutManager(this)
    }

    override fun onRowClicked(snapshot: DocumentSnapshot, view: View, x: Float, y: Float) {
        var pt = main_container.convertPoint(Point(x.toInt(), y.toInt()), view)
        taskFrame.show(snapshot, pt)
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
