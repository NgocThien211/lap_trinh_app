package com.example.btvntuan2bai1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { StudentScreen() }
    }
}

@Composable
fun InfoLine(label: String, value: String?) {
    Row {
        Text("$label: ", fontSize = 12.sp, color = Color.DarkGray)
        Text(
            showText(value),
            fontSize = 12.sp,
            color = if (value == null) Color.Red else Color.DarkGray,
            fontStyle = if (value == null) FontStyle.Italic else FontStyle.Normal
        )
    }
}

@Composable
fun StudentCard(s: Student) {
    val shape = RoundedCornerShape(10.dp)
    Column(
        Modifier.fillMaxWidth()
            .background(Color.White, shape)
            .border(1.dp, Color(0xFFE3E8F0), shape)
            .padding(12.dp)
    ) {
        Text(s.name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        InfoLine("Email", s.email)
        InfoLine("SĐT", s.phone)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentScreen() {
    val students = getStudents()
    val missingEmail = countMissingEmail(students)

    Scaffold(
        containerColor = Color(0xFFF1F3F9),
        topBar = {
            TopAppBar(
                title = { Text("Danh sách sinh viên", color = Color.White, fontSize = 16.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2B4170))
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(12.dp)) {
            // Bước 3: dùng for để tạo thẻ cho từng sinh viên
            for (s in students) {
                StudentCard(s)
                Spacer(Modifier.height(8.dp))
            }

            Row(
                Modifier.fillMaxWidth()
                    .background(Color(0xFFE6ECF7), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Text("Tổng: ", fontSize = 12.sp)
                    Text("${students.size} sinh viên", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Row {
                    Text("Thiếu email: ", fontSize = 12.sp)
                    Text("$missingEmail", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                }
            }
        }
    }
}