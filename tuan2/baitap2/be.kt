package com.example.btvntuan2bai3

val dsDiem = listOf(9.0, 7.0, 5.5, 3.0, 11.0)

fun laHopLe(score: Double): Boolean {
    if (score >= 0 && score <= 10) {
        return true
    } else {
        return false
    }
}

fun xepLoai(score: Double): String {
    if (!laHopLe(score)) {
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

fun tinhDiemTrungBinh(ds: List<Double>): Double {
    var tong = 0.0
    var dem = 0
    for (d in ds) {
        if (laHopLe(d)) {
            tong = tong + d
            dem = dem + 1
        }
    }
    if (dem == 0) {
        return 0.0
    }
    return tong / dem
}

fun getDiem(): Double {
    return tinhDiemTrungBinh(dsDiem)
}
fun getKiemTra(): List<Double> {
    return dsDiem
}