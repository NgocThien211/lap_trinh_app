package com.example.bttuan4bt2.model


open class Book(id: String, title: String, val author: String) :
    LibraryItem(id, title), Borrowable {

    var borrower: Student? = null
        private set

    override val isAvailable: Boolean
        get() = borrower == null

    override fun borrowBy(student: Student): Boolean {
        if (!isAvailable) return false
        borrower = student
        return true
    }

    override fun giveBack() {
        borrower = null
    }

    override fun getInfo(): String {
        val status = borrower?.let { "Đang được ${it.name} mượn" } ?: "Còn trong thư viện"
        return "$title - $author ($status)"
    }
}