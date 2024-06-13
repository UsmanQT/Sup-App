package com.example.sup.data

enum class RequestStatus {
    REQUESTED,
    ACCEPTED,
    REJECTED
}

data class FriendRequest(
    val senderId: String = "",
    val receiverId: String = "",
    val status: RequestStatus = RequestStatus.REQUESTED
)

data class User(
    val id: String = "",
    val email: String = "",
    val profile_picture_url: String = "",
    val friends: List<String> = emptyList(),
    val chats: List<String> = emptyList(),
    val receivedFriendRequests: List<FriendRequest> = emptyList(),
    val sentFriendRequests: List<FriendRequest> = emptyList()
)