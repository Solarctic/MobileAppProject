package com.example.movieapp

import android.annotation.SuppressLint
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
import com.example.model.LoginRequest
import com.example.model.LoginResult
import com.example.movieapp.ui.theme.BoldFunny
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val bodyBold = androidx.compose.ui.text.TextStyle(
    fontFamily = BoldFunny,
    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
    fontSize = 28.sp,
    color = Color.White
)



class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LoginScreen(
                    onLoginSuccess = { userId ->
                        Toast.makeText(this, "Login successful! UserId: $userId", Toast.LENGTH_LONG).show()
                        // TODO: Navigate to next screen here
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
    var statusMessage by remember { mutableStateOf("") } // 🆕 For showing login status

    val activity = LocalContext.current as? ComponentActivity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
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

        // 🔴 Show login error or status
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

                val loginRequest = LoginRequest(username, password)

                RetrofitClient.apiService.loginUser(loginRequest)
                    .enqueue(object : Callback<LoginResult> {
                        override fun onResponse(call: Call<LoginResult>, response: Response<LoginResult>) {
                            isLoading = false
                            val loginResult = response.body()

                            if (response.isSuccessful && loginResult?.status == "success") {
                                loginResult.userId?.let { onLoginSuccess(it) }
                            } else {
                                statusMessage = loginResult?.message ?: "Invalid username or password"
                            }
                        }

                        override fun onFailure(call: Call<LoginResult>, t: Throwable) {
                            isLoading = false
                            statusMessage = "Failed to connect to server: ${t.localizedMessage}"
                        }
                    })
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
                Text("Login")
            }
        }

        Button(
            onClick = { activity?.finish() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Back to Start Page")
        }
    }
}

