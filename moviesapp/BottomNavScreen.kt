package com.example.moviesapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List

import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavApp() {
    var selectedItem by remember { mutableStateOf(0) }

    val items = listOf("Home", "Category")

    Scaffold(
        bottomBar = {
            NavigationBar {
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
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (selectedItem) {
                0 -> HomePage()
                1 -> CategoryPage()
            }
        }
    }
}

@Composable
fun HomePage() {
    Text("This is the Home Page", fontSize = 24.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun CategoryPage() {
    Text("This is the Category Page", fontSize = 24.sp, fontWeight = FontWeight.Bold)
}
