package com.github.sdp.tarjetakuna.model

class CurrentUser {
    companion object {
        private var currentUser: User? = null

        fun getCurrentUser(): User? {
            return currentUser
        }

        fun setCurrentUser(user: User) {
            currentUser = user
        }

        fun removeCurrentUser() {
            currentUser = null
        }

        fun isUserLoggedIn(): Boolean {
            return currentUser != null
        }
    }
}
