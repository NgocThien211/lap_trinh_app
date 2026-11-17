package com.example.btvntuan4bai3.Screen


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmScreen(
    email: String,
    otp: String,
    pass: String,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    AuthLayout(title = "Confirm", subtitle = "We are here to help you!", onBack = onBack) {
        OutlinedTextField(
            value = email, onValueChange = {}, readOnly = true,
            label = { Text("Email") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = otp, onValueChange = {}, readOnly = true,
            label = { Text("OTP") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = pass, onValueChange = {}, readOnly = true,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onSubmit, modifier = Modifier.fillMaxWidth()) { Text("Submit") }
    }
}