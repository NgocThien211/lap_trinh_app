package com.example.bttuan4bt2.model

interface Borrowable {
    val isAvailable: Boolean
    fun borrowBy(student: Student): Boolean
    fun giveBack()
}