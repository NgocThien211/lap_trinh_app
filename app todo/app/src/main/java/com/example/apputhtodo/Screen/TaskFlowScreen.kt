
package com.example.apputhtodo.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apputhtodo.model.SubTask
import com.example.apputhtodo.model.TodoTask
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


private val Blue = Color(0xFF365BE8)
private val Background = Color(0xFFF4F6FD)
private val DarkText = Color(0xFF18233F)
private val MutedText = Color(0xFF667085)
private val StudyBackground = Color(0xFFEAF0FF)
private val LifeBackground = Color(0xFFFFF0DF)
private val LifeOrange = Color(0xFFD88A25)
private val SuccessGreen = Color(0xFF22A56A)

private fun today(): String =
    SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

private fun categoryColor(category: String): Color =
    if (category == "Học tập") Blue else LifeOrange


@Composable
fun TaskFlowScreen(
    userName: String,
    tasks: List<TodoTask>,
    error: String?,
    onProfile: () -> Unit,
    onSave: (TodoTask) -> Unit,
    onDelete: (String) -> Unit,
    onToggle: (TodoTask) -> Unit,
    onFetchApi: (() -> Unit)? = null
) {
    var tab by remember { mutableIntStateOf(0) }
    var selectedId by remember { mutableStateOf<String?>(null) }
    var editing by remember { mutableStateOf<TodoTask?>(null) }
    var showEditor by remember { mutableStateOf(false) }
    var filter by remember { mutableStateOf("Tất cả") }
    var query by remember { mutableStateOf("") }

    val selectedTask = tasks.find { it.id == selectedId }
    val pendingCount = tasks.count { !it.completed }

    Scaffold(
        containerColor = Background,


        floatingActionButton = {
            if (selectedId == null && tab != 2 && !showEditor) {
                FloatingActionButton(
                    onClick = {
                        editing = null
                        showEditor = true
                    },
                    containerColor = Blue,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Thêm công việc"
                    )
                }
            }
        },


        bottomBar = {
            if (selectedId == null && !showEditor) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 6.dp
                ) {
                    NavigationBarItem(
                        selected = tab == 0,
                        onClick = { tab = 0 },
                        icon = {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Trang chủ"
                            )
                        },
                        label = { Text("Trang chủ") },
                        colors = navColors()
                    )

                    NavigationBarItem(
                        selected = tab == 1,
                        onClick = { tab = 1 },
                        icon = {
                            Icon(
                                Icons.Default.List,
                                contentDescription = "Danh sách"
                            )
                        },
                        label = { Text("Danh sách") },
                        colors = navColors()
                    )

                    NavigationBarItem(
                        selected = tab == 2,
                        onClick = { tab = 2 },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (pendingCount > 0) {
                                        Badge()
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Notifications,
                                    contentDescription = "Thông báo"
                                )
                            }
                        },
                        label = { Text("Thông báo") },
                        colors = navColors()
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick = onProfile,
                        icon = {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = "Cài đặt"
                            )
                        },
                        label = { Text("Cài đặt") },
                        colors = navColors()
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = if (showEditor) 0.dp else 16.dp)
        ) {
            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            when {
                showEditor -> {
                    AddTaskScreen(
                        initial = editing,
                        onCancel = { showEditor = false },
                        onSaved = {
                            onSave(it)
                            showEditor = false
                        }
                    )
                }

                selectedTask != null -> {
                    DetailContent(
                        task = selectedTask,
                        onBack = { selectedId = null },
                        onEdit = {
                            editing = selectedTask
                            showEditor = true
                        },
                        onSave = onSave,
                        onDelete = {
                            onDelete(selectedTask.id)
                            selectedId = null
                        },
                        onToggle = { onToggle(selectedTask) }
                    )
                }

                // TRANG CHỦ
                tab == 0 -> {
                    HomeContent(
                        userName = userName,
                        tasks = tasks,
                        onProfile = onProfile,
                        onFetchApi = onFetchApi,
                        onOpenTask = { selectedId = it },
                        onToggle = onToggle,
                        onOpenList = {
                            filter = "Tất cả"
                            tab = 1
                        },
                        onCategory = {
                            filter = it
                            tab = 1
                        }
                    )
                }

                // DANH SÁCH
                tab == 1 -> {
                    ListContent(
                        tasks = tasks,
                        filter = filter,
                        query = query,
                        onFilter = { filter = it },
                        onQuery = { query = it },
                        onOpenTask = { selectedId = it },
                        onToggle = onToggle
                    )
                }

                // THÔNG BÁO
                else -> {
                    NotificationContent(
                        tasks = tasks,
                        onOpenTask = { selectedId = it }
                    )
                }
            }
        }
    }
}

