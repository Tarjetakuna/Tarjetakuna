package com.github.sdp.tarjetakuna.ui.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object GoogleAuthAdapter : Authenticator {
    var auth: FirebaseAuth = Firebase.auth
    var currentUser = auth.currentUser

    override fun isUserLoggedIn(): Boolean {
        return currentUser != null
    }
}
