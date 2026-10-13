package com.example.baitapthuchanhtuan2

fun tinh(a: String, b: String, phepToan: String): String {
    val x = a.toDoubleOrNull()
    val y = b.toDoubleOrNull()

    if (x == null || y == null) return ""

    if (phepToan == "+") return (x + y).toString()
    if (phepToan == "-") return (x - y).toString()
    if (phepToan == "*") return (x * y).toString()
    if (phepToan == "/") {
        if (y == 0.0) return "Không chia được cho 0"
        return (x / y).toString()
    }
    return ""
}