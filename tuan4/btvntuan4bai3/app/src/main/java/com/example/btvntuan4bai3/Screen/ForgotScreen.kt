package com.example.btvntuan4bai3.Screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ForgotScreen(
    email: String?,
    otp: String?,
    pass: String?,
    onNext: (String) -> Unit
) {
    var input by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AuthLayout(
        title = "Forget Password?",
        subtitle = "Enter your Email, we will send you a verification code."
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it; error = null },
            label = { Text("Your Email") },
            singleLine = true,
            isError = error != null,
            supportingText = { error?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                if (input.contains("@")) onNext(input.trim()) else error = "Email không hợp lệ"
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Next") }

        if (email != null) {
            Spacer(Modifier.height(32.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFDCE9FB), RoundedCornerShape(24.dp))
                    .padding(24.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text("email: $email", textAlign = TextAlign.Center)
                Text("otp: $otp", textAlign = TextAlign.Center)
                Text("pass: $pass", textAlign = TextAlign.Center)
                Text("(từ màn hình 4)", textAlign = TextAlign.Center)
            }
        }
    }
}