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
        }
    }.start(wait = true)
}

data class LoginRequest(val username: String, val password: String)
data class User(val id: Int, val username: String, val email: String)
data class LoginResponse(val success: Boolean, val user: User?)

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
}