@Composable
private fun navColors() =
    NavigationBarItemDefaults.colors(
        selectedIconColor = Blue,
        selectedTextColor = Blue,
        indicatorColor = StudyBackground,
        unselectedIconColor = MutedText,
        unselectedTextColor = MutedText
    )


@Composable
private fun HomeContent(
    userName: String,
    tasks: List<TodoTask>,
    onProfile: () -> Unit,
    onFetchApi: (() -> Unit)? = null,
    onOpenTask: (String) -> Unit,
    onToggle: (TodoTask) -> Unit,
    onOpenList: () -> Unit,
    onCategory: (String) -> Unit
) {
    val done = tasks.count { it.completed }
    val progress = if (tasks.isEmpty()) {
        0f
    } else {
        done.toFloat() / tasks.size
    }

    val todayTasks = tasks.filter {
        it.dueDate.isBlank() || it.dueDate == today()
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Chào $userName!",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )

                    Text(
                        "Hôm nay cùng hoàn thành mục tiêu nhé!",
                        fontSize = 13.sp,
                        color = MutedText
                    )
                }

                if (onFetchApi != null) {
                    IconButton(onClick = onFetchApi) {
                        Icon(
                            Icons.Default.CloudDownload,
                            contentDescription = "Tải từ API",
                            tint = Blue,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                IconButton(onClick = onProfile) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = "Tài khoản",
                        tint = Blue,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Blue
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        "Tiến độ công việc",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 14.sp
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "$done/${tasks.size} việc",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        "Cố gắng hoàn thành mục tiêu nhé!",
                        color = Color.White,
                        fontSize = 12.sp
                    )

                    Spacer(Modifier.height(16.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(9.dp),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "${(progress * 100).toInt()}% hoàn thành",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CategoryCard(
                    title = "Học tập",
                    count = tasks.count {
                        it.category == "Học tập"
                    },
                    background = StudyBackground,
                    accent = Blue,
                    modifier = Modifier.weight(1f),
                    onClick = { onCategory("Học tập") }
                )

                CategoryCard(
                    title = "Cuộc sống",
                    count = tasks.count {
                        it.category == "Cuộc sống"
                    },
                    background = LifeBackground,
                    accent = LifeOrange,
                    modifier = Modifier.weight(1f),
                    onClick = { onCategory("Cuộc sống") }
                )
            }
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Việc hôm nay",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    modifier = Modifier.weight(1f)
                )

                TextButton(onClick = onOpenList) {
                    Text("Xem tất cả", color = Blue)
                }
            }
        }

        if (todayTasks.isEmpty()) {
            item {
                EmptyCard("Không có công việc nào hôm nay.")
            }
        }

        items(todayTasks, key = { it.id }) { task ->
            TaskRow(
                task = task,
                onOpen = { onOpenTask(task.id) },
                onToggle = { onToggle(task) }
            )
        }

        item {
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun CategoryCard(
    title: String,
    count: Int,
    background: Color,
    accent: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(17.dp)
    ) {
        Column(
            modifier = Modifier.padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Surface(
                color = background,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = if (title == "Học tập") {
                        Icons.Default.School
                    } else {
                        Icons.Default.Home
                    },
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.padding(9.dp)
                )
            }

            Text(
                title,
                color = DarkText,
                fontWeight = FontWeight.Bold
            )

            Text(
                "$count công việc",
                color = MutedText,
                fontSize = 12.sp
            )
        }
    }
}


@Composable
private fun ListContent(
    tasks: List<TodoTask>,
    filter: String,
    query: String,
    onFilter: (String) -> Unit,
    onQuery: (String) -> Unit,
    onOpenTask: (String) -> Unit,
    onToggle: (TodoTask) -> Unit
) {
    Spacer(Modifier.height(16.dp))

    Text(
        "Danh sách",
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        color = DarkText
    )

    Text(
        "${tasks.count { !it.completed }} việc chưa xong",
        color = MutedText,
        fontSize = 13.sp
    )

    Spacer(Modifier.height(14.dp))

    OutlinedTextField(
        value = query,
        onValueChange = onQuery,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Tìm công việc...") },
        leadingIcon = {
            Icon(Icons.Default.Search, null)
        },
        shape = RoundedCornerShape(15.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Blue,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )

    Spacer(Modifier.height(8.dp))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf("Tất cả", "Học tập", "Cuộc sống").forEach { cat ->
            FilterChip(
                selected = filter == cat,
                onClick = { onFilter(cat) },
                label = { Text(cat) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Blue,
                    selectedLabelColor = Color.White
                )
            )
        }
    }

    val filtered = tasks.filter {
        (filter == "Tất cả" || it.category == filter) &&
                it.title.contains(query, ignoreCase = true)
    }

    val pending = filtered.filter { !it.completed }
    val completed = filtered.filter { it.completed }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            SectionTitle("CẦN LÀM (${pending.size})")
        }

        items(pending, key = { "todo_${it.id}" }) { task ->
            TaskRow(
                task = task,
                onOpen = { onOpenTask(task.id) },
                onToggle = { onToggle(task) }
            )
        }

        item {
            Spacer(Modifier.height(6.dp))
            SectionTitle("ĐÃ XONG (${completed.size})")
        }

        items(completed, key = { "done_${it.id}" }) { task ->
            TaskRow(
                task = task,
                onOpen = { onOpenTask(task.id) },
                onToggle = { onToggle(task) }
            )
        }

        if (filtered.isEmpty()) {
            item {
                EmptyCard("Không tìm thấy công việc phù hợp.")
            }
        }

        item {
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun TaskRow(
    task: TodoTask,
    onOpen: () -> Unit,
    onToggle: () -> Unit
) {
    val cardColor = when {
        task.completed -> Color(0xFFF0FDF4)
        task.category == "Học tập" -> StudyBackground
        task.category == "Cuộc sống" -> LifeBackground
        else -> Color.White
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpen),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        shape = RoundedCornerShape(15.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = SuccessGreen,
                    uncheckedColor = Blue
                )
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    task.title,
                    color = DarkText,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (task.completed) {
                        TextDecoration.LineThrough
                    } else {
                        null
                    }
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    "${task.category} • " +
                            task.dueDate.ifBlank { "Chưa đặt hạn" },
                    color = categoryColor(task.category),
                    fontSize = 12.sp
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Xem chi tiết",
                tint = MutedText
            )
        }
    }
}


