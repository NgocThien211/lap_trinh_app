package com.example.bttuan4bt2.Screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bttuan4bt2.model.Library

@Composable
fun BookListScreen(library: Library) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(library.books) { book ->
            Card(Modifier.fillMaxWidth()) {
                Text(book.getInfo(), Modifier.padding(12.dp))
            }
        }
    }
}