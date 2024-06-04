package com.example.sup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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

@Composable
fun SupAppLayout() {
    val backgroundImage = painterResource(id = R.drawable.whatsapp_background)
    Image(
        painter = backgroundImage,
        contentDescription = null,
        contentScale = ContentScale.FillBounds)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SupTheme {
        SupAppLayout()
    }
}