package com.scrawlsoft.week2wk

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : SignedInActivity() {
    private val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        actionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onStart() {
        super.onStart()

        findViewById<FloatingActionButton>(R.id.task_fab).setOnClickListener {
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
