package com.example.apputhtodo.Screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.apputhtodo.model.TodoTask
import com.example.apputhtodo.ui.theme.*
import com.google.firebase.auth.FirebaseUser

@Composable
fun ProfileScreen(
    user: FirebaseUser?,
    tasks: List<TodoTask>,
    isGuest: Boolean,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        containerColor = TodoBackground,
        topBar = {
            Row(
                Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, null)
                    Text("Cài đặt")
                }
                Spacer(Modifier.weight(1f))
                Text("Hồ sơ", fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            Surface(
                shape = CircleShape,
                color = Color(0xFFE5ECFF),
                modifier = Modifier.size(82.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        (user?.displayName ?: "Khách")
                            .take(2).uppercase(),
                        color = TodoBlue,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                if (isGuest) "Khách"
                else (user?.displayName ?: "Người dùng"),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Text(
                if (isGuest) "Chế độ dùng thử"
                else (user?.email ?: ""),
                color = TodoGray,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                if (isGuest) "Chưa liên kết Google"
                else "✓ Đã liên kết tài khoản Google",
                color = Color(0xFF24885B),
                fontSize = 12.sp
            )

            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    "${tasks.count { it.completed }}" to "Việc đã xong",
                    "${tasks.count { !it.completed }}" to "Đang chờ",
                    "${if (tasks.isEmpty()) 0 else tasks.count { it.completed } * 100 / tasks.size}%" to "Hoàn thành"
                ).forEach { (value, label) ->
                    Card(
                        Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            Modifier.padding(
                                vertical = 18.dp,
                                horizontal = 5.dp
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                value,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(
                                label,
                                fontSize = 10.sp,
                                color = TodoGray
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFFBA3D32)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    if (isGuest) "Thoát chế độ dùng thử"
                    else "Đăng xuất"
                )
            }
        }
    }
}
