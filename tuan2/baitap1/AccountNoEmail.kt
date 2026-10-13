package com.example.btvntuan2bai1



fun getStudents(): List <Student>
{
    return listOf(
    Student(" Phan Ngọc Thiện ", "pnt@gmail.com", " 03425432"),
    Student(" Lê Xuân Thanh ", null, " 0344556"),
    Student("Phạm Quốc thiện","pqt@gmail.com",null),
        Student("Nguyên",null,null),
        Student("Nam",null,null)
    )
}
fun showText (value : String?):String{
    if (value == null)
    {
        return " chưa cập nhật"
    }
    else
    {
        return value
    }
}
fun countMissingEmail(Students: List<Student>): Int
{
    var count = 0
    for ( s in Students)
    {
        if( s.email == null)
        {
            count = count + 1
        }
    }
    return count

}
