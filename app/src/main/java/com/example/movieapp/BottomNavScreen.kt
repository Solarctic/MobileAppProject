package com.example.movieapp

import android.graphics.RenderEffect
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RenderEffect.*
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun BottomNavApp() {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Home", "Category")

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = androidx.compose.ui.graphics.Color(0xFF1A1A1A),
                tonalElevation = 4.dp
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            when (item) {
                                "Home" -> Icon(Icons.Filled.Home, contentDescription = "Home")
                                "Category" -> Icon(Icons.Filled.List, contentDescription = "Category")
                            }
                        },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = androidx.compose.ui.graphics.Color(0xFFE50914),
                            selectedTextColor = androidx.compose.ui.graphics.Color(0xFFE50914),
                            indicatorColor = androidx.compose.ui.graphics.Color(0xFF330000)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopStart
        ) {
            when (selectedItem) {
                0 -> HomePage()
                1 -> CategoryPage()
            }
        }
    }
}

@Composable
fun StartPageFullImage(onStartClicked: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {

        // 背景图全屏
        Image(
            painter = painterResource(id = R.drawable.img1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 模糊 + 半透明背景层
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(24.dp)
                .graphicsLayer {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        renderEffect = RenderEffect.
                        createBlurEffect(30f, 30f, android.graphics.Shader.TileMode.CLAMP).asComposeRenderEffect()
                    }
                }
                .background(Color(0xAA000000), shape = RoundedCornerShape(16.dp)) // 半透明黑底
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Your cinema adventure\nbegins here!",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onStartClicked,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .width(200.dp)
                        .height(48.dp)
                ) {
                    Text(
                        text = "Start",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

@Composable
fun StartPage(onStartClicked: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onStartClicked
        ) {
            Text(text = "Start", style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
fun HomePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Featured",
            style = androidx.compose.material3.MaterialTheme.typography.headlineLarge
        )

    }
}

@Composable
fun CategoryPage() {
    Text(
        "Category",
        style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
        color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(24.dp)
    )

}
