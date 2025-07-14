package com.example.moviesapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movieapp.R

// Movie data class
data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val imageRes: Int,
    val summary:String,
    val shortMovie:String
)

data class Category(
    val id: Int,
    val category: String,
    val imageRes: Int
)

@Composable
fun BottomNavApp() {
    var selectedItem by remember { mutableStateOf(0) }
    var showDetail by remember { mutableStateOf(false) }
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }

    val items = listOf("Home", "Category", "My Page")

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
                        onClick = {
                            selectedItem = index
                            showDetail = false
                        },
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
                0 -> {
                    if (showDetail && selectedMovie != null) {
                        MovieDetailPage(movie = selectedMovie!!, onBack = { showDetail = false })
                    } else {
                        HomePage(onMovieClick = {
                            selectedMovie = it
                            showDetail = true
                        })
                    }
                }
                1 -> CategoryPage()
                2 -> MyPage()
            }
        }
    }
}


@Composable
fun MyPage() {
    Text(
        "My Page",
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(24.dp)
    )
}



@Composable
fun HomePage(onMovieClick: (Movie) -> Unit) {
    val movies = listOf(
        Movie(1, "Oppenheimer", "A classic by Christopher Nolan", R.drawable.img1,
            "A dramatization of the life story of J. Robert Oppenheimer, the physicist who had a large hand in the development of the atomic bomb, thus helping end World War 2. We see his life from university days all the way to post-WW2, where his fame saw him embroiled in political machinations.",
            "https://youtu.be/hd93UMcbebA?feature=shared"),
        Movie(2, "The last of us, Part 1", "Famous video game adaptation", R.drawable.img2,
            "sample link",
            "link"),
        Movie(3, "3 body problem", "SF by creaters of 'Game of Thrones'", R.drawable.img3,
            "sample link",
            "link"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Featured",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        movies.forEach { movie ->
            MoviePoster(movie = movie, onClick = onMovieClick)
        }
    }
}

@Composable
fun MoviePoster(movie: Movie, onClick: (Movie) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(movie) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = movie.imageRes),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )


        }
    }

}

@Composable
fun CategoryItem(category: Category, onClick: (Category) -> Unit)
{
    Card(modifier = Modifier
        .fillMaxSize()
        .clickable { onClick(category) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp))
    {
        Column {
            Image(
                painter = painterResource(id = category.imageRes),
                contentDescription = category.category,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Text(
                text = category.category,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun MovieDetailPage(movie: Movie, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Button(onClick = onBack) {
            Text("Back")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = movie.title, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = movie.description, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = movie.imageRes),
            contentDescription = "Movie Poster",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = movie.summary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = movie.shortMovie,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun CategoryPage() {
    val categories = listOf(
        Category(1, "Action", R.drawable.action_posters),
        Category(2, "Science Fiction", R.drawable.action_posters),
        Category(3, "Comedy", R.drawable.action_posters),
        Category(4, "Horror", R.drawable.action_posters),
        Category(5, "Romance", R.drawable.action_posters)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            "Category",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(8.dp)
        )
        categories.forEach { category ->
            CategoryItem(category = category, onClick = {})
        }

    }
}


