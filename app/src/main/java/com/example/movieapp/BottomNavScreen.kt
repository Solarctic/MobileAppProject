package com.example.movieapp

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

// Movie数据类，增加videoUrl字段，默认统一一个测试URL
data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val imageRes: Int,
    val videoUrl: String? =null
)

@Composable
fun BottomNavApp() {
    val homeScrollState = rememberScrollState()
    val categoryScrollState = rememberScrollState()
    val categoryDetailScrollState = rememberScrollState()

    var selectedItem by remember { mutableStateOf(0) }
    var selectedCategory by remember { mutableStateOf<Movie?>(null) }
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    var inCategoryDetail by remember { mutableStateOf(false) }
    var inMovieDetail by remember { mutableStateOf(false) }

    // 收藏列表
    val favoriteMovies = remember { mutableStateListOf<Movie>() }

    val categories = listOf(
        Movie(5, "Sci-Fi", "Explore futuristic worlds", R.drawable.scifi),
        Movie(6, "Comedy", "Laugh out loud", R.drawable.comedy),
        Movie(7, "Horror", "Feel the fear", R.drawable.horror),
        Movie(8, "Romance", "Fall in love", R.drawable.romance),
        Movie(9, "Action", "Nice", R.drawable.action)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = androidx.compose.ui.graphics.Color(0xFF1A1A1A),
                tonalElevation = 4.dp
            ) {
                listOf("Home", "Category", "My Page").forEachIndexed { index, item ->
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
                            // 切换tab时重置状态和页面状态
                            inCategoryDetail = false
                            inMovieDetail = false
                            selectedCategory = null
                            selectedMovie = null
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
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.TopStart
        ) {
            when (selectedItem) {
                0 -> {
                    if (inMovieDetail && selectedMovie != null) {
                        MovieDetailPage(
                            movie = selectedMovie!!,
                            onBack = { inMovieDetail = false },
                            favoriteMovies = favoriteMovies
                        )
                    } else {
                        HomePage(
                            scrollState = homeScrollState,
                            onMovieClick = { movie ->
                                selectedMovie = movie
                                inMovieDetail = true
                            }
                        )
                    }
                }
                1 -> {
                    when {
                        inMovieDetail && selectedMovie != null -> {
                            MovieDetailPage(
                                movie = selectedMovie!!,
                                onBack = { inMovieDetail = false },
                                favoriteMovies = favoriteMovies
                            )
                        }
                        inCategoryDetail && selectedCategory != null -> {
                            CategoryDetailPage(
                                category = selectedCategory!!,
                                onMovieClick = { movie ->
                                    selectedMovie = movie
                                    inMovieDetail = true
                                },
                                onBack = {
                                    inCategoryDetail = false
                                    selectedCategory = null
                                },
                                scrollState = categoryDetailScrollState
                            )
                        }
                        else -> {
                            CategoryPage(
                                categories = categories,
                                onCategoryClick = { category ->
                                    selectedCategory = category
                                    inCategoryDetail = true
                                },
                                scrollState = categoryScrollState
                            )
                        }
                    }
                }
                2 -> {
                    MyPage(
                        favoriteMovies = favoriteMovies,
                        onMovieClick = { movie ->
                            selectedMovie = movie
                            inMovieDetail = true
                            selectedItem = 0  // 跳转到详情页面所在的Tab页
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HomePage(scrollState: androidx.compose.foundation.ScrollState, onMovieClick: (Movie) -> Unit) {
    val movies = listOf(
        Movie(1, "Oppenheimer",
            "He unleashed the power of the universe—and couldn't control what came next.",
            R.drawable.img1,
            "https://www.youtube.com/watch?v=1Fg5iWmQjwk"),
        Movie(2, "The last of us, Part 1", "When the world ends, what’s left is who we are.", R.drawable.img2,"https://www.youtube.com/watch?v=arIiRPOky80"),
        Movie(3, "3-body problem", "When science meets the unknown, humanity faces extinction—or evolution.", R.drawable.img3,"https://www.youtube.com/watch?v=P4AJS0-Lhf4"),
        Movie(4, "Interstellar", "Love transcends time. Hope defies gravity.", R.drawable.img4,"https://www.youtube.com/watch?v=xXOw_zbwew0")
    )
    MovieListSection(title = "Featured", movies = movies, onMovieClick = onMovieClick, scrollState = scrollState)
}

@Composable
fun CategoryPage(
    categories: List<Movie>,
    onCategoryClick: (Movie) -> Unit,
    scrollState: androidx.compose.foundation.ScrollState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Categories", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(bottom = 16.dp))
        categories.forEach { category ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCategoryClick(category) },
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = category.imageRes),
                        contentDescription = category.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(80.dp).padding(end = 16.dp)
                    )
                    Text(
                        text = category.title,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryDetailPage(
    category: Movie,
    onMovieClick: (Movie) -> Unit,
    onBack: () -> Unit,
    scrollState: androidx.compose.foundation.ScrollState
) {
    val movies = when (category.title) {
        "Sci-Fi" -> listOf(
            Movie(101, "Back to the future", "Time travel never goes according to plan.", R.drawable.back_to_the_future_scifi,"https://www.youtube.com/watch?v=haSbQV3dVu8"),
            Movie(102, "Blade Runner", "They were built to serve. They chose to survive.", R.drawable.blade_runner_scifi,"https://www.youtube.com/watch?v=HJI6GRctGtY"),
            Movie(103, "Interstellar", "Love transcends time. Hope defies gravity.", R.drawable.img4,"https://www.youtube.com/watch?v=xXOw_zbwew0"),
            Movie(104, "3-body problem", "When science meets the unknown, humanity faces extinction—or evolution.", R.drawable.img3,"https://www.youtube.com/watch?v=P4AJS0-Lhf4"),
        )
        "Comedy" -> listOf(
            Movie(201, "The Grand Budapest Hotel", "Check in for the charm. Stay for the chaos.", R.drawable.budapest,"https://www.youtube.com/watch?v=1Fg5iWmQjwk"),
            Movie(202, "Rush hour", "Two cops. One city. Zero chill.", R.drawable.rushhour,"https://www.youtube.com/watch?v=qQNKZOFkV7I"),
            Movie(203, "Police Story", "One cop. One city. Zero tolerance.", R.drawable.policestory,"https://www.youtube.com/watch?v=JkuL2n418n8"),
            Movie(204, "Kung fu panda", "Every panda has his day.", R.drawable.panda,"https://www.youtube.com/watch?v=8y9QnS_tMkY")
        )
        "Horror" -> listOf(
            Movie(301, "The Ring", "Watch it once. Die seven days later.", R.drawable.ring_horror,"https://www.youtube.com/watch?v=_nMQ5suNlrA"),
            Movie(302, "Saw", "Trapped. Tested. Tormented.", R.drawable.saw_horror,"https://www.youtube.com/watch?v=EgJGlbq24Mo"),
            Movie(303, "The shinning", "Some places are better left alone.", R.drawable.the_shining_horror,"https://www.youtube.com/watch?v=o6l48mT7AI0"),
            Movie(304, "The exorcist", "Some battles are fought beyond this world.", R.drawable.the_exorcist_horror,"https://www.youtube.com/watch?v=-DwxgXsS1rA")
        )
        "Romance" -> listOf(
            Movie(401, "Titanic", "Nothing on Earth could come between them.", R.drawable.titanic_romance,"https://www.youtube.com/watch?v=ASwUYKY1Ang"),
            Movie(402, "La la land", "Dreams are calling. Will you answer?", R.drawable.lalaland_romance,"https://www.youtube.com/watch?v=K8O0QbusfU8"),
            Movie(403, "Roman holiday", "A princess. A city. A day to remember forever.", R.drawable.roman_holiday_romance,"https://www.youtube.com/watch?v=_k5n3gCB9-c"),
            Movie(404, "Pretty woman", "She’s not your typical fairy tale.", R.drawable.pretty_woman_romance,"https://www.youtube.com/watch?v=-llInsEagh4")
        )
        "Action" -> listOf(
            Movie(501, "John wick", "They took everything from him. Now he’s coming for more.", R.drawable.john_wick_action,"https://www.youtube.com/watch?v=HMiRde-DfM4"),
            Movie(502, "The matrix", "What if everything you know is a lie?", R.drawable.the_matrix_action,"https://www.youtube.com/watch?v=gDAenrA_w4E"),
            Movie(504, "Predator", "If it bleeds, we can kill it.", R.drawable.predator_action,"https://www.youtube.com/watch?v=eQg_btC0prY"),
            Movie(504, "Die hard", "Yippee-ki-yay, motherf**er.", R.drawable.die_hard_action,"https://www.youtube.com/watch?v=hIZlvaj7jms")
        )
        else -> emptyList()
    }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Button(onClick = onBack) { Text("Back to Categories") }
        Spacer(Modifier.height(16.dp))
        MovieListSection(title = "${category.title} Movies", movies = movies, onMovieClick = onMovieClick, scrollState = scrollState)
    }
}

@Composable
fun MovieListSection(
    title: String,
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    scrollState: androidx.compose.foundation.ScrollState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(title, style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(bottom = 8.dp))
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
                modifier = Modifier.fillMaxWidth().height(180.dp)
            )
            Text(
                movie.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun MovieDetailPage(
    movie: Movie,
    onBack: () -> Unit,
    favoriteMovies: MutableList<Movie>
) {
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(favoriteMovies.contains(movie)) }

    Box(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Button(onClick = onBack) { Text("Back") }
            Spacer(modifier = Modifier.height(16.dp))
            Text(movie.title, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(movie.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = movie.imageRes),
                contentDescription = "Movie Poster",
                modifier = Modifier.fillMaxWidth().height(300.dp),
                contentScale = ContentScale.Crop
            )
        }

        // 右上角 收藏按钮
        IconButton(
            onClick = {
                if (isFavorite) {
                    favoriteMovies.remove(movie)
                } else {
                    favoriteMovies.add(movie)
                }
                isFavorite = !isFavorite
            },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            if (isFavorite) {
                Icon(Icons.Filled.Favorite, contentDescription = "Unfavorite", tint = androidx.compose.ui.graphics.Color.Red)
            } else {
                Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Favorite")
            }
        }

        // 顶部中间的Click按钮（缩小尺寸）
        movie.videoUrl?.let { url ->
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
                    .width(100.dp)
                    .height(36.dp)
            ) {
                Text("Click", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun MyPage(favoriteMovies: List<Movie>, onMovieClick: (Movie) -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("My Page", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // 🔴 Login Button
        Button(
            onClick = {
                context.startActivity(Intent(context, LoginActivity::class.java))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE50914),  // Netflix red
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (favoriteMovies.isEmpty()) {
            Text("No favorite movies yet.")
        } else {
            favoriteMovies.forEach { movie ->
                MoviePoster(movie = movie, onClick = onMovieClick)
            }
        }
    }
}