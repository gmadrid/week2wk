package com.scrawlsoft.week2wk.base

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
    private val _tag = this.javaClass.simpleName

    private fun getCurrentUser(): FirebaseUser? = FirebaseAuth.getInstance().currentUser
    protected fun getUid(): String? {
        return getCurrentUser()?.uid
    }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getCurrentUser() == null) {
            startActivity(Intent(this, AuthActivity::class.java))
        } else {
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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == ResultCodes.SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)
//                firebaseAuthWithGoogle(account)
//                onSignedIn()
//            } catch (e: ApiException) {
//                Log.w(_tag, "Google sign in failed: ", e)
//            }
//        }
//    }
//
    protected open fun onSignedIn() {}
//
//    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
//        Log.d(_tag, "firebaseAuthWithGoogle: ${acct.id!!}")
//
//        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
//        FirebaseAuth.getInstance().signInWithCredential(credential)
//                .addOnCompleteListener(this, { task ->
//                    if (task.isSuccessful) {
//                        // Sign in success, update UI with the signed-in user's information
//                        Log.d(_tag, "signInWithCredential:success")
//                        val user = FirebaseAuth.getInstance().currentUser
//                        //updateUI(user)
//                        Snackbar.make(main_container, "Logged in as user: ${user.toString()}", Snackbar.LENGTH_LONG).show()
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Log.w(_tag, "signInWithCredential:failure", task.exception)
//                        Snackbar.make(main_container, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
//                        //updateUI(null)
//                    }
//
//                    // ...
//                })
//    }
}