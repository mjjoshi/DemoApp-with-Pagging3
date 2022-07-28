package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


/**
 * add google sign with firebase
 */
class LoginViewmodel(app: Application) : AndroidViewModel(app) {

    private var auth: FirebaseAuth = Firebase.auth
    val currentUserName = MutableLiveData<FirebaseUser>()
    val currentUser: LiveData<FirebaseUser> = currentUserName

    val name = MutableLiveData<String>()

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(app.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(app, gso)

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                currentUserName.value = auth.currentUser
            }
        }
    }

    fun logout() {
        auth.signOut()
        googleSignInClient.signOut()
        name.value = ""
    }


}