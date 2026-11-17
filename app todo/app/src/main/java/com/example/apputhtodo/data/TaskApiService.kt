package com.example.apputhtodo.data

import com.example.apputhtodo.model.SubTask
import com.example.apputhtodo.model.TodoTask
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.UUID

data class ApiSubTask(
    @SerializedName("title") val title: String? = null,
    @SerializedName("done") val done: Boolean? = null
)

data class ApiTaskResponse(
    @SerializedName("id") val id: Any? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("note") val note: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("date") val date: String? = null,
    @SerializedName("time") val time: String? = null,
    @SerializedName("priority") val priority: String? = null,
    @SerializedName("remind") val remind: Boolean? = null,
    @SerializedName("done") val done: Boolean? = null,
    @SerializedName("subtasks") val subtasks: List<ApiSubTask>? = null
)

fun ApiTaskResponse.toTodoTask(): TodoTask {
    val categoryDisplay = when {
        category.isNullOrBlank() -> "Học tập"
        category.equals("study", ignoreCase = true) -> "Học tập"
        category.equals("life", ignoreCase = true) -> "Cuộc sống"
        else -> category
    }

    val priorityDisplay = when {
        priority.equals("low", ignoreCase = true) -> "Thấp"
        priority.equals("high", ignoreCase = true) -> "Cao"
        priority.equals("Thấp", ignoreCase = true) -> "Thấp"
        priority.equals("Cao", ignoreCase = true) -> "Cao"
        else -> "Vừa"
    }

    return TodoTask(
        id = id?.toString() ?: UUID.randomUUID().toString(),
        title = title ?: "",
        category = categoryDisplay,
        time = time ?: "19:00",
        completed = done ?: false,
        dueDate = date ?: "",
        note = note ?: "",
        priority = priorityDisplay,
        reminderMinutes = if (remind == true) 15 else 0,
        subtasks = subtasks?.map { sub ->
            SubTask(
                id = UUID.randomUUID().toString(),
                title = sub.title ?: "",
                completed = sub.done ?: false
            )
        } ?: emptyList()
    )
}

interface TaskApi {
    @GET("mock/teach/tasks")
    suspend fun getTasks(
        @Header("Authorization") token: String = "Bearer TuanTruong-uth-student"
    ): List<ApiTaskResponse>

    @POST("mock/teach/tasks")
    suspend fun createTask(
        @Body task: ApiTaskResponse,
        @Header("Authorization") token: String = "Bearer TuanTruong-uth-student"
    ): ApiTaskResponse
}

object TaskApiRepository {
    private const val BASE_URL = "https://share.tryapps.store/"

    private val api: TaskApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaskApi::class.java)
    }

    suspend fun fetchTasks(token: String = "Bearer TuanTruong-uth-student"): List<TodoTask> {
        val response = api.getTasks(token)
        return response.map { it.toTodoTask() }
    }

    suspend fun createTask(task: TodoTask, token: String = "Bearer TuanTruong-uth-student"): TodoTask {
        val request = ApiTaskResponse(
            title = task.title,
            note = task.note,
            category = if (task.category == "Học tập") "study" else "life",
            date = task.dueDate,
            time = task.time,
            priority = when (task.priority) {
                "Thấp" -> "low"
                "Cao" -> "high"
                else -> "medium"
            },
            remind = task.reminderMinutes > 0,
            done = task.completed
        )
        return runCatching {
            api.createTask(request, token).toTodoTask()
        }.getOrDefault(task)
    }
}
