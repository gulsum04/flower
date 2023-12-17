package com.demir.starter.data

class UserProfile {
    var fullName: String? = null
    var email: String? = null
    var phoneNumber: String? = null

    fun toMap(): Map<String, String?> {
        val map = mutableMapOf<String, String?>()
        map["fullName"] = fullName
        map["email"] = email
        map["phoneNumber"] = phoneNumber
        return map
    }
}