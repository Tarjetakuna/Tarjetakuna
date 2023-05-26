package com.github.sdp.tarjetakuna.ui.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * Adapter for Google authentication. Information about the potentially currently signed in user.
 */
object GoogleAuthAdapter : Authenticator {
    var auth: FirebaseAuth = Firebase.auth

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun getUserUID(): String? {
        return auth.currentUser?.uid
    }

    override fun getUserDisplayName(): String? {
        return auth.currentUser?.displayName
    }
}
