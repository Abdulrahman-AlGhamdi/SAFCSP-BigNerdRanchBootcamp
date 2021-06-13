package NyetHack

fun main() {

    var beverage: String? = null

    beverage = beverage?.let {
        "Your $it is ready."
    } ?: "You can't get your beverage."

    println(beverage)
}