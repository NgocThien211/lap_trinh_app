package com.example.apputhtodo.Screen


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.*
import com.example.apputhtodo.model.TodoTask
import com.example.apputhtodo.ui.theme.*

@Composable
fun HomeScreen(
    userName: String,
    tasks: List<TodoTask>,
    error: String?,
    onProfile: () -> Unit,
    onAdd: (TodoTask) -> Unit,
    onToggle: (TodoTask) -> Unit
) {
    var adding by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Học tập") }

    val done = tasks.count { it.completed }
    val progress = if (tasks.isEmpty()) 0f
    else done.toFloat() / tasks.size

    Scaffold(
        containerColor = TodoBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { adding = true },
                containerColor = TodoBlue,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, "Thêm công việc")
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Trang chủ") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.List, null) },
                    label = { Text("Danh sách") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.Notifications, null) },
                    label = { Text("Thông báo") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onProfile,
                    icon = { Icon(Icons.Default.Settings, null) },
                    label = { Text("Cài đặt") }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Chào $userName!",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onProfile) {
                        Icon(Icons.Default.AccountCircle, "Hồ sơ")
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = TodoBlue
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(Modifier.fillMaxWidth().padding(18.dp)) {
                        Text(
                            "Tiến độ hôm nay",
                            color = Color.White.copy(alpha = .85f),
                            fontSize = 12.sp
                        )
                        Text(
                            "$done/${tasks.size} việc",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White,
                            trackColor = Color.White.copy(alpha = .3f)
                        )
                        Text(
                            "Đã hoàn thành, cố lên!",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf("Học tập", "Cuộc sống").forEach { cat ->
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Icon(
                                    if (cat == "Học tập")
                                        Icons.Default.School
                                    else Icons.Default.Home,
                                    null,
                                    tint = TodoBlue
                                )
                                Spacer(Modifier.height(12.dp))
                                Text(cat, fontWeight = FontWeight.Bold)
                                Text(
                                    "${tasks.count { it.category == cat }} việc",
                                    color = TodoGray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
                Text(
                    "Việc hôm nay",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                if (error != null) {
                    Text(error, color = MaterialTheme.colorScheme.error)
                }
            }

            if (tasks.isEmpty()) {
                item {
                    Text(
                        "Chưa có công việc. Nhấn + để thêm.",
                        color = TodoGray
                    )
                }
            }

            items(tasks, key = { it.id }) { task ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth()
                            .clickable { onToggle(task) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = task.completed,
                            onCheckedChange = { onToggle(task) }
                        )
                        Column(Modifier.weight(1f)) {
                            Text(
                                task.title,
                                fontWeight = FontWeight.Medium,
                                textDecoration =
                                    if (task.completed)
                                        TextDecoration.LineThrough
                                    else null
                            )
                            Text(
                                "${task.category}  ${task.time}",
                                color = TodoGray,
                                fontSize = 12.sp
                            )
                        }
                        Icon(
                            Icons.Default.ChevronRight,
                            null,
                            tint = TodoGray
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(64.dp)) }
        }
    }

    if (adding) {
        AlertDialog(
            onDismissRequest = { adding = false },
            title = { Text("Thêm công việc") },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Tên công việc") },
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))
                    Row {
                        listOf("Học tập", "Cuộc sống").forEach { cat ->
                            FilterChip(
                                selected = category == cat,
                                onClick = { category = cat },
                                label = { Text(cat) },
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (title.isNotBlank()) {
                        onAdd(
                            TodoTask(
                                title = title.trim(),
                                category = category
                            )
                        )
                        title = ""
                        adding = false
                    }
                }) {
                    Text("Thêm")
                }
            },
            dismissButton = {
                TextButton(onClick = { adding = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}
