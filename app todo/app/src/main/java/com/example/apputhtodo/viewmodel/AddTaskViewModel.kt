package com.example.apputhtodo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apputhtodo.data.TaskApiRepository
import com.example.apputhtodo.model.TodoTask
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskViewModel : ViewModel() {

    var title by mutableStateOf("")
    var note by mutableStateOf("")
    var category by mutableStateOf("Học tập")
    var dateOption by mutableStateOf("Hôm nay") // Hôm nay, Ngày mai, Tuần này
    var time by mutableStateOf("19:00")
    var remind by mutableStateOf(true)
    var priority by mutableStateOf("Vừa") // Thấp, Vừa, Cao
    var isSaving by mutableStateOf(false)

    fun loadInitial(task: TodoTask?) {
        title = task?.title ?: ""
        note = task?.note ?: ""
        category = task?.category ?: "Học tập"
        dateOption = "Hôm nay"
        time = task?.time?.ifBlank { "19:00" } ?: "19:00"
        remind = (task?.reminderMinutes ?: 15) > 0
        priority = task?.priority?.ifBlank { "Vừa" } ?: "Vừa"
    }

    private fun resolveDueDate(option: String): String {
        val cal = Calendar.getInstance()
        when (option) {
            "Ngày mai" -> cal.add(Calendar.DAY_OF_YEAR, 1)
            "Tuần này" -> cal.add(Calendar.DAY_OF_YEAR, 7)
        }
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.time)
    }

    fun submitTask(initial: TodoTask?, onSuccess: (TodoTask) -> Unit) {
        if (title.isBlank() || isSaving) return

        val draft = (initial ?: TodoTask()).copy(
            title = title.trim(),
            note = note.trim(),
            category = category,
            dueDate = resolveDueDate(dateOption),
            time = time.trim().ifBlank { "19:00" },
            reminderMinutes = if (remind) 15 else 0,
            priority = priority
        )

        viewModelScope.launch {
            isSaving = true
            val resultTask = if (initial == null) {
                TaskApiRepository.createTask(draft)
            } else {
                draft
            }
            isSaving = false
            onSuccess(resultTask.copy(id = initial?.id ?: ""))
        }
    }
}

