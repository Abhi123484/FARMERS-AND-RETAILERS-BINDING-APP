package com.farmers.retailers.app.data

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val state: String = "",
    val district: String = "",
    val taluk: String = "",
    val phoneNumber: String = "",
    val role: String = "" // "farmer" or "retailer"
)