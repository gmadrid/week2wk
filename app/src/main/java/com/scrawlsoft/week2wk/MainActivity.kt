package com.scrawlsoft.week2wk

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate

class MainActivity : SignedInActivity() {
    private val TAG: String = this.javaClass.simpleName

    class ViewHolder(val view: TextView) : RecyclerView.ViewHolder(view) {

    }

    class MyAdapter(options: FirestoreRecyclerOptions<TaskModel>)
        : FirestoreRecyclerAdapter<TaskModel, ViewHolder>(options) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val textView = TextView(parent.context)
            return ViewHolder(textView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int, task: TaskModel) {
            holder.view.text = task.text
            Log.d("MYADAPTER", task.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()
        setupRecycler()
        setupFab()
        setupTaskEdit()
    }

    private fun setupRecycler() {
        val query = FirebaseFirestore.getInstance().collection("users").document(getUid())
                .collection("tasks").limit(50)
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
        add_task_text.setOnEditorActionListener { textView, i, keyEvent ->
            val desc: String = textView.text.toString()
            textView.text = ""

            if (desc.isNotBlank()) {
                val task = TaskModel(desc, LocalDate.now())

                FirebaseFirestore.getInstance()
                        .collection("users").document(getUid())
                        .collection("tasks").add(task)
                        .addOnSuccessListener {
                            Snackbar.make(main_container, "Task added: $desc", Snackbar.LENGTH_SHORT).show()
                            hideTaskEdit()
                        }
                        .addOnFailureListener {
                            Snackbar.make(main_container, "Failed to add task", Snackbar.LENGTH_INDEFINITE).show()
                        }
            }
            true
        }
        add_task_text.showSoftKeyboardOnFocus(this)
    }

    private fun showTaskEdit() {
        add_task_frame.visibility = View.VISIBLE
        add_task_text.requestFocus()
        task_fab.visibility = View.GONE
    }

    private fun hideTaskEdit() {
        task_fab.visibility = View.VISIBLE
        add_task_frame.visibility = View.GONE

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(add_task_frame.windowToken, 0)
    }

}
