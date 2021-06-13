package NyetHack

fun main() {

    "Madrigal has left the building".easyPrint().addEnthusiasm().easyPrint()
    42.easyPrint()
    "How many vowels?".numVowels.easyPrint()

    val nullableString: String? = null
    nullableString printWithDefault "Default String"
}

infix fun String?.printWithDefault(default: String){
    print(this ?: default)
}

fun String.addEnthusiasm(amount: Int = 1): String = this + "!".repeat(amount)

private val String.numVowels get() = count{"aeiou".contains(it)}

fun <T> T.easyPrint(): T{
    println(this)
    return this
}