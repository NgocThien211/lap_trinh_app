package com.example.apputhtodo.Screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apputhtodo.model.TodoTask
import com.example.apputhtodo.viewmodel.AddTaskViewModel

private val PrimaryBlue = Color(0xFF365BE8)
private val LightBlueBg = Color(0xFFEAF0FF)
private val PageBg = Color(0xFFF4F6FA)
private val TextMain = Color(0xFF18233F)
private val TextSub = Color(0xFF7B849B)

@Composable
fun AddTaskScreen(
    initial: TodoTask? = null,
    onCancel: () -> Unit,
    onSaved: (TodoTask) -> Unit,
    vm: AddTaskViewModel = viewModel()
) {
    LaunchedEffect(initial) {
        vm.loadInitial(initial)
    }

    var showTimeDialog by remember { mutableStateOf(false) }
    var tempTime by remember(vm.time) { mutableStateOf(vm.time) }

    val canSave = vm.title.isNotBlank() && !vm.isSaving

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        // 1. Thanh tiêu đề: Hủy - Việc mới - Lưu
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hủy",
                color = PrimaryBlue,
                fontSize = 15.sp,
                modifier = Modifier.clickable { onCancel() }
            )
            Text(
                text = if (initial == null) "Việc mới" else "Sửa việc",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = TextMain,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Lưu",
                color = if (canSave) PrimaryBlue else TextSub.copy(alpha = 0.5f),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(enabled = canSave) {
                    vm.submitTask(initial, onSaved)
                }
            )
        }

        Spacer(Modifier.height(18.dp))

        // 2. Khung Tên công việc & Ghi chú
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text("Tên công việc", fontSize = 12.sp, color = TextSub)
                TextField(
                    value = vm.title,
                    onValueChange = { vm.title = it },
                    placeholder = {
                        Text("Ví dụ: Ôn chương 4 – Giải thuật", color = TextSub.copy(alpha = 0.6f))
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider(color = Color(0xFFEEF1F6))
                Spacer(Modifier.height(8.dp))

                Text("Ghi chú", fontSize = 12.sp, color = TextSub)
                TextField(
                    value = vm.note,
                    onValueChange = { vm.note = it },
                    placeholder = {
                        Text("Thêm ghi chú (không bắt buộc)", color = TextSub.copy(alpha = 0.6f))
                    },
                    minLines = 2,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        // 3. Nhóm (Học tập / Cuộc sống)
        Text("Nhóm", fontSize = 13.sp, color = TextSub, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf("Học tập", "Cuộc sống").forEach { item ->
                val selected = vm.category == item
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clickable { vm.category = item },
                    shape = RoundedCornerShape(12.dp),
                    color = if (selected) LightBlueBg else Color.White,
                    border = if (selected) BorderStroke(1.5.dp, PrimaryBlue) else null
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = item,
                            color = if (selected) PrimaryBlue else TextMain,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // 4. Ngày (Hôm nay / Ngày mai / Tuần này)
        Text("Ngày", fontSize = 13.sp, color = TextSub, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Hôm nay", "Ngày mai", "Tuần này").forEach { item ->
                val selected = vm.dateOption == item
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clickable { vm.dateOption = item },
                    shape = RoundedCornerShape(12.dp),
                    color = if (selected) LightBlueBg else Color.White,
                    border = if (selected) BorderStroke(1.5.dp, PrimaryBlue) else null
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = item,
                            color = if (selected) PrimaryBlue else TextMain,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // 5. Giờ & Nhắc tôi
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = TextSub)
                    Spacer(Modifier.width(10.dp))
                    Text("Giờ", fontWeight = FontWeight.SemiBold, color = TextMain, modifier = Modifier.weight(1f))

                    Surface(
                        color = PageBg,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.clickable {
                            tempTime = vm.time
                            showTimeDialog = true
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(vm.time, fontWeight = FontWeight.Bold, color = TextMain, fontSize = 13.sp)
                            Spacer(Modifier.width(6.dp))
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = null,
                                tint = TextMain,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFFEEF1F6))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = TextSub)
                    Spacer(Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Nhắc tôi", fontWeight = FontWeight.SemiBold, color = TextMain, fontSize = 14.sp)
                        Text("Trước 15 phút", color = TextSub, fontSize = 11.sp)
                    }
                    Switch(
                        checked = vm.remind,
                        onCheckedChange = { vm.remind = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF34C759)
                        )
                    )
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // 6. Mức ưu tiên (Thấp / Vừa / Cao)
        Text("Mức ưu tiên", fontSize = 13.sp, color = TextSub, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE7EAF2))
                .padding(4.dp)
        ) {
            listOf("Thấp", "Vừa", "Cao").forEach { level ->
                val selected = vm.priority == level
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clickable { vm.priority = level },
                    shape = RoundedCornerShape(9.dp),
                    color = if (selected) Color.White else Color.Transparent
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = level,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            color = if (selected) TextMain else TextSub,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(36.dp))

        // 7. Nút Lưu công việc
        Button(
            onClick = { vm.submitTask(initial, onSaved) },
            enabled = canSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                disabledContainerColor = Color(0xFFE2E5EC),
                contentColor = Color.White,
                disabledContentColor = TextSub
            )
        ) {
            Text("Lưu công việc", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        }
    }

    if (showTimeDialog) {
        AlertDialog(
            onDismissRequest = { showTimeDialog = false },
            title = { Text("Chọn giờ") },
            text = {
                OutlinedTextField(
                    value = tempTime,
                    onValueChange = { tempTime = it },
                    label = { Text("Giờ (HH:mm)") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (tempTime.isNotBlank()) vm.time = tempTime.trim()
                    showTimeDialog = false
                }) {
                    Text("Xong")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimeDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}

