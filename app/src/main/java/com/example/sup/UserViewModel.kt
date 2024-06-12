package com.example.sup

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sup.data.User
import com.google.firebase.auth.FirebaseAuth
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
}