package com.scrawlsoft.week2wk.tasklist

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.scrawlsoft.week2wk.R
import com.scrawlsoft.week2wk.base.SignedInActivity
import com.scrawlsoft.week2wk.common.W2WApp
import com.scrawlsoft.week2wk.model.TaskModel
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate

//////////////////
//
// Things to do
// * Capitalization in input form
// * FAB in input form
// * Add date picker to input form
// * Date short names everywhere
// * Date picker in list
// * Day of the week headers
// * Filter popdown in list header
// * Move Adapter and ViewHolder to own file.
// * Weekly review screen
// * Move model access functions to separate place.
// * Make SignedInActivity.getUid return an optional, and deal with it correctly everywhere.
//
/////////////////////
class MainActivity : SignedInActivity() {
    private val _tag: String = this.javaClass.simpleName

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descView: TextView = view.findViewById(R.id.list_desc)
        val dateView: TextView = view.findViewById(R.id.list_date)
        val doneView: CheckBox = view.findViewById(R.id.list_done)
    }

    class MyAdapter(options: FirestoreRecyclerOptions<TaskModel>)
        : FirestoreRecyclerAdapter<TaskModel, ViewHolder>(options) {
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

    override fun onCreateWithUser(savedInstanceState: Bundle?) {
        (application as W2WApp).appComponent.inject(this)
        setContentView(R.layout.activity_main)

        setupActionBar()
        setupRecycler()
        setupFab()
        setupTaskEdit()
    }

    private fun setupRecycler() {
        val query = FirebaseFirestore.getInstance().collection("users").document(getUid())
                .collection("tasks")
                .orderBy("dateString")
                .limit(50)
        val options = FirestoreRecyclerOptions.Builder<TaskModel>()
                .setQuery(query, TaskModel::class.java)
                .setLifecycleOwner(this)
                .build()
        val adapter = MyAdapter(options)
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
        task_fab.setOnClickListener { showTaskEdit() }
    }

    private fun setupTaskEdit() {
        add_task_text.setOnEditorActionListener { textView, _, _ ->
            val desc: String = textView.text.toString()
            textView.text = ""

            Log.d(_tag, "DESC: $desc")
            if (desc.isNotBlank()) {
                val task = TaskModel(desc, LocalDate.now())

                Log.d(_tag, "Adding task: $task for user: ${getUid()}")
                FirebaseFirestore.getInstance()
                        .collection("users").document(getUid())
                        .collection("tasks").add(task)
                        .addOnSuccessListener {
                            Log.d(_tag, "Added task: $desc")
                            Snackbar.make(main_container, "Task added: $desc", Snackbar.LENGTH_SHORT).show()
                            hideTaskEdit()
                        }
                        .addOnFailureListener {
                            Log.e(_tag, "Failed to add task: $desc")
                            Snackbar.make(main_container, "Failed to add task", Snackbar.LENGTH_INDEFINITE).show()
                        }
            }
            true
        }
    }

    override fun onBackPressed() {
        if (add_task_frame.visibility == View.VISIBLE) {
            hideTaskEdit()
        } else {
            super.onBackPressed()
        }
    }

    private fun showTaskEdit() {
        val cx = main_container.right - 30
        val cy = main_container.bottom - 60
        val finalRadius = Math.max(main_container.width, main_container.height)
        val anim = ViewAnimationUtils.createCircularReveal(add_task_frame, cx, cy, 0f, finalRadius.toFloat())
        add_task_frame.visibility = View.VISIBLE
        add_task_text.setText("", TextView.BufferType.NORMAL)
        anim.start()
        add_task_text.requestFocus()
    }

    private fun hideTaskEdit() {
        val cx = main_container.right - 30
        val cy = main_container.bottom - 60
        val initialRadius = add_task_frame.width.toFloat()
        val anim = ViewAnimationUtils.createCircularReveal(add_task_frame, cx, cy, initialRadius, 0f)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                add_task_frame.visibility = View.GONE
            }
        })
        anim.start()

//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(add_task_frame.windowToken, 0)
    }

}