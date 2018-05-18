package com.scrawlsoft.week2wk.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.firebase.ui.auth.AuthUI

import com.google.firebase.auth.FirebaseAuth
import com.scrawlsoft.week2wk.tasklist.MainActivity

class AuthActivity : AppCompatActivity() {
    companion object {
        private val _tag = this::class.java.simpleName
        private val RC_SIGN_IN = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser != null) {
            // We're already logged in, so proceed to main activity.
            // TODO: allow forwarding to a "continue" Intent.
            startActivity(Intent(this, MainActivity::class.java))
            return
        }

        // Otherwise, we have to log in.
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(arrayListOf(
                                AuthUI.IdpConfig.GoogleBuilder().build()
                        ))
                        // TODO: do you want to turn off smart lock for dev builds?
                        .build(),
                RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            // TODO: what's all the business about ipdresponse?
            // https://github.com/firebase/FirebaseUI-Android/tree/master/auth

            Log.i(_tag, "onActivityResult called")
            if (resultCode == Activity.RESULT_OK) {
                Log.i(_tag, "     ...and it was okay.")
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}
