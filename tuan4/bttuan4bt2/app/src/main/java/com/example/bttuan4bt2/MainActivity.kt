package com.example.bttuan4bt2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.bttuan4bt2.model.Book
import com.example.bttuan4bt2.model.Library
import com.example.bttuan4bt2.model.ReferenceBook
import com.example.bttuan4bt2.model.Student
import com.example.bttuan4bt2.Screen.LibraryApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val library = Library().apply {
            addStudent(Student("SV01", "Nguyen Van A"))
            addStudent(Student("SV02", "Tran Thi B"))
            addBook(Book("S01", "Sách 01", "Tác giả A"))
            addBook(Book("S02", "Sách 02", "Tác giả B"))
            addBook(ReferenceBook("S03", "Từ điển", "Nhiều tác giả"))
        }

        setContent {
            MaterialTheme { LibraryApp(library) }
        }
    }
}