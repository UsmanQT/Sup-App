package com.example.sup

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sup.data.User
import com.google.firebase.auth.FirebaseAuth
import com.example.sup.data.FriendRequest
import com.example.sup.data.RequestStatus

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun FriendsListScreen(
    modifier: Modifier = Modifier, navController: NavController, userViewModel: UserViewModel = viewModel()){
    val users by userViewModel.users.collectAsState()

    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("SupApp Users") })
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {
                items(users.filter { it.id != currentUser?.uid }) { user ->
                    val isFriend = userViewModel.isUserFriend(user.id)
                    var requestStatus by remember { mutableStateOf<RequestStatus?>(null) }

                    LaunchedEffect(user.id) {
                        currentUser?.uid?.let {
                            userViewModel.getFriendRequestStatus(user.id, it) { status ->
                                requestStatus = status
                            }
                        }
                    }

                    UserItem(user, isFriend, { selectedUser ->
                        currentUser?.let {
                            val email = it.email ?: "unknown@example.com"
                            userViewModel.sendFriendRequest(User(it.uid, email), selectedUser) }
                    }, requestStatus)
                }
            }
        }
    )
}

@Composable
fun UserItem(user: User, isFriend: Boolean, onAddFriendClick: (User) -> Unit, requestStatus: RequestStatus?) {
    Column(
        modifier = Modifier.padding(16.dp)
            .fillMaxWidth()
    ) {
        Row (
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
            Button(
                onClick = { onAddFriendClick(user) },
                modifier = Modifier.height(30.dp),
                enabled = !isFriend && requestStatus != RequestStatus.REQUESTED
            ) {
                Text(
                    text = when (requestStatus) {
                        RequestStatus.REQUESTED -> "Requested"
                        RequestStatus.ACCEPTED -> "Friend"
                        RequestStatus.REJECTED -> "Rejected"
                        else -> if (isFriend) "Friend" else "Add friend"
                    },
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}


