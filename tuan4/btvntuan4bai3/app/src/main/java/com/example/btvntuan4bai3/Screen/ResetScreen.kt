package com.example.btvntuan4bai3.Screen


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun ResetScreen(
    onNext: (String) -> Unit,
    onBack: () -> Unit
) {
    var pass by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    val mismatch = confirm.isNotEmpty() && pass != confirm

    AuthLayout(
        title = "Create new password",
        subtitle = "Your new password must be different form previously used password",
        onBack = onBack
    ) {
        OutlinedTextField(
            value = pass, onValueChange = { pass = it },
            label = { Text("Password") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = confirm, onValueChange = { confirm = it },
            label = { Text("Confirm Password") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            isError = mismatch,
            supportingText = { if (mismatch) Text("Mật khẩu không khớp") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onNext(pass) },
            enabled = pass.isNotEmpty() && pass == confirm,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Next") }
    }
}