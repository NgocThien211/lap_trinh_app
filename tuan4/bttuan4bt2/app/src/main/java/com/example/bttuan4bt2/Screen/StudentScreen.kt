package com.example.bttuan4bt2.Screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.bttuan4bt2.model.Library

@Composable
fun StudentScreen(library: Library, onChanged: () -> Unit) {
    val student = library.currentStudent
    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (student == null) {
            Text("Chưa có sinh viên nào")
        } else {
            Text("${student.role()}: ${student.name}", style = MaterialTheme.typography.titleMedium)
            Text("Sách đang mượn:")
            if (student.borrowedBooks.isEmpty()) Text("(chưa mượn cuốn nào)")

            student.borrowedBooks.forEach { book ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("• ${book.getInfo()}", modifier = Modifier.weight(1f))
                    Button(onClick = {
                        student.giveBack(book) // xóa sách khỏi danh sách mượn + xóa người mượn của sách
                        Toast.makeText(context, "Đã trả ${book.title}", Toast.LENGTH_SHORT).show()
                        onChanged()
                    }) { Text("Trả sách") }
                }
            }
        }
    }
}