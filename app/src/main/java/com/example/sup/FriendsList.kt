package com.example.sup

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import com.example.sup.data.User

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun FriendsListScreen(
    modifier: Modifier = Modifier, navController: NavController, userViewModel: UserViewModel = viewModel()){
    val users by userViewModel.users.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Users") })
        },
        content = {
            LazyColumn {
                items(users) { user ->
                    UserItem(user)
                }
            }
        }
    )
}

@Composable
fun UserItem(user: User) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
    }
}
