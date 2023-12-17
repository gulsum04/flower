package com.demir.starter.manager

import com.demir.starter.data.Basket
import com.demir.starter.data.UserProfile
import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthManager {
    private val mAuth get() = FirebaseAuth.getInstance()
    var currentUser: UserProfile? = null

    fun registerUser(mail: String, password: String, callback: (e: Exception?) -> Unit) {
        mAuth.createUserWithEmailAndPassword(mail, password)
            .addOnSuccessListener { authResult ->
                val userProfile = authResult.user!!.let {
                    UserProfile().apply {
                        email = it.email!!
                    }
                }
                FirestoreManager.saveUserProfile(authResult.user!!.email!!, userProfile) {
                    if (it != null) {
                        callback(it)
                    } else {
                        callback(null)
                    }
                }
            }
            .addOnFailureListener { e ->
                callback(e)
            }
    }

    fun loginUser(email: String, password: String, callback: (e: Exception?) -> Unit) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                callback(null)

                FirestoreManager.getUserProfile(mAuth.currentUser!!.email!!) {
                    currentUser = it
                }
            }
            .addOnFailureListener { e ->
                callback(e)
            }
    }

    fun logout() {
        currentUser = null
        mAuth.signOut()

        Basket.clear()
    }
}