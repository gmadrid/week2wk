package com.scrawlsoft.week2wk

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
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
    }

    override fun onStart() {
        super.onStart()

        task_fab.setOnClickListener {
            val task = TaskModel(getUid(), "The task text")

            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(getUid())
                    .collection("tasks").add(task)
                    .addOnSuccessListener {
                        Snackbar.make(findViewById(R.id.main_container), "Task added", Snackbar.LENGTH_SHORT).show()
                        Log.d(TAG, "SUCCESS")
                    }
                    .addOnFailureListener { Log.d(TAG, "FAILURE") }
        }
    }
}
