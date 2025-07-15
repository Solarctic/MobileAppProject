package com.example.movieapp

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.api.RetrofitClient
import com.example.model.User
import com.example.movieapp.ui.theme.BoldFunny
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Bundle


val bodyBold = androidx.compose.ui.text.TextStyle(
    fontFamily = BoldFunny,
    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
    fontSize = 28.sp,
    color = Color.White
)

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LoginScreen(
                    onLoginSuccess = { userId ->
                        val intent = Intent().apply {
                            putExtra("userId", userId)
                        }
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                )
            }
        }
    }
}


@SuppressLint("ContextCastToActivity")
@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit
) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf("") }
    var isSignUpMode by remember { mutableStateOf(false) }

    val activity = LocalContext.current as? ComponentActivity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isSignUpMode) "Sign Up" else "Login",
            style = bodyBold,
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            placeholder = { Text("Username", color = Color.Black) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                Color.Black,
                Color.Black,
                Color.Transparent,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password", color = Color.Black) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                Color.Black,
                Color.Black,
                Color.Transparent,
                Color.White,
                Color.White,
                Color.White,
                Color.White,
                Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (statusMessage.isNotEmpty()) {
            Text(
                text = statusMessage,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            onClick = {
                if (username.isEmpty() || password.isEmpty()) {
                    statusMessage = "Please enter both username and password"
                    return@Button
                }

                isLoading = true
                statusMessage = ""

                if (isSignUpMode) {
                    // Sign Up API call (POST)
                    val newUser = User(username = username, password = password)
                    RetrofitClient.apiService.registerUser(newUser)
                        .enqueue(object : Callback<User> {
                            override fun onResponse(call: Call<User>, response: Response<User>) {
                                isLoading = false
                                if (response.isSuccessful && response.body() != null) {
                                    statusMessage = "Registration successful. Please log in."
                                    isSignUpMode = false
                                } else {
                                    statusMessage = "Registration failed: ${response.message()}"
                                }
                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                isLoading = false
                                statusMessage = "Failed to register: ${t.localizedMessage}"
                            }
                        })
                } else {
                    // Login API call
                    RetrofitClient.apiService.loginUser(username, password)
                        .enqueue(object : Callback<List<User>> {
                            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                                isLoading = false
                                val users = response.body()

                                if (response.isSuccessful && !users.isNullOrEmpty()) {
                                    val userId = users[0].id.toString()
                                    onLoginSuccess(userId)
                                } else {
                                    statusMessage = "Invalid username or password"
                                }
                            }

                            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                                isLoading = false
                                statusMessage = "Failed to connect: ${t.localizedMessage}"
                            }
                        })
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(if (isSignUpMode) "Sign Up" else "Login")
            }
        }

        // Toggle login/signup
        TextButton(
            onClick = { isSignUpMode = !isSignUpMode },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isSignUpMode) "Already have an account? Login" else "Don't have an account? Sign Up",
                color = Color.White
            )
        }

        // Back button
        Button(
            onClick = { activity?.finish() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Back to Start Page")
        }
    }
}

