package com.example.sup

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sup.data.FriendRequest
import com.example.sup.data.RequestStatus
import com.example.sup.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel : ViewModel() {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val userList = result.map { document ->
                    document.toObject(User::class.java).copy(id = document.id)
                }
                Log.d("FetchUsers", "Fetched users: ${userList.size}")
                for (document in result) {
                    Log.d("FetchUsers", "Document: ${document.id} => ${document.data}")
                }
                _users.value = userList
            }
            .addOnFailureListener { exception ->
                Log.e("FetchUsers", "Error fetching users", exception)
            }
    }

    fun isUserFriend(userId: String): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val currentUserId = currentUser.uid
            val currentUserData = _users.value.find { it.id == currentUserId }
            return currentUserData?.friends?.contains(userId) == true
        }
        return false
    }

    fun sendFriendRequest(sender: User, receiver: User) {
        val db = FirebaseFirestore.getInstance()

        val friendRequest = FriendRequest(sender.id, receiver.id, RequestStatus.REQUESTED)

        // Update sender's sentFriendRequests in Firestore
        db.collection("users").document(sender.id)
            .update("sentFriendRequests", FieldValue.arrayUnion(friendRequest))
            .addOnSuccessListener {
                Log.d("SendFriendRequest", "Friend request sent by ${sender.email} to ${receiver.email}")

                // Update local state for sender
                val updatedSender = _users.value.find { it.id == sender.id }?.copy(
                    sentFriendRequests = sender.sentFriendRequests + friendRequest
                )

                // Update local state for receiver
                val updatedReceiver = _users.value.find { it.id == receiver.id }?.copy(
                    receivedFriendRequests = receiver.receivedFriendRequests + friendRequest
                )

                // Update the state flow
                _users.value = _users.value.map {
                    when (it.id) {
                        sender.id -> updatedSender ?: it
                        receiver.id -> updatedReceiver ?: it
                        else -> it
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("SendFriendRequest", "Error sending friend request", e)
            }

        // Update receiver's receivedFriendRequests in Firestore
        db.collection("users").document(receiver.id)
            .update("receivedFriendRequests", FieldValue.arrayUnion(friendRequest))
            .addOnSuccessListener {
                Log.d("SendFriendRequest", "Friend request received by ${receiver.email} from ${sender.email}")
            }
            .addOnFailureListener { e ->
                Log.e("SendFriendRequest", "Error receiving friend request", e)
            }
    }



    fun acceptFriendRequest(receiver: User, senderId: String) {
        val db = FirebaseFirestore.getInstance()

        val request = receiver.receivedFriendRequests.find { it.senderId == senderId && it.status == RequestStatus.REQUESTED }
            ?: throw IllegalArgumentException("No friend request from this user")

        val updatedRequest = request.copy(status = RequestStatus.ACCEPTED)

        // Update receiver's friends and receivedFriendRequests in Firestore
        db.collection("users").document(receiver.id)
            .update("friends", FieldValue.arrayUnion(senderId))
            .addOnSuccessListener {
                Log.d("AcceptFriendRequest", "Friend request accepted by ${receiver.email}")
            }
            .addOnFailureListener { e ->
                Log.e("AcceptFriendRequest", "Error accepting friend request", e)
            }

        db.collection("users").document(receiver.id)
            .update("receivedFriendRequests", FieldValue.arrayRemove(request))
            .addOnSuccessListener {
                db.collection("users").document(receiver.id)
                    .update("receivedFriendRequests", FieldValue.arrayUnion(updatedRequest))
            }
            .addOnFailureListener { e ->
                Log.e("AcceptFriendRequest", "Error updating friend request status", e)
            }

        // Update sender's friends and sentFriendRequests in Firestore
        db.collection("users").document(senderId)
            .update("friends", FieldValue.arrayUnion(receiver.id))
            .addOnSuccessListener {
                Log.d("AcceptFriendRequest", "Friend added to ${senderId}")
            }
            .addOnFailureListener { e ->
                Log.e("AcceptFriendRequest", "Error adding friend", e)
            }

        db.collection("users").document(senderId)
            .update("sentFriendRequests", FieldValue.arrayRemove(request))
            .addOnSuccessListener {
                db.collection("users").document(senderId)
                    .update("sentFriendRequests", FieldValue.arrayUnion(updatedRequest))
            }
            .addOnFailureListener { e ->
                Log.e("AcceptFriendRequest", "Error updating friend request status", e)
            }
    }
    fun getFriendRequestStatus(userId: String, currentUserId: String, callback: (RequestStatus?) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(currentUserId).get()
            .addOnSuccessListener { document ->
                val currentUser = document.toObject(User::class.java)
                val receivedRequest = currentUser?.receivedFriendRequests?.find { it.senderId == userId }
                val sentRequest = currentUser?.sentFriendRequests?.find { it.receiverId == userId }

                callback(receivedRequest?.status ?: sentRequest?.status)
            }
            .addOnFailureListener { exception ->
                Log.e("getFriendRequestStatus", "Error fetching user data", exception)
                callback(null)
            }
    }


}