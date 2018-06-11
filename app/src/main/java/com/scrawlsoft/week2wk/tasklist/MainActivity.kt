package com.scrawlsoft.week2wk.tasklist

import android.graphics.Point
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.scrawlsoft.week2wk.R
import com.scrawlsoft.week2wk.base.SignedInActivity
import com.scrawlsoft.week2wk.choice.ChoiceSheet
import com.scrawlsoft.week2wk.common.W2WApp
import com.scrawlsoft.week2wk.common.convertPoint
import com.scrawlsoft.week2wk.model.TaskModel
import com.scrawlsoft.week2wk.model.naturalString
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate

//////////////////
//
// Things to do
// * Add a delete button
// * Add multi-edit for date/delete.
// * Add Select All
//
// * Add day picker to input form
// * Add custom date picker to day picker.
//
// * Day of the week headers
// * Filter popdown in list header
// * Weekly review screen
// * Move model access functions to separate place.
// * Clear keyboard when done with entry view.
// * Nav drawer
// * Deal with case where login fails somehow.
//
// * Allow setting date in task entry box.
// * Close task entry box immediately, and update user with Toasts about status of comms.
//
/////////////////////
class MainActivity : SignedInActivity(), TaskListAdapter.RowClicked {
    private lateinit var taskFrame: TaskFrame

    override fun onCreateWithUser(savedInstanceState: Bundle?) {
        (application as W2WApp).appComponent.inject(this)
        setContentView(R.layout.activity_main)

        val uid = getUid()
        setupActionBar()
        setupRecycler(uid)
        setupFab()
        setupTaskFrame(uid)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupTaskFrame(uid: String) {
        taskFrame = TaskFrame(add_task_frame, uid, main_container, task_fab)
    }

    private fun setupRecycler(uid: String) {
        val query = FirebaseFirestore.getInstance().collection("users").document(uid)
                .collection("tasks")
                .whereEqualTo("done", false)
                .whereEqualTo("deleted", false)
                .orderBy("dateString")
                .limit(50)
        val options = FirestoreRecyclerOptions.Builder<TaskModel>()
                .setQuery(query, TaskModel::class.java)
                .setLifecycleOwner(this)
                .build()
        val adapter = TaskListAdapter(options, this)
        task_recycler.adapter = adapter
        task_recycler.layoutManager = LinearLayoutManager(this)

        val foobar = ItemTouchHelper(TaskListTouchHelperCallback(adapter))
        foobar.attachToRecyclerView(task_recycler)
    }

    override fun onRowClicked(snapshot: DocumentSnapshot, view: View, x: Float, y: Float) {
        val pt = main_container.convertPoint(Point(x.toInt(), y.toInt()), view)
        taskFrame.show(snapshot, pt)
    }

    override fun onDateClicked(snapshot: DocumentSnapshot) {
        val today = LocalDate.now()
        val choices = 0.rangeTo(5).map { today.plusDays(it.toLong()) }

        val f = ChoiceSheet.newInstance(choices, object : ChoiceSheet.ChoiceListener<LocalDate> {
            override fun itemToString(item: LocalDate): String {
                return item.naturalString()
            }

            override fun itemClicked(item: LocalDate) {
                val task = snapshot.toObject(TaskModel::class.java)
                if (task != null) {
                    task.dateString = item.toString()
                    snapshot.reference.set(task)
                }
            }
        })

        // TODO: highlight the currently selected item.
        f.show(supportFragmentManager, "choice fragment")
    }

    override fun onDeleteClicked(snapshot: DocumentSnapshot) {
        // TODO: Need to ask first.
        // TODO: replace this with a deleted flag to allow UNDO.
        val task = snapshot.toObject(TaskModel::class.java)
        if (task != null) {
            task.deleted = true
            snapshot.reference.set(task)
        }
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_signout -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
