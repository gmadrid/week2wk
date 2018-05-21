package com.scrawlsoft.week2wk.base

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.scrawlsoft.week2wk.auth.AuthActivity

/**
 * Any Activity that requires a UID for the current Firebase user should inherit from
 * SignedInActivity.
 *
 * The standard activity lifecycle methods are declared final to enforce that the superclass
 * methods are called. Subclasses should implement onCreateWithUser, onStartWithUser, etc.
 */
abstract class SignedInActivity : AppCompatActivity() {
    private companion object {
        val _tag = SignedInActivity::class.java.simpleName
    }

    private fun getCurrentUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser

    protected fun getUid(): String {
        val uid = getCurrentUser()?.uid
        if (uid != null) return uid

        val msg = "getUid() called when not logged in."
        Log.e(_tag, msg)
        throw RuntimeException(msg)
    }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getCurrentUser() == null) {
            Log.d(_tag, "user is null, starting up auth UI.")
            startActivity(Intent(this, AuthActivity::class.java))
        } else {
            Log.d(_tag, "user found, continuing.")
            onCreateWithUser(savedInstanceState)
        }
    }

    protected open fun onCreateWithUser(savedInstanceState: Bundle?) {}

    final override fun onStart() {
        super.onStart()
        onStartWithUser()
    }

    protected open fun onStartWithUser() {}

    final override fun onResume() {
        super.onResume()
        onResumeWithUser()
    }

    protected open fun onResumeWithUser() {}

    final override fun onPause() {
        super.onPause()
        onPauseWithUser()
    }

    protected open fun onPauseWithUser() {}

    final override fun onStop() {
        super.onStop()
        onStopWithUser()
    }

    protected open fun onStopWithUser() {}

    final override fun onDestroy() {
        super.onDestroy()
        onDestroyWithUser()
    }

    protected open fun onDestroyWithUser() {}
    protected fun signOut() {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener({
                    startActivity(Intent(this, this::class.java))
                })
    }
}