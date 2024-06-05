package com.example.sup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ReceiverMessageBubbles(modifier: Modifier = Modifier) {
    Card (
        modifier = modifier
            .padding(16.dp)
    ) {
        Text(text = "hey", modifier = modifier.padding(16.dp))
    }
}

@Composable
fun SenderMessageBubbles(modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.Green
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "hey how are you doing man?")
        }
    }
}