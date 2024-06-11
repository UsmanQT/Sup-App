package com.example.sup

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable

fun SignInScreen(modifier: Modifier = Modifier, navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    var emailText by remember {
        mutableStateOf("")
    }
    var passwordText by remember {
        mutableStateOf("")
    }
    Scaffold {
        innerPadding -> Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
        ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(
                text = "Sign In",
                modifier = modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = modifier.padding(50.dp))
            Button(onClick = { navController.navigate("sign-up")}) {
                Text(text = "Sign Up")
            }
        }
            OutlinedTextField(
                value = emailText,
                onValueChange = {emailText = it},
                label = {
                    Text(
                        text = "Email", color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                modifier = modifier.padding(bottom = 16.dp)

            )
            OutlinedTextField(
                value = passwordText,
                onValueChange = {passwordText = it},
                label = {
                    Text(
                        text = "Password", color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = modifier.padding(bottom = 16.dp)
                )
            Button(onClick = {
                if(emailText.isNotEmpty() && passwordText.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Sign up success, navigate to the next screen
                                navController.navigate("chat-screen")
                            } else {
                                // If sign up fails, display a message to the user
                                Toast.makeText(context, "Sign Up Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                else {
                    Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(text = "Sign In")
             }
        }
    }
}