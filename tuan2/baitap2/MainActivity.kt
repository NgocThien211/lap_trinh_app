package com.example.btvntuan2bai3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
        setContent { XepLoaiScreen() }
    }
}


fun textColorOf(loai: String): Color {
    if (loai == "Giỏi") return Color(0xFF2E7D32)
    else if (loai == "Khá") return Color(0xFF2B6CD4)
    else if (loai == "Trung bình") return Color(0xFFB26A00)
    else if (loai == "Yếu") return Color(0xFFD93025)
    else return Color.Gray
}

fun bgColorOf(loai: String): Color {
    if (loai == "Giỏi") return Color(0xFFE3F2E6)
    else if (loai == "Khá") return Color(0xFFE3ECFA)
    else if (loai == "Trung bình") return Color(0xFFFCEFD6)
    else if (loai == "Yếu") return Color(0xFFFBE3E1)
    else return Color(0xFFEDEEF2)
}

@Composable
fun CheckRow(score: Double) {
    val loai = xepLoai(score)
    Row(
        Modifier.fillMaxWidth().padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Điểm $score", fontSize = 12.sp)
        Text(
            loai,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColorOf(loai),
            modifier = Modifier
                .background(bgColorOf(loai), RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XepLoaiScreen() {
    val diem = getDiem()
    val loai = xepLoai(diem)
    val cardShape = RoundedCornerShape(12.dp)

    Scaffold(
        containerColor = Color(0xFFF1F3F9),
        topBar = {
            TopAppBar(
                title = { Text("Xếp loại học lực", color = Color.White, fontSize = 16.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2B4170))
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(12.dp)) {

            Column(
                Modifier.fillMaxWidth()
                    .background(Color.White, cardShape)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Điểm trung bình", fontSize = 11.sp, color = Color.DarkGray)
                Text("$diem", fontSize = 44.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(
                    loai,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColorOf(loai),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgColorOf(loai), RoundedCornerShape(8.dp))
                        .padding(vertical = 10.dp)
                )
            }

            Spacer(Modifier.height(16.dp))
            Text(
                "KIỂM TRA THÊM",
                fontSize = 10.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(8.dp))

            Column(
                Modifier.fillMaxWidth()
                    .background(Color.White, cardShape)
                    .padding(horizontal = 12.dp)
            ) {
                for (s in getKiemTra()) {
                    CheckRow(s)
                }
            }
        }
    }
}