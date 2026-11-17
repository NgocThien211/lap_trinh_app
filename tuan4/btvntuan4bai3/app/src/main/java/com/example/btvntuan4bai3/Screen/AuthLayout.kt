package com.example.btvntuan4bai3.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AuthLayout(
    title: String,
    subtitle: String,
    onBack: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().statusBarsPadding().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.fillMaxWidth().height(48.dp)) {
            if (onBack != null) {
                IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                }
            }
        }
        Text("UTH", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B7F7A))
        Text("SmartTasks", style = MaterialTheme.typography.titleMedium, color = Color(0xFF3B8FEF))
        Spacer(Modifier.height(24.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        content()
    }
}