package com.github.sdp.tarjetakuna.ui.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object GoogleAuthAdapter : Authenticator {
    var auth: FirebaseAuth = Firebase.auth

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun getUserUID(): String? {
        return auth.currentUser?.uid
    }
}
