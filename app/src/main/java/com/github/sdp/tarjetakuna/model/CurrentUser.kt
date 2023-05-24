package com.github.sdp.tarjetakuna.model

class CurrentUser {
    companion object {
        private var currentUser: User? = null

        fun getCurrentUser(): User {
            return currentUser!!
        }

        fun setCurrentUser(user: User) {
            currentUser = user
            currentUser!!.addChatsListener()
        }

        fun attachChatsListener(listener: () -> Unit) {
            currentUser!!.addChatsListener(listener)
        }

        fun detachChatsListener() {
            currentUser!!.addChatsListener()
        }

        fun removeCurrentUser() {
            currentUser!!.removeChatsListener()
            currentUser = null
        }

        fun isUserLoggedIn(): Boolean {
            return currentUser != null
        }
    }
}
