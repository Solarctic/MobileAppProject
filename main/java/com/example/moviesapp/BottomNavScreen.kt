package com.example.moviesapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavApp() {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Home", "Category","My Page")

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
                                "My Page" -> Icon(Icons.Filled.Person, contentDescription = "My Page")
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
                2 -> MyPage()

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

@Composable
fun MyPage() {
    Text(
        "My page",
        style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
        color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(24.dp)
    )

}