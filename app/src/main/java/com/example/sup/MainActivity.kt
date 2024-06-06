package com.example.sup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.sharp.AccountBox
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.Call
import androidx.compose.material.icons.sharp.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sup.components.ReceiverMessageBubbles
import com.example.sup.components.SenderMessageBubbles
import com.example.sup.ui.theme.SupTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SupTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SupAppLayout()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupAppLayout(modifier: Modifier = Modifier) {
    val backgroundImage = painterResource(id = R.drawable.whatsapp_background)

    var mutableListSender by remember {
        mutableStateOf(mutableListOf<String>())
    }

    Scaffold (
        topBar = {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        Icons.Sharp.ArrowBack,
                        "back button",
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        Icons.Sharp.AccountCircle,
                        "account circle",
                    )
                }
                TopAppBar(
                    modifier = modifier.weight(3f),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text("SupApp", fontWeight = FontWeight.Bold)
                    }
                )
                IconButton(
                    modifier = modifier.weight(1f),
                    onClick = { /*TODO*/ }) {
                    Icon(

                        Icons.Sharp.Call,
                        "call button",
                    )
                }
            }
        },
    ){ innerPadding ->
        Box (modifier = modifier.padding(innerPadding)){
            Image(
                painter = backgroundImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier.fillMaxSize(), // Occupy entire available space
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start // Align content center horizontally
            ) {
                ReceiverMessageBubbles()
                Column(
                    modifier = Modifier.fillMaxSize(), // Occupy entire available space
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.End // Align content center horizontally
                ) {
                    SenderMessageBubbles(messageList = mutableListSender)
                }
            }
            MessageWritingArea(modifier = modifier) { newMessage ->
                mutableListSender = (mutableListSender + newMessage).toMutableList()
            }
        }
    }
}

@Composable
fun MessageWritingArea(modifier: Modifier =  Modifier, onMessageAdded: (String) -> Unit) {
    var message by remember {
        mutableStateOf("")
    }
    
    var textFieldEmpty by remember {
        mutableStateOf(true)
    }

    Box (modifier = modifier
        .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter) {
        Card(modifier = modifier) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /*TODO*/ },
                ) {
                    Icon(
                        Icons.Sharp.Add,
                        "add document",
                        modifier = modifier.size(30.dp)
                    )

                }
                TextField(
                    value = message , onValueChange = {
                        message = it
                        if (message.isNotEmpty()) {
                            textFieldEmpty = false
                        }
                        else {
                            textFieldEmpty = true
                        }
                                                      },
                    textStyle = TextStyle(color = Color.Black),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .weight(3f)// Fill the available width
                        .padding(vertical = 10.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0f),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    )
                )
                when(textFieldEmpty) {
                    true -> Row {
                        IconButton(
                            onClick = { /*TODO*/ },
                        ) {
                            Icon(
                                Icons.Sharp.AccountBox,
                                "add document",
                                modifier = modifier.size(30.dp)
                            )

                        }
                        IconButton(
                            onClick = { /*TODO*/ },
                        ) {
                            Icon(
                                Icons.Sharp.Call,
                                "add document",
                                modifier = modifier.size(25.dp)
                            )

                        }
                    }
                    false -> IconButton(
                        onClick = {
                            onMessageAdded(message)
                        },
                    ) {
                        Icon(
                            Icons.Sharp.Send,
                            "add document",
                            modifier = modifier.size(25.dp)
                        )

                    }
                }
                
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SupTheme {
        SupAppLayout()
    }
}