package com.scrawlsoft.week2wk

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : SignedInActivity() {
    private val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(this)
        print("GEORGE")
        print(account)

        Toast.makeText(this, "HI", Toast.LENGTH_LONG).show()

        val db = FirebaseFirestore.getInstance()

        val user = HashMap<String,String>()
        user.put("GEORGE", "MADRID")
        user.put("WAS", "here")
        user.put("quux", "foosball")

        db.collection("users")
                .add(user as Map<String, Any>)
                .addOnSuccessListener { Log.d(TAG, "SUCCESS") }
                .addOnFailureListener { e -> Log.d(TAG, "FAILURE: $e") }

    }
}
