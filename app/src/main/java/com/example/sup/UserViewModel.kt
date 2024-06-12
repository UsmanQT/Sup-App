package com.example.sup

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sup.data.User
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
}