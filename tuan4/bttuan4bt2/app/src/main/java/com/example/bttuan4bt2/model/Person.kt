package com.example.bttuan4bt2.model




abstract class Person(val id: String, name: String) {

    var name: String = name.trim()
        set(value) {
            require(value.isNotBlank()) { "Tên không được để trống" }
            field = value.trim()
        }

    abstract fun role(): String
}