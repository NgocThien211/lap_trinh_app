package com.example.baitapthuchanhtuan2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                PhoneFrame {
                    ManHinh()
                }
            }
        }
    }
}

@Composable
fun ManHinh() {
    var a by remember { mutableStateOf("") }
    var b by remember { mutableStateOf("") }
    var phepToan by remember { mutableStateOf("") }

    val ketQua = tinh(a, b, phepToan)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(90.dp))

        Text(
            text = "Thực hành 03",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(60.dp))

        ONhap(giaTri = a, khiDoi = { a = it })

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NutPhepToan("+", Color(0xFFD9534F), Color.Black, phepToan == "+") { phepToan = "+" }
            NutPhepToan("-", Color(0xFFE0A93E), Color.Black, phepToan == "-") { phepToan = "-" }
            NutPhepToan("*", Color(0xFF5B3EE0), Color.Black, phepToan == "*") { phepToan = "*" }
            NutPhepToan("/", Color(0xFF1C1C1E), Color(0xFF9CA3AF), phepToan == "/") { phepToan = "/" }
        }

        Spacer(Modifier.height(20.dp))

        ONhap(giaTri = b, khiDoi = { b = it })

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Kết quả: $ketQua",
            fontSize = 17.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ONhap(giaTri: String, khiDoi: (String) -> Unit) {
    OutlinedTextField(
        value = giaTri,
        onValueChange = khiDoi,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFB0B0B0),
            focusedBorderColor = Color(0xFF5B3EE0)
        )
    )
}

@Composable
fun NutPhepToan(
    kyHieu: String,
    mau: Color,
    mauVien: Color,
    dangChon: Boolean,
    khiBam: () -> Unit
) {
    Button(
        onClick = khiBam,
        modifier = Modifier.size(56.dp),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = mau),
        border = if (dangChon) BorderStroke(3.dp, mauVien) else null
    ) {
        Text(kyHieu, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun PhoneFrame(content: @Composable () -> Unit) {
    val mauKhung = Color(0xFF1F1F1F)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(9f / 19f)
                .clip(RoundedCornerShape(36.dp))
                .background(mauKhung)
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(30.dp))
            ) {
                content()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ManHinhPreview() {
    PhoneFrame {
        ManHinh()
    }
}