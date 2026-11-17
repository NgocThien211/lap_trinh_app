package com.example.apputhtodo.data


import com.example.apputhtodo.model.TodoTask
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TodoRepository {

    private val db = FirebaseFirestore.getInstance()

    private fun collection(uid: String) =
        db.collection("users")
            .document(uid)
            .collection("tasks")

    fun observe(uid: String): Flow<List<TodoTask>> = callbackFlow {
        val listener = collection(uid).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            trySend(
                snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(TodoTask::class.java)?.copy(id = doc.id)
                } ?: emptyList()
            )
        }

        awaitClose {
            listener.remove()
        }
    }

    suspend fun add(uid: String, task: TodoTask) {
        collection(uid).add(task).await()
    }

    suspend fun update(uid: String, task: TodoTask) {
        require(task.id.isNotBlank()) {
            "ID công việc không được để trống"
        }

        collection(uid)
            .document(task.id)
            .set(task)
            .await()
    }

    suspend fun delete(uid: String, taskId: String) {
        collection(uid)
            .document(taskId)
            .delete()
            .await()
    }

    suspend fun fetchFromApi(token: String = "Bearer TuanTruong-uth-student"): List<TodoTask> {
        return TaskApiRepository.fetchTasks(token)
    }

}
