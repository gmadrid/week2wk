package com.scrawlsoft.week2wk

import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : SignedInActivity() {
    private val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        findViewById<Button>(R.id.thebutton).setOnClickListener {
            val task = TaskModel(getUid(), "The task text")

            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(getUid())
                    .collection("tasks").add(task)
                    .addOnSuccessListener { Log.d(TAG, "SUCCESS") }
                    .addOnFailureListener { Log.d(TAG, "FAILURE") }
        }

//        val account = GoogleSignIn.getLastSignedInAccount(this)

//        Toast.makeText(this, "HI", Toast.LENGTH_LONG).show()

//        val db = FirebaseFirestore.getInstance()

//        val user = HashMap<String,String>()
//        user.put("GEORGE", "MADRID")
//        user.put("WAS", "here")
//        user.put("quux", "foosball")
//
//        db.collection("users")
//                .add(user as Map<String, Any>)
//                .addOnSuccessListener { Log.d(TAG, "SUCCESS") }
//                .addOnFailureListener { e -> Log.d(TAG, "FAILURE: $e") }

    }
}
