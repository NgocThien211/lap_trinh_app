package com.example.apputhtodo.model


data class SubTask(
    val id: String = "",
    val title: String = "",
    val completed: Boolean = false
)

data class TodoTask(
    val id: String = "",
    val title: String = "",
    val category: String = "Học tập",
    val time: String = "19:00",
    val completed: Boolean = false,
    val dueDate: String = "",
    val note: String = "",
    val priority: String = "Vừa",
    val reminderMinutes: Int = 15,
    val subtasks: List<SubTask> = emptyList()
)
