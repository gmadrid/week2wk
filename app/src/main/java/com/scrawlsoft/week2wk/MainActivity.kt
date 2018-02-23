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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : SignedInActivity() {
    class ViewHolderr(val view: TextView) : RecyclerView.ViewHolder(view) {

    }

    class Adapterr : RecyclerView.Adapter<ViewHolderr>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderr {
            val textView = TextView(parent.context)
            Log.d("ADAPTER", "CVH")
            return ViewHolderr(textView)
        }

        override fun onBindViewHolder(holder: ViewHolderr, position: Int) {
            holder.view.text = "GEORGE: $position"
        }

        override fun getItemCount(): Int = 60
    }

    private val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        task_recycler.adapter = Adapterr()
        task_recycler.layoutManager = LinearLayoutManager(this)

        add_task_text.setOnEditorActionListener { textView, i, keyEvent ->
            val desc: String = textView.text.toString()
            textView.text = ""

            if (desc.isNotBlank()) {
                val task = TaskModel(getUid(), desc)

                FirebaseFirestore.getInstance()
                        .collection("users").document(getUid())
                        .collection("tasks").add(task)
                        .addOnSuccessListener {
                            Snackbar.make(main_container, "Task added: $desc", Snackbar.LENGTH_SHORT).show()
                            task_fab.visibility = View.VISIBLE
                            add_task_frame.visibility = View.GONE

                            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(add_task_frame.windowToken, 0)
                        }
                        .addOnFailureListener {
                            Snackbar.make(main_container, "Failed to add task", Snackbar.LENGTH_INDEFINITE).show()
                        }
            }
            true
        }
        add_task_text.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        task_fab.setOnClickListener {
            add_task_frame.visibility = View.VISIBLE
            add_task_text.requestFocus()
            task_fab.visibility = View.GONE

//            val task = TaskModel(getUid(), "The task text")
//
//            val db = FirebaseFirestore.getInstance()
//            db.collection("users").document(getUid())
//                    .collection("tasks").add(task)
//                    .addOnSuccessListener {
//                        Snackbar.make(findViewById(R.id.main_container), "Task added", Snackbar.LENGTH_SHORT).show()
//                        Log.d(TAG, "SUCCESS")
//                    }
//                    .addOnFailureListener { Log.d(TAG, "FAILURE") }
        }
    }
}
