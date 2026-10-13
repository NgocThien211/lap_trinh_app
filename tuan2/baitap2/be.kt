package com.example.btvntuan2bai3


fun getDiem() = 8.5


fun getKiemTra() = listOf(9.0, 7.0, 5.5, 3.0, 11.0)


fun xepLoai(score: Double): String {
    if (score < 0 || score > 10) {
        return "Không hợp lệ"
    } else if (score >= 8.0) {
        return "Giỏi"
    } else if (score >= 6.5) {
        return "Khá"
    } else if (score >= 5.0) {
        return "Trung bình"
    } else {
        return "Yếu"
    }
}
