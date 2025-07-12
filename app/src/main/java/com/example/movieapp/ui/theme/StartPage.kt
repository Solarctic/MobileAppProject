// StartPage.kt
package com.example.movieapp.ui.theme

import android.graphics.RenderEffect
import android.graphics.Shader
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.asComposeRenderEffect
import com.example.movieapp.R


val bodyBold = androidx.compose.ui.text.TextStyle(
    fontFamily = BoldFunny,
    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
    fontSize = 28.sp,
    color = Color.White
)

@androidx.annotation.RequiresApi(android.os.Build.VERSION_CODES.S)
@Composable
fun StartPageFullImage(onStartClicked: () -> Unit) {
    var pageIndex by remember { mutableStateOf(0) }
    val totalPages = 3

    val bgImages = listOf(
        R.drawable.img1,
        R.drawable.img2,
        R.drawable.img3
    )

    val pageTexts = listOf(
        "Discover amazing films",
        "Your favorite genres",
        "Ready to start?"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Background image - no blur here
        Image(
            painter = painterResource(id = bgImages[pageIndex]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Bottom container with blurred background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(24.dp)
        ) {
            // 2.1 Blur layer under the content (text + button)
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        renderEffect = RenderEffect
                            .createBlurEffect(30f, 30f, Shader.TileMode.CLAMP)
                            .asComposeRenderEffect()
                    }
                    .background(Color.Black.copy(alpha = 0.8f))
            )

            // 2.2 Foreground content (text and button)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = pageTexts[pageIndex],
                    style = bodyBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Button(
                    onClick = {
                        if (pageIndex < totalPages - 1) {
                            pageIndex++
                        } else {
                            onStartClicked()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(text = if (pageIndex < totalPages - 1) "Next" else "Start")
                }
            }
        }
    }
}