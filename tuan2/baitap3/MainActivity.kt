package com.example.btvntuan2bai2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ScoreBoardScreen() }
    }
}

fun loaiColor(loai: String): Color = when (loai) {
    "Giỏi" -> Color(0xFF2E9E5B)
    "Khá" -> Color(0xFF2B6CD4)
    "Trung bình" -> Color(0xFFE08A00)
    "Yếu" -> Color(0xFFD93025)
    else -> Color.Gray
}

@Composable
fun StatItem(label: String, value: String) {
    Column {
        Text(label, fontSize = 10.sp, color = Color(0xFFB8C4E0))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun StudentRow(s: Students) {
    val shape = RoundedCornerShape(10.dp)
    val loai = loaiOf(s.score)

    Row(
        Modifier.fillMaxWidth()
            .background(Color.White, shape)
            .border(1.dp, Color(0xFFE3E8F0), shape)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(s.name, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(loai, fontSize = 10.sp, color = loaiColor(loai))
        }
        Text(
            if (s.score == null) "—" else s.score.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (s.score == null) Color.Gray else Color(0xFF1C1C1C)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreBoardScreen() {
    val students = getStudents()
    val stats = tinhThongKe(students)

    Scaffold(
        containerColor = Color(0xFFF1F3F9),
        topBar = {
            TopAppBar(
                title = { Text("Bảng điểm lớp", color = Color.White, fontSize = 16.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2B4170))
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(12.dp)) {

            // Thẻ điểm trung bình
            Column(
                Modifier.fillMaxWidth()
                    .background(Color(0xFF2B4170), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text("Điểm trung bình lớp", fontSize = 11.sp, color = Color(0xFFB8C4E0))
                Text(
                    String.format("%.2f", stats.avg),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(Modifier.height(10.dp))
                Spacer(Modifier.fillMaxWidth().height(1.dp).background(Color(0x33FFFFFF)))
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                    StatItem("Giỏi", "${stats.countGioi}")
                    StatItem("Chưa có điểm", "${stats.countMissing}")
                    StatItem("Cao nhất", "${stats.max}")
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(
                "DANH SÁCH · ${students.size} SINH VIÊN",
                fontSize = 10.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(8.dp))

            // Bước 2: for hiển thị từng sinh viên
            for (s in students) {
                StudentRow(s)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}