package com.example.bttuan4bt2.model


class ReferenceBook(id: String, title: String, author: String) : Book(id, title, author) {

    override fun borrowBy(student: Student): Boolean = false

    override fun getInfo(): String = "[Tham khảo] " + super.getInfo()
}