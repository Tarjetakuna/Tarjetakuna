package com.github.sdp.tarjetakuna.ui.authentication

/**
 * Interface for authentication.
 */
interface Authenticator {
    fun isUserLoggedIn(): Boolean

    fun getUserUID(): String?

    fun getUserDisplayName(): String?

}
