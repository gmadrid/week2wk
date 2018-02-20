package com.scrawlsoft.week2wk

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.common.SignInButton

class SigninActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin_layout)

        // You need a message or something here.
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        //signInButton.setOnClickListener(this)
    }

}