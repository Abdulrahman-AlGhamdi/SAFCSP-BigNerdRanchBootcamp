package NyetHack

fun main() {
    val gradesByStudent = mapOf("Josh" to 4.0, "Alex" to 2.0, "Jane" to 3.0)
    val reverseStudentGrades = gradesByStudent.map{it.value to it.key}.joinToString(" ")
    println(reverseStudentGrades)
    println()

    val valuesToAdd = listOf(1, 18, 73, 3, 44, 6, 1, 33, 2, 22, 5, 7)
    println(valuesToAdd)
    var value: Any
    // 1
    value = valuesToAdd.filter{it >= 5}
    println(value)
    // 2
    value = value.chunked(2)
    println(value)
    // 3
    value = value.map {it[0]*it[1]}
    println(value)
    // 4
    value = value.sum()
    println(value)
}