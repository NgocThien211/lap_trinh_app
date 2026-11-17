
package com.example.apputhtodo.Screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blue = Color(0xFF386AE8)
private val DarkBlue = Color(0xFF1B315D)
private val GrayText = Color(0xFF7888A4)

@Composable
fun EmailLoginScreen(
    onBack: () -> Unit,
    onSubmit: (String, String, Boolean) -> Unit,
    onGoogleLogin: () -> Unit = {},
    onForgotPassword: (String) -> Unit = {},
    isLoading: Boolean = false
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isRegister by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFEAF2FF),
                        Color.White,
                        Color(0xFFE3EDFF)
                    )
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("← Quay lại", color = DarkBlue)
        }

        Spacer(Modifier.height(40.dp))

        // Minh họa phong thư
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    Color(0xFFDCE9FF),
                    RoundedCornerShape(30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Email,
                contentDescription = "Email",
                modifier = Modifier.size(72.dp),
                tint = Blue
            )
        }

        Spacer(Modifier.height(30.dp))

        Text(
            text = if (isRegister)
                "Tạo tài khoản UTH Todo"
            else
                "Đăng nhập bằng Email",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Nhập email và mật khẩu của bạn",
            fontSize = 13.sp,
            color = GrayText
        )

        Spacer(Modifier.height(35.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = {
                Icon(Icons.Default.Email, null)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu") },
            leadingIcon = {
                Icon(Icons.Default.Lock, null)
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        showPassword = !showPassword
                    }
                ) {
                    Icon(
                        if (showPassword)
                            Icons.Default.VisibilityOff
                        else
                            Icons.Default.Visibility,
                        contentDescription = "Hiện/ẩn mật khẩu"
                    )
                }
            },
            visualTransformation =
                if (showPassword)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        )

        if (!isRegister) {
            TextButton(
                onClick = {
                    onForgotPassword(email.trim())
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    "Quên mật khẩu?",
                    color = Blue,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(Modifier.height(25.dp))

        Button(
            onClick = {
                onSubmit(
                    email.trim(),
                    password,
                    isRegister
                )
            },
            enabled = !isLoading &&
                    email.isNotBlank() &&
                    password.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    if (isRegister) "Đăng ký"
                    else "Đăng nhập",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(25.dp))

        Text(
            "Hoặc đăng nhập nhanh bằng",
            color = GrayText,
            fontSize = 12.sp
        )

        Spacer(Modifier.height(15.dp))

        OutlinedButton(
            onClick = onGoogleLogin,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                "G",
                color = Color(0xFF4285F4),
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.width(12.dp))

            Text(
                "Google",
                color = DarkBlue,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(25.dp))

        TextButton(
            onClick = {
                isRegister = !isRegister
            }
        ) {
            Text(
                if (isRegister)
                    "Đã có tài khoản? Đăng nhập"
                else
                    "Chưa có tài khoản? Đăng ký ngay",
                color = Blue,
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(20.dp))
    }
}
