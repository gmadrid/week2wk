package com.scrawlsoft.week2wk.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.firebase.ui.auth.AuthUI

import com.google.firebase.auth.FirebaseAuth
import com.scrawlsoft.week2wk.common.RequestCodes
import com.scrawlsoft.week2wk.tasklist.MainActivity

class AuthActivity : AppCompatActivity() {
    private val _tag = this::class.java.simpleName

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
                        .setIsSmartLockEnabled(true)
                        // TODO: do you want to turn off smart lock for dev builds?
                        .build(),
                RequestCodes.SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(_tag, "AuthActivity: onActivityResult")
        if (requestCode == RequestCodes.SIGN_IN) {
            // TODO: what's all the business about ipdresponse?
            // https://github.com/firebase/FirebaseUI-Android/tree/master/auth
            Log.d(_tag, "AuthActivity: inside here: $resultCode")

            if (resultCode == Activity.RESULT_OK) {
                Log.i(_tag, "     ...and it was okay.")
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}
