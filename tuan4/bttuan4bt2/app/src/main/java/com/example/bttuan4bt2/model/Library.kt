package com.example.bttuan4bt2.model

class Library {
    private val _books = mutableListOf<Book>()
    private val _students = mutableListOf<Student>()

    val books: List<Book> get() = _books.toList()
    val students: List<Student> get() = _students.toList()

    var currentStudent: Student? = null
        private set

    fun addBook(book: Book) {
        _books.add(book)
    }

    fun addStudent(student: Student) {
        _students.add(student)
        if (currentStudent == null) currentStudent = student
    }

    fun selectStudent(student: Student) {
        currentStudent = student
    }

    fun borrowBooks(selected: List<Book>): Int {
        val student = currentStudent ?: return 0
        return selected.count { student.borrow(it) }
    }
}