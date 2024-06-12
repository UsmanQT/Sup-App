package com.example.sup.data

data class User(
    val id: String = "",
    val email: String = "",
    val profile_picture_url: String = "",
    val friends: List<String> = emptyList(), // Corrected: empty list for friends
    val chats: List<String> = emptyList() // Corrected: empty list for chats
)
