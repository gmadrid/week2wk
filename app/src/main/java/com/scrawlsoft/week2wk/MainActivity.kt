package com.scrawlsoft.week2wk

import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn

class MainActivity : SignedInActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(this)
        print("GEORGE")
        print(account)
    }
}
