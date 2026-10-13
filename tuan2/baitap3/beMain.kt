package com.example.btvntuan2bai2



class Students (val name: String, val score: Double?)

class ClassStats(
    val avg: Double,
    val countGioi: Int,
    val countMissing: Int,
    val max: Double
)
fun getStudents() = listOf(
    Students ("Nguyễn Văn An", 9.0),
    Students( "Phan Ngọc Thiện", 8.5),
    Students("Phạm Quốc Thiện", 7.0),
    Students("Lê Xuân Thanh",5.5 ),
    Students("Nguyễn Nam", 4.5)
)
fun xepLoai(score: Double): String {
    if (score >= 8.0) {
        return "Giỏi"
    } else if (score >= 6.5)
    {
        return "Khá"
    } else if (score >= 5.0)
    {
        return "Trung bình"
    } else if ( score >= 0.0)
    {
        return "Yếu"
    }
    else
    {
        return "chưa có điểm"
    }
}

fun loaiOf(score : Double?): String
{
    if(score == null)
    {
        return "chưa có điểm"
    }
    return xepLoai(score)
}

 fun tinhThongKe(students: List<Students>): ClassStats {
     var sum = 0.0
     var countScored = 0
     var maxScored = 0.0
     var countMissing = 0
     var countGioi = 0
     for (s in students) {
         val diem = s.score
         if (diem == null) {
             countMissing++
         } else {
             sum += diem
             countScored++
             if (xepLoai(diem) == "Giỏi") {
                 countGioi++
             }
             if (diem > maxScored) {
                 maxScored = diem

             }
         }
     }
     var avg = 0.0
     if(countScored > 0)
     {
         avg = sum/countScored
     }
     return ClassStats(avg, countGioi, countMissing, maxScored)
 }
