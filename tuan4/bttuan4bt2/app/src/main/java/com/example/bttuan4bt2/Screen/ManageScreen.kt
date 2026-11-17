package com.example.bttuan4bt2.Screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bttuan4bt2.model.Book
import com.example.bttuan4bt2.model.Library

@Composable
fun ManageScreen(library: Library, onChanged: () -> Unit) {
    var nameInput by remember { mutableStateOf(library.currentStudent?.name ?: "") }
    val selected = remember { mutableStateListOf<Book>() }
    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Hệ thống Quản lý Thư viện", style = MaterialTheme.typography.titleLarge)

        Text("Sinh viên", fontWeight = FontWeight.Bold)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            Button(onClick = {
                val student = library.currentStudent
                if (student == null) {
                    Toast.makeText(context, "Chưa có sinh viên nào", Toast.LENGTH_SHORT).show()
                } else {
                    try {
                        student.name = nameInput
                        onChanged()
                        Toast.makeText(context, "Đã đổi tên thành ${student.name}", Toast.LENGTH_SHORT).show()
                    } catch (e: IllegalArgumentException) {
                        Toast.makeText(context, e.message ?: "Tên không hợp lệ", Toast.LENGTH_SHORT).show()
                    }
                }
            }) { Text("Thay đổi") }
        }

        Text("Danh sách sách", fontWeight = FontWeight.Bold)
        LazyColumn(Modifier.heightIn(max = 300.dp)) {
            items(library.books) { book ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = book in selected,
                        onCheckedChange = { if (it) selected.add(book) else selected.remove(book) },
                        enabled = book.isAvailable
                    )
                    Text(book.title)
                }
            }
        }

        Button(
            onClick = {
                val ok = library.borrowBooks(selected.toList())
                Toast.makeText(context, "Đã mượn $ok/${selected.size} sách", Toast.LENGTH_SHORT).show()
                selected.clear()
                onChanged()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) { Text("Thêm") }
    }
}