package com.example.apputhtodo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat

import com.example.apputhtodo.Screen.*
import com.example.apputhtodo.data.AuthRepository
import com.example.apputhtodo.data.TodoRepository
import com.example.apputhtodo.model.TodoTask
import com.google.firebase.messaging.FirebaseMessaging

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : ComponentActivity() {

    private val notificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            Log.d(
                "FCM_PERMISSION",
                "Quyền thông báo: $granted"
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                Log.d("FCM_TOKEN", token)
            }
            .addOnFailureListener { exception ->
                Log.e(
                    "FCM_TOKEN",
                    "Không lấy được FCM Token",
                    exception
                )
            }

        val authRepo = AuthRepository(this)
        val todoRepo = TodoRepository()


        setContent {
            MaterialTheme {

                val scope = rememberCoroutineScope()

                val snackbarHostState = remember {
                    SnackbarHostState()
                }


                var loggedIn by remember {
                    mutableStateOf(
                        authRepo.auth.currentUser != null
                    )
                }


                var guest by remember {
                    mutableStateOf(false)
                }


                var page by remember {
                    mutableStateOf(
                        if (authRepo.auth.currentUser != null) {
                            "home"
                        } else {
                            "login"
                        }
                    )
                }


                var tasks by remember {
                    mutableStateOf<List<TodoTask>>(
                        emptyList()
                    )
                }


                var error by remember {
                    mutableStateOf<String?>(null)
                }


                var isLoading by remember {
                    mutableStateOf(false)
                }


                var user by remember {
                    mutableStateOf(
                        authRepo.auth.currentUser
                    )
                }


                fun showMessage(message: String) {
                    scope.launch {
                        snackbarHostState.showSnackbar(message)
                    }
                }

                fun loginSuccess() {
                    val currentUser =
                        authRepo.auth.currentUser

                    if (currentUser != null) {
                        user = currentUser
                        loggedIn = true
                        guest = false
                        error = null
                        tasks = emptyList()
                        page = "home"
                    } else {
                        showMessage(
                            "Đăng nhập chưa thành công"
                        )
                    }
                }

                fun saveTask(task: TodoTask) {
                    if (guest) {
                        val savedTask = if (task.id.isBlank()) {
                            task.copy(id = UUID.randomUUID().toString())
                        } else {
                            task
                        }

                        tasks = if (task.id.isBlank()) {
                            tasks + savedTask
                        } else {
                            tasks.map {
                                if (it.id == savedTask.id) savedTask else it
                            }
                        }
                        return
                    }

                    val uid = user?.uid ?: run {
                        showMessage("Vui lòng đăng nhập")
                        return
                    }

                    scope.launch {
                        try {
                            if (task.id.isBlank()) {
                                todoRepo.add(uid, task)
                            } else {
                                todoRepo.update(uid, task)
                            }
                        } catch (e: Exception) {
                            error = e.localizedMessage
                            showMessage(error ?: "Không lưu được công việc")
                        }
                    }
                }

                fun deleteTask(taskId: String) {
                    if (guest) {
                        tasks = tasks.filter { it.id != taskId }
                        return
                    }

                    val uid = user?.uid ?: return

                    scope.launch {
                        try {
                            todoRepo.delete(uid, taskId)
                        } catch (e: Exception) {
                            error = e.localizedMessage
                            showMessage(error ?: "Không xóa được công việc")
                        }
                    }
                }

                fun fetchApiTasks() {
                    scope.launch {
                        isLoading = true
                        try {
                            val apiTasks = com.example.apputhtodo.data.TaskApiRepository.fetchTasks()
                            if (guest) {
                                val existingIds = tasks.map { it.id }.toSet()
                                val newTasks = apiTasks.filter { it.id !in existingIds }
                                tasks = tasks + newTasks
                            } else {
                                val uid = user?.uid
                                if (uid != null) {
                                    val existingTitles = tasks.map { it.title }.toSet()
                                    apiTasks.forEach { apiTask ->
                                        if (apiTask.title !in existingTitles) {
                                            todoRepo.add(uid, apiTask)
                                        }
                                    }
                                } else {
                                    val existingIds = tasks.map { it.id }.toSet()
                                    val newTasks = apiTasks.filter { it.id !in existingIds }
                                    tasks = tasks + newTasks
                                }
                            }
                            showMessage("Đã tải ${apiTasks.size} công việc từ API")
                        } catch (e: Exception) {
                            Log.e("API_ERROR", "Lỗi tải API", e)
                            showMessage("Lỗi tải API: ${e.localizedMessage}")
                        } finally {
                            isLoading = false
                        }
                    }
                }


                LaunchedEffect(
                    loggedIn,
                    guest,
                    user?.uid
                ) {
                    if (
                        loggedIn &&
                        !guest &&
                        user != null
                    ) {
                        tasks = emptyList()

                        todoRepo.observe(user!!.uid)
                            .catch { exception ->
                                error =
                                    exception.localizedMessage
                            }
                            .collect { newTasks ->
                                tasks = newTasks
                                error = null
                            }
                    } else if (guest && tasks.isEmpty()) {
                        fetchApiTasks()
                    }
                }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            snackbarHostState
                        )
                    }
                ) { paddingValues ->

                    Box(
                        modifier = Modifier.padding(
                            paddingValues
                        )
                    ) {

                        when {

                            !loggedIn &&
                                    !guest &&
                                    page == "email" -> {

                                EmailLoginScreen(
                                    onBack = {
                                        page = "login"
                                    },

                                    onSubmit = {
                                            email,
                                            password,
                                            isRegister ->

                                        if (
                                            email.isBlank() ||
                                            password.isBlank()
                                        ) {
                                            showMessage(
                                                "Vui lòng nhập đầy đủ thông tin"
                                            )
                                        } else {
                                            scope.launch {
                                                isLoading = true

                                                try {
                                                    if (isRegister) {
                                                        authRepo.registerWithEmail(
                                                            email,
                                                            password
                                                        )
                                                    } else {
                                                        authRepo.signInWithEmail(
                                                            email,
                                                            password
                                                        )
                                                    }

                                                    loginSuccess()

                                                } catch (
                                                    e: Exception
                                                ) {
                                                    showMessage(
                                                        e.localizedMessage
                                                            ?: "Xác thực Email thất bại"
                                                    )
                                                } finally {
                                                    isLoading = false
                                                }
                                            }
                                        }
                                    },

                                    onGoogleLogin = {
                                        page = "login"
                                    },

                                    onForgotPassword = { email ->

                                        if (email.isBlank()) {
                                            showMessage(
                                                "Vui lòng nhập Email trước"
                                            )
                                        } else {
                                            scope.launch {
                                                try {
                                                    authRepo.resetPassword(
                                                        email
                                                    )

                                                    showMessage(
                                                        "Đã gửi Email đặt lại mật khẩu"
                                                    )

                                                } catch (
                                                    e: Exception
                                                ) {
                                                    showMessage(
                                                        e.localizedMessage
                                                            ?: "Không gửi được Email"
                                                    )
                                                }
                                            }
                                        }
                                    },

                                    isLoading = isLoading
                                )
                            }

                            !loggedIn &&
                                    !guest &&
                                    page == "phone" -> {

                                Column {
                                    TextButton(
                                        onClick = {
                                            page = "login"
                                        }
                                    ) {
                                        Text("← Quay lại")
                                    }

                                    Text(
                                        "Đăng nhập bằng số điện thoại"
                                    )

                                    Text(
                                        "Chức năng xác thực OTP đang được tích hợp."
                                    )
                                }
                            }

                            !loggedIn && !guest -> {

                                LoginScreen(
                                    onGoogleLogin = {

                                        scope.launch {
                                            isLoading = true
                                            error = null

                                            try {
                                                authRepo.signInWithGoogle()

                                                loginSuccess()

                                            } catch (
                                                e: Exception
                                            ) {
                                                showMessage(
                                                    e.localizedMessage
                                                        ?: "Đăng nhập Google thất bại"
                                                )
                                            } finally {
                                                isLoading = false
                                            }
                                        }
                                    },

                                    onEmailLogin = {
                                        page = "email"
                                    },

                                    onPhoneLogin = {
                                        page = "phone"
                                    },

                                    onGuestLogin = {
                                        guest = true
                                        loggedIn = false
                                        user = null
                                        tasks = emptyList()
                                        error = null
                                        page = "home"
                                    },

                                    isLoading = isLoading
                                )
                            }

                            page == "profile" -> {

                                ProfileScreen(
                                    user = if (guest) {
                                        null
                                    } else {
                                        user
                                    },

                                    tasks = tasks,
                                    isGuest = guest,

                                    onBack = {
                                        page = "home"
                                    },

                                    onLogout = {
                                        authRepo.signOut()

                                        loggedIn = false
                                        guest = false
                                        user = null
                                        tasks = emptyList()
                                        error = null
                                        page = "login"
                                    }
                                )
                            }

                            else -> {

                                    TaskFlowScreen(
                                        userName = if (guest) {
                                            "Bạn"
                                        } else {
                                            user?.displayName
                                                ?: user?.email
                                                ?: "Bạn"
                                        },

                                        tasks = tasks,
                                        error = error,

                                        onProfile = {
                                            page = "profile"
                                        },

                                        onFetchApi = {
                                            fetchApiTasks()
                                        },

                                        onSave = { task ->
                                            saveTask(task)
                                        },

                                        onDelete = { taskId ->
                                            deleteTask(taskId)
                                        },

                                        onToggle = { task ->
                                            saveTask(
                                                task.copy(
                                                    completed = !task.completed
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
