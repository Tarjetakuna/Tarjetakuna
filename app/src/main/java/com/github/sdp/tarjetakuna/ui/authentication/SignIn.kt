package com.github.sdp.tarjetakuna.ui.authentication

object SignIn {

    private var signIn: Authenticator = GoogleAuthAdapter

    fun getSignIn(): Authenticator {
        return signIn
    }

    fun setSignIn(newSignIn: Authenticator) {
        signIn = newSignIn
    }
}
