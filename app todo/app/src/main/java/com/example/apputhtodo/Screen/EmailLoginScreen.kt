package com.example.apputhtodo.Screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blue = Color(0xFF386AE8)
private val DarkBlue = Color(0xFF1B315D)
private val GrayText = Color(0xFF7888A4)

@Composable
fun LoginScreen(
    onGoogleLogin: () -> Unit,
    onEmailLogin: () -> Unit,
    onPhoneLogin: () -> Unit,
    onGuestLogin: () -> Unit,
    isLoading: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFE6F0FF),
                        Color(0xFFF9FBFF),
                        Color(0xFFDCE9FF)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(55.dp))

            // Logo ứng dụng
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF6AA5FF),
                                Blue
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "UTH Todo",
                    tint = Color.White,
                    modifier = Modifier.size(45.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "UTH Todo",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DarkBlue
            )

            Text(
                text = "Quản lí công việc • Học tập • Cuộc sống",
                fontSize = 12.sp,
                color = GrayText,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(30.dp))

            // Minh họa danh sách công việc
            TodoIllustration()

            Spacer(Modifier.height(30.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(
                        alpha = 0.94f
                    )
                ),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Đăng nhập để tiếp tục",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "Đồng bộ công việc và sử dụng đầy đủ\n" +
                                "tính năng của UTH Todo",
                        fontSize = 12.sp,
                        color = GrayText,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(24.dp))

                    // Đăng nhập Email
                    LoginOptionButton(
                        text = "Đăng nhập bằng Email",
                        onClick = onEmailLogin,
                        enabled = !isLoading,
                        icon = {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                tint = Blue
                            )
                        }
                    )

                    Spacer(Modifier.height(12.dp))

                    // Đăng nhập điện thoại
                    LoginOptionButton(
                        text = "Đăng nhập bằng số điện thoại",
                        onClick = onPhoneLogin,
                        enabled = !isLoading,
                        icon = {
                            Icon(
                                Icons.Default.Phone,
                                contentDescription = null,
                                tint = Blue
                            )
                        }
                    )

                    Spacer(Modifier.height(12.dp))

                    // Đăng nhập Google
                    LoginOptionButton(
                        text = "Tiếp tục với Google",
                        onClick = onGoogleLogin,
                        enabled = !isLoading,
                        icon = {
                            Text(
                                "G",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4285F4)
                            )
                        }
                    )

                    Spacer(Modifier.height(20.dp))

                    Text(
                        "Hoặc",
                        color = GrayText,
                        fontSize = 12.sp
                    )

                    Spacer(Modifier.height(10.dp))

                    Button(
                        onClick = onGuestLogin,
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE4EEFF),
                            contentColor = DarkBlue
                        )
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(19.dp)
                        )

                        Spacer(Modifier.width(8.dp))

                        Text("Dùng thử không cần tài khoản")
                    }
                }
            }

            Spacer(Modifier.height(22.dp))

            Text(
                "Bằng cách tiếp tục, bạn đồng ý với Điều khoản\n" +
                        "sử dụng và Chính sách quyền riêng tư.",
                color = GrayText,
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))
        }
    }
}

@Composable
private fun LoginOptionButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    icon: @Composable () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            1.dp,
            Color(0xFFD7E3F5)
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier.width(28.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Spacer(Modifier.width(8.dp))

        Text(
            text = text,
            fontSize = 13.sp,
            color = DarkBlue,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun TodoIllustration() {
    Card(
        modifier = Modifier
            .width(235.dp)
            .height(175.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(7.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Việc cần làm hôm nay",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )

            Spacer(Modifier.height(16.dp))

            IllustrationTask("Hoàn thành bài tập", true)
            Spacer(Modifier.height(12.dp))
            IllustrationTask("Học Android Studio", true)
            Spacer(Modifier.height(12.dp))
            IllustrationTask("Lập kế hoạch ngày mai", false)
        }
    }
}

@Composable
private fun IllustrationTask(
    title: String,
    completed: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(19.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(
                    if (completed) Blue
                    else Color(0xFFE6EDFA)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (completed) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(Modifier.width(10.dp))

        Text(
            title,
            fontSize = 11.sp,
            color = DarkBlue
        )
    }
}