@Composable
private fun NotificationContent(
    tasks: List<TodoTask>,
    onOpenTask: (String) -> Unit
) {
    val pendingTasks = tasks.filter { !it.completed }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 18.dp)
    ) {
        Text(
            "Thông báo",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        Spacer(Modifier.height(5.dp))

        Text(
            "${pendingTasks.size} công việc đang chờ",
            color = MutedText,
            fontSize = 13.sp
        )

        Spacer(Modifier.height(16.dp))

        if (pendingTasks.isEmpty()) {
            EmptyCard("Bạn đã hoàn thành mọi công việc!")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(pendingTasks, key = { it.id }) { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOpenTask(task.id)
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement =
                                Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = null,
                                tint = Blue
                            )

                            Column(
                                verticalArrangement =
                                    Arrangement.spacedBy(5.dp)
                            ) {
                                Text(
                                    task.title,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkText
                                )

                                Text(
                                    task.category,
                                    color = categoryColor(task.category),
                                    fontSize = 12.sp
                                )

                                Text(
                                    "Hạn chót: " +
                                            task.dueDate.ifBlank {
                                                "Chưa đặt"
                                            },
                                    color = MutedText,
                                    fontSize = 13.sp
                                )

                                Text(
                                    "Nhắc trước: " +
                                            "${task.reminderMinutes} phút",
                                    color = MutedText,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun TaskEditor(
    initial: TodoTask?,
    onDismiss: () -> Unit,
    onSave: (TodoTask) -> Unit
) {
    var title by remember(initial) {
        mutableStateOf(initial?.title.orEmpty())
    }
    var category by remember(initial) {
        mutableStateOf(initial?.category ?: "Học tập")
    }
    var dueDate by remember(initial) {
        mutableStateOf(initial?.dueDate.orEmpty())
    }
    var note by remember(initial) {
        mutableStateOf(initial?.note.orEmpty())
    }
    var reminder by remember(initial) {
        mutableStateOf(
            (initial?.reminderMinutes ?: 15).toString()
        )
    }
    var validationError by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                if (initial == null) {
                    "Thêm công việc"
                } else {
                    "Sửa công việc"
                },
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 420.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Tên công việc") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Học tập", "Cuộc sống").forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat) }
                        )
                    }
                }

                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Hạn chót: yyyy-MM-dd") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Ghi chú") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = reminder,
                    onValueChange = { reminder = it },
                    label = { Text("Nhắc trước (phút)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (validationError.isNotEmpty()) {
                    Text(
                        validationError,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val dateValid = dueDate.isBlank() || run {
                        val parser = SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.US
                        ).apply {
                            isLenient = false
                        }

                        runCatching {
                            val parsed = parser.parse(dueDate)
                            parsed != null &&
                                    parser.format(parsed) == dueDate
                        }.getOrDefault(false)
                    }

                    val minutes = reminder.toIntOrNull()

                    when {
                        title.isBlank() -> {
                            validationError =
                                "Hãy nhập tên công việc"
                        }

                        !dateValid -> {
                            validationError =
                                "Ngày phải có dạng yyyy-MM-dd"
                        }

                        minutes == null || minutes < 0 -> {
                            validationError =
                                "Số phút nhắc không hợp lệ"
                        }

                        else -> {
                            onSave(
                                (initial ?: TodoTask()).copy(
                                    title = title.trim(),
                                    category = category,
                                    dueDate = dueDate,
                                    note = note,
                                    reminderMinutes = minutes
                                )
                            )
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue
                )
            ) {
                Text("Lưu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}


@Composable
private fun DetailContent(
    task: TodoTask,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onSave: (TodoTask) -> Unit,
    onDelete: () -> Unit,
    onToggle: () -> Unit
) {
    var newSubtask by remember(task.id) {
        mutableStateOf("")
    }
    var editingSubtask by remember {
        mutableStateOf<SubTask?>(null)
    }
    var subtaskTitle by remember {
        mutableStateOf("")
    }
    var confirmDelete by remember {
        mutableStateOf(false)
    }

    val subtaskDone = task.subtasks.count { it.completed }
    val subtaskProgress = if (task.subtasks.isEmpty()) {
        0f
    } else {
        subtaskDone.toFloat() / task.subtasks.size
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("← Quay lại", color = Blue)
            }

            TextButton(onClick = onEdit) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = null,
                    tint = Blue
                )
                Spacer(Modifier.width(5.dp))
                Text("Sửa", color = Blue)
            }
        }

        Surface(
            color = if (task.category == "Học tập") {
                StudyBackground
            } else {
                LifeBackground
            },
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                task.category,
                color = categoryColor(task.category),
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 7.dp
                )
            )
        }

        Text(
            task.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(15.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {
                Row {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Blue
                    )

                    Spacer(Modifier.width(10.dp))

                    Text(
                        "Hạn chót: " +
                                task.dueDate.ifBlank { "Chưa đặt" },
                        color = DarkText
                    )
                }

                Row {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = null,
                        tint = Blue
                    )

                    Spacer(Modifier.width(10.dp))

                    Text(
                        "Nhắc trước: ${task.reminderMinutes} phút",
                        color = DarkText
                    )
                }
            }
        }

        Text(
            "Ghi chú",
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text(
                task.note.ifBlank { "Chưa có ghi chú" },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = MutedText
            )
        }

        Text(
            "Việc nhỏ ($subtaskDone/${task.subtasks.size})",
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        LinearProgressIndicator(
            progress = { subtaskProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = Blue
        )

        task.subtasks.forEach { sub ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = sub.completed,
                        onCheckedChange = { checked ->
                            onSave(
                                task.copy(
                                    subtasks = task.subtasks.map {
                                        if (it.id == sub.id) {
                                            it.copy(completed = checked)
                                        } else {
                                            it
                                        }
                                    }
                                )
                            )
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = SuccessGreen
                        )
                    )

                    Text(
                        sub.title,
                        modifier = Modifier.weight(1f),
                        color = DarkText,
                        textDecoration = if (sub.completed) {
                            TextDecoration.LineThrough
                        } else {
                            null
                        }
                    )

                    IconButton(
                        onClick = {
                            editingSubtask = sub
                            subtaskTitle = sub.title
                        }
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Sửa việc nhỏ",
                            tint = Blue
                        )
                    }

                    IconButton(
                        onClick = {
                            onSave(
                                task.copy(
                                    subtasks = task.subtasks.filter {
                                        it.id != sub.id
                                    }
                                )
                            )
                        }
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Xóa việc nhỏ",
                            tint = Color.Red
                        )
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newSubtask,
                onValueChange = { newSubtask = it },
                label = { Text("Thêm việc nhỏ") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            TextButton(
                onClick = {
                    if (newSubtask.isNotBlank()) {
                        onSave(
                            task.copy(
                                subtasks = task.subtasks + SubTask(
                                    id = UUID.randomUUID().toString(),
                                    title = newSubtask.trim()
                                )
                            )
                        )
                        newSubtask = ""
                    }
                }
            ) {
                Text("Thêm", color = Blue)
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onToggle,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                if (task.completed) {
                    "Đánh dấu chưa xong"
                } else {
                    "Đánh dấu hoàn thành"
                }
            )
        }

        TextButton(
            onClick = { confirmDelete = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = null,
                tint = Color.Red
            )

            Spacer(Modifier.width(6.dp))

            Text("Xóa công việc", color = Color.Red)
        }

        Spacer(Modifier.height(20.dp))
    }

    if (editingSubtask != null) {
        AlertDialog(
            onDismissRequest = {
                editingSubtask = null
            },
            title = {
                Text("Sửa việc nhỏ")
            },
            text = {
                OutlinedTextField(
                    value = subtaskTitle,
                    onValueChange = { subtaskTitle = it },
                    label = { Text("Tên việc nhỏ") }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val id = editingSubtask?.id

                        if (subtaskTitle.isNotBlank()) {
                            onSave(
                                task.copy(
                                    subtasks = task.subtasks.map {
                                        if (it.id == id) {
                                            it.copy(
                                                title = subtaskTitle.trim()
                                            )
                                        } else {
                                            it
                                        }
                                    }
                                )
                            )
                            editingSubtask = null
                        }
                    }
                ) {
                    Text("Lưu")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        editingSubtask = null
                    }
                ) {
                    Text("Hủy")
                }
            }
        )
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = {
                confirmDelete = false
            },
            title = {
                Text("Xóa công việc?")
            },
            text = {
                Text(
                    "Bạn có chắc chắn muốn xóa? " +
                            "Thao tác này không thể hoàn tác."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmDelete = false
                        onDelete()
                    }
                ) {
                    Text("Xóa", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        confirmDelete = false
                    }
                ) {
                    Text("Hủy")
                }
            }
        )
    }
}


@Composable
private fun SectionTitle(title: String) {
    Text(
        title,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = MutedText
    )
}

@Composable
private fun EmptyCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(32.dp)
            )

            Spacer(Modifier.height(10.dp))

            Text(
                message,
                color = MutedText,
                fontSize = 14.sp
            )
        }
    }
}
