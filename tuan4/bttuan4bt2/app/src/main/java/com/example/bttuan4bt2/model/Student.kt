package com.example.bttuan4bt2.model


class Student(id: String, name: String) : Person(id, name) {


    private val _borrowedBooks = mutableListOf<Book>()
    val borrowedBooks: List<Book>
        get() = _borrowedBooks.toList()

    override fun role() = "Sinh viên"

    fun borrow(book: Book): Boolean {
        if (_borrowedBooks.size >= MAX_BOOKS) return false
        if (!book.borrowBy(this)) return false
        _borrowedBooks.add(book)
        return true
    }

    fun giveBack(book: Book) {
        if (_borrowedBooks.remove(book)) book.giveBack()
    }

    companion object {
        const val MAX_BOOKS = 3
    }
}