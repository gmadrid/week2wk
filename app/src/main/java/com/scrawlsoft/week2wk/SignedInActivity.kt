package com.scrawlsoft.week2wk

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Any Activity that requires a UID for the current Firebase user should inherit from
 * SignedInActivity.
 *
 * The standard activity lifecycle methods are declared final to enforce that the superclass
 * methods are called. Subclasses should implement onCreateWithUser, onStartWithUser, etc.
 */
abstract class SignedInActivity : AppCompatActivity() {

    private val _tag = this.javaClass.simpleName

    private fun getCurrentUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser
    protected fun getUid(): String {
        return getCurrentUser()!!.uid
    }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        val mSignInClient = GoogleSignIn.getClient(this, gso)

        val user = getCurrentUser()
        if (user == null) {
            startActivityForResult(mSignInClient.signInIntent, ResultCodes.SIGN_IN)
        }

        onCreateWithUser(savedInstanceState)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ResultCodes.SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.w(_tag, "Google sign in failed: ", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(_tag, "firebaseAuthWithGoogle: ${acct.id!!}")

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(_tag, "signInWithCredential:success")
                        val user = FirebaseAuth.getInstance().currentUser
                        //updateUI(user)
                        Snackbar.make(main_container, "Logged in as user: ${user.toString()}", Snackbar.LENGTH_LONG).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(_tag, "signInWithCredential:failure", task.exception)
                        Snackbar.make(main_container, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                        //updateUI(null)
                    }

                    // ...
                })
    }
}