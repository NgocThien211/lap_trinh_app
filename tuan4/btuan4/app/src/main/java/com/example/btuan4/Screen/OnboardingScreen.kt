package com.example.btuan4.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btuan4.model.IllustrationType
import com.example.btuan4.model.OnboardingItem
import kotlinx.coroutines.launch
import androidx.activity.compose.BackHandler

@Composable
fun OnboardingScreen(items: List<OnboardingItem>, onFinish: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { items.size })
    val scope = rememberCoroutineScope()
    val isLast = pagerState.currentPage == items.lastIndex
    BackHandler(enabled = pagerState.currentPage > 0) {
        scope.launch { pagerState.animateScrollToPage(0) }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp)
    ) {

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Brand.Blue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text("UTH", fontWeight = FontWeight.Bold)
                Text(" Todo", color = Brand.Blue, fontWeight = FontWeight.Bold)
            }
            Text(
                "Bỏ qua",
                color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier.clickable { onFinish() }
            )
        }


        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            val item = items[page]
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Illustration(item.illustration)
                Spacer(Modifier.height(32.dp))
                Text(item.title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(item.description, fontSize = 14.sp, color = Color.Gray)
            }
        }


        Row(Modifier.padding(bottom = 16.dp)) {
            repeat(items.size) { i ->
                val selected = i == pagerState.currentPage
                Box(
                    Modifier
                        .padding(end = 6.dp)
                        .size(width = if (selected) 20.dp else 8.dp, height = 8.dp)
                        .clip(CircleShape)
                        .background(if (selected) Brand.Blue else Color.LightGray)
                )
            }
        }
        Button(
            onClick = {
                if (isLast) onFinish()
                else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Brand.Blue)
        ) {
            Text(if (isLast) "Bắt đầu ngay" else "Tiếp tục", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun Illustration(type: IllustrationType) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Brand.BlueSoft),
        contentAlignment = Alignment.Center
    ) {
        when (type) {
            IllustrationType.CHECKLIST -> Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(3) { i -> TaskRowMock(checked = i == 0) }
            }

            IllustrationType.CATEGORIES -> Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CategoryCardMock("Học tập", Brand.BlueSoft, Brand.Blue)
                CategoryCardMock("Cuộc sống", Brand.Orange, Brand.OrangeText)
            }

            IllustrationType.REMINDER -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = Brand.Blue)
                }
                Spacer(Modifier.height(12.dp))
                Surface(shape = RoundedCornerShape(8.dp), color = Color.White) {
                    Text(
                        "Ôn bài Toán lúc 19:00",
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskRowMock(checked: Boolean) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        modifier = Modifier.width(200.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (checked) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Brand.Blue,
                    modifier = Modifier.size(18.dp)
                )
            } else {
                Box(Modifier.size(18.dp).border(1.5.dp, Color.LightGray, CircleShape))
            }
            Spacer(Modifier.width(10.dp))
            Box(
                Modifier
                    .height(6.dp)
                    .weight(1f)
                    .clip(CircleShape)
                    .background(Color(0xFFE5E7EB))
            )
        }
    }
}

@Composable
private fun CategoryCardMock(label: String, chipBg: Color, chipText: Color) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        modifier = Modifier.width(200.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Surface(shape = RoundedCornerShape(6.dp), color = chipBg) {
                Text(
                    label,
                    fontSize = 10.sp,
                    color = chipText,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Box(
                Modifier
                    .height(6.dp)
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .background(Color(0xFFE5E7EB))
            )
        }
    }
}