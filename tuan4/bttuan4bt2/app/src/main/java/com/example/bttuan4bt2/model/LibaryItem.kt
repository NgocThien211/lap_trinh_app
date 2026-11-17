package com.example.bttuan4bt2.model



abstract class LibraryItem(val id: String, val title: String) {
    abstract fun getInfo(): String
}