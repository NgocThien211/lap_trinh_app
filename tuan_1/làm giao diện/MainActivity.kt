package com.example.baitap01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                PhoneFrame {
                    ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F8FB))
            .padding(16.dp)
    ) {

        // Nút quay lại
        IconButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
        }

        // Nút chỉnh sửa
        IconButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
        ) {
            Icon(
                Icons.Default.Edit,
                contentDescription = "Chỉnh sửa",
                tint = Color(0xFF2E9E83)
            )
        }


        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = "Ảnh đại diện",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Phan Ngọc Thiện",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "SV0123456",
                fontSize = 18.sp,
                color = Color.Gray
            )
        }
    }
}


@Composable
fun PhoneFrame(content: @Composable () -> Unit) {
    val mauKhung = Color(0xFF3C3C3C)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5E7EB))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(9f / 19f)
                .clip(RoundedCornerShape(40.dp))
                .background(mauKhung)
                .padding(10.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(32.dp))
            ) {
                content()
            }


            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .width(110.dp)
                    .height(26.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(mauKhung)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    PhoneFrame {
        ProfileScreen()
    }
}