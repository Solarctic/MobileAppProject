package com.example.movieapp.ktor

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.gson.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.*

// Data classes
data class LoginRequest(val username: String, val password: String)
data class User(val id: Int, val username: String, val email: String)
data class LoginResponse(val success: Boolean, val user: User?)
data class TagMovieRequest(val userId: Int, val movieId: Int, val tag: String)
data class Movie(val id: Int, val title: String, val description: String?, val releaseYear: Int?)

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) {
            gson()
        }
        routing {
            post("/login") {
                val loginRequest = call.receive<LoginRequest>()
                val user = Database.loginUser(loginRequest.username, loginRequest.password)
                if (user != null) {
                    call.respond(LoginResponse(success = true, user = user))
                } else {
                    call.respond(LoginResponse(success = false, user = null))
                }
            }

            post("/addTagToMovie") {
                val request = call.receive<TagMovieRequest>()
                Database.addTagToMovie(request.userId, request.movieId, request.tag)
                call.respond(mapOf("message" to "Tag added"))
            }

            get("/moviesByTag/{userId}/{tag}") {
                val userId = call.parameters["userId"]?.toIntOrNull()
                val tag = call.parameters["tag"]

                if (userId != null && tag != null) {
                    val movies = Database.getMoviesByTag(userId, tag)
                    call.respond(movies)
                } else {
                    call.respond(mapOf("error" to "Invalid parameters"))
                }
            }
        }
    }.start(wait = true)
}

// Database access
object Database {
    private const val JDBC_URL = "jdbc:mysql://localhost:3306/your_database"
    private const val DB_USER = "root"
    private const val DB_PASSWORD = "12345"

    init {
        Class.forName("com.mysql.cj.jdbc.Driver")
    }

    fun loginUser(username: String, password: String): User? {
        val connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)
        val statement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")
        statement.setString(1, username)
        statement.setString(2, password)
        val result = statement.executeQuery()
        val user = if (result.next()) {
            User(
                id = result.getInt("id"),
                username = result.getString("username"),
                email = result.getString("email")
            )
        } else null
        result.close()
        statement.close()
        connection.close()
        return user
    }

    fun addTagToMovie(userId: Int, movieId: Int, tagName: String) {
        val connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)
        connection.use {
            // Insert tag if it doesn't exist
            val tagStmt = it.prepareStatement("INSERT IGNORE INTO tags (name) VALUES (?)")
            tagStmt.setString(1, tagName)
            tagStmt.executeUpdate()
            tagStmt.close()

            // Retrieve tag ID
            val getTagIdStmt = it.prepareStatement("SELECT id FROM tags WHERE name = ?")
            getTagIdStmt.setString(1, tagName)
            val tagResult = getTagIdStmt.executeQuery()
            if (!tagResult.next()) return
            val tagId = tagResult.getInt("id")
            tagResult.close()
            getTagIdStmt.close()

            // Map user, movie, and tag
            val insertMapStmt = it.prepareStatement(
                "INSERT IGNORE INTO user_movie_tags (user_id, movie_id, tag_id) VALUES (?, ?, ?)"
            )
            insertMapStmt.setInt(1, userId)
            insertMapStmt.setInt(2, movieId)
            insertMapStmt.setInt(3, tagId)
            insertMapStmt.executeUpdate()
            insertMapStmt.close()
        }
    }

    fun getMoviesByTag(userId: Int, tag: String): List<Movie> {
        val connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)
        val movies = mutableListOf<Movie>()
        connection.use {
            val stmt = it.prepareStatement(
                """
                SELECT m.id, m.title, m.description, m.release_year
                FROM movies m
                JOIN user_movie_tags umt ON m.id = umt.movie_id
                JOIN tags t ON umt.tag_id = t.id
                WHERE umt.user_id = ? AND t.name = ?
                """
            )
            stmt.setInt(1, userId)
            stmt.setString(2, tag)
            val result = stmt.executeQuery()
            while (result.next()) {
                movies.add(
                    Movie(
                        id = result.getInt("id"),
                        title = result.getString("title"),
                        description = result.getString("description"),
                        releaseYear = result.getInt("release_year")
                    )
                )
            }
            result.close()
            stmt.close()
        }
        return movies
    }
}
