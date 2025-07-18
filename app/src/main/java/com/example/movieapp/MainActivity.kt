package com.example.movieapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.movieapp.ui.theme.MoviesAppTheme
import com.example.movieapp.ui.theme.StartPageFullImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesAppTheme {
                var showStartPage by remember { mutableStateOf(true) }

                if (showStartPage) {
                    StartPageFullImage(onStartClicked = { showStartPage = false })
                } else {
                     BottomNavApp()
                }
            }
        }

    }
}