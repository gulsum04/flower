package com.demir.starter.manager

import com.demir.starter.data.Product
import com.demir.starter.data.UserProfile
import com.google.firebase.firestore.FirebaseFirestore


object FirestoreManager {
    private const val COLLECTION_USERS = "users"
    private const val COLLECTION_FLOWERS = "flowers"
    private val db get() = FirebaseFirestore.getInstance()
    val products = mutableListOf<Product>()

    fun saveUserProfile(userId: String, userProfile: UserProfile, callback: (Exception?) -> Unit) {
        db.collection(COLLECTION_USERS)
            .document(userId)
            .set(userProfile)
            .addOnSuccessListener {
                callback(null)
            }
            .addOnFailureListener {
                callback(it)
            }
    }

    fun getUserProfile(userId: String, callback: (UserProfile?) -> Unit) {
        db.collection(COLLECTION_USERS)
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val userProfile = documentSnapshot.toObject(UserProfile::class.java)
                callback(userProfile)
            }
            .addOnFailureListener {
                it.printStackTrace()
                callback(null)
            }
    }

    fun updateUserProfile(userId: String, userProfile: UserProfile) {
        val userReference = db.collection(COLLECTION_USERS).document(userId)
        val userMap = userProfile.toMap()

        userReference.set(userMap)
            .addOnSuccessListener {}
            .addOnFailureListener { }
    }

    fun getFlowers(callback: (List<Product>?) -> Unit) {
        db.collection(COLLECTION_FLOWERS)
            .get()
            .addOnSuccessListener { documents ->
                val mutableList = mutableListOf<Product>()
                for (i in documents) {
                    mutableList.add(i.toObject(Product::class.java))
                }
                callback(mutableList)
                products.clear()
                products.addAll(mutableList)
            }
            .addOnFailureListener {
                it.printStackTrace()
                callback(null)
            }
    }
}