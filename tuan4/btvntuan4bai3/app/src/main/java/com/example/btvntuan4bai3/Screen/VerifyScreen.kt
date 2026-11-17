package com.example.btvntuan4bai3.Screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun VerifyScreen(
    email: String,
    onNext: (String) -> Unit,
    onBack: () -> Unit
) {
    var otp by remember { mutableStateOf("") }

    AuthLayout(
        title = "Verify Code",
        subtitle = "Enter the code we just sent you on your registered Email\n$email",
        onBack = onBack
    ) {
        OutlinedTextField(
            value = otp,
            onValueChange = { if (it.length <= 5 && it.all(Char::isDigit)) otp = it },
            label = { Text("Mã 5 chữ số") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onNext(otp) },
            enabled = otp.length == 5,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Next") }
    }
}