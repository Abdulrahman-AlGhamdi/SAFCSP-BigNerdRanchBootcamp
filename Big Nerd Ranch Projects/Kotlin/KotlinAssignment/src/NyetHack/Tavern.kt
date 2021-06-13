package NyetHack

import java.io.File
import java.lang.StringBuilder

var PINT = 0.125
var DRAGON_COIN = 6.0
var PINT_PURCHASE = 12
var BARREL_GALLONS = 5
var DRAGON_COIN_REMAIN = 0.0
const val TAVERN_NAME = "Taernyl's Folly"
var UNIQUE_PATRONS = mutableListOf<String>()
val PATRON_LIST = mutableListOf("Eli","Mordoc","Sophie")
val LAST_NAME = listOf("Ironfoot", "Fernsworth", "Baggins")
val MENU_LIST = File("data/tavern-menu-items.txt").readText().split("\n")
val PATRON_GOLD = mutableMapOf<String,Double>()

fun main() {

    println(randomizedWords("NyetHack.Dragon,Skeleton,Vampire,Werewolves,NyetHack.Goblin,Griffin"))
    println()

    val welcome = "*** Welcome to Taernyl's Folly ***"
    println(welcome)
    println()

    print("Welcome, Madrigal".frame(5))
    println()

    MENU_LIST.forEach{
        val (type, name, price) = it.split(',')
        val priceFormat = stringFormatting(".", (welcome.length - price.trim().length - name.length))
        val typeFormat = stringFormatting(" ", (("$name$priceFormat$price".length - type.length) - 4) / 2)
        println("$typeFormat~[$type]~")
        println("$name$priceFormat$price")
    }
    println()

    val eliMessage = if(PATRON_LIST.contains("Eli")){
        "The Tavern Master Says: Eli's in the back playing cards."
    }else{
        "The Tavern Master Says: Eli isn't here."
    }
    println(eliMessage)

    val otherMessage = if(PATRON_LIST.containsAll(listOf("Sophie","Mordoc"))){
        "The tavern master says: Yea, they're seated by the stew kettle."
    }else{
        "the tavern master says: Nay, they departed hours ago."
    }
    println(otherMessage)

    UNIQUE_PATRONS = generateSequence {
        val first = PATRON_LIST.random()
        val last = LAST_NAME.random()
        "$first $last"
    }.distinct().take(9).toMutableList()

    UNIQUE_PATRONS.toSet().forEach{
        PATRON_GOLD[it] = 6.0
    }
    println()

    var orderCount = 0
    while (orderCount <= 9){
        placeOrder(
                UNIQUE_PATRONS.random(),
                MENU_LIST.random()
        )
        orderCount++
    }
}

fun String.frame(padding: Int): String {
    val middle = "*".padEnd(padding).plus(this).plus("*".padStart(padding))
    val end = (0 until middle.length).joinToString("") { "*" }
    return "$end\n$middle\n$end"
}

private fun String.toDragonSpeak(): String = replace(Regex("[aeiou]")){
    when (it.value){
        "a" -> "4"
        "e" -> "3"
        "i" -> "1"
        "o" -> "0"
        "u" -> "|_|"
        else -> it.value
    }
}

fun stringFormatting(symbol: String, length: Int): String{
    var format = StringBuilder()
    repeat(length){
        format.append(symbol)
    }
    return format.toString()
}

fun randomizedWords(monsters: String): String {
    return monsters.split(',').shuffled().toString()
}

fun performPurchase(price: Double, patronName: String){
    if (PATRON_GOLD.getValue(patronName) >= price){
        val totalPurse = PATRON_GOLD.getValue(patronName)
        PATRON_GOLD[patronName] = totalPurse - price
    }else{
        PATRON_GOLD.remove(patronName)
    }
}

private fun placeOrder(patronName: String, menuData: String) {
    val indexOfAPOptions = TAVERN_NAME.indexOf('\'')
    val tavernMaster = TAVERN_NAME.substring(0 until indexOfAPOptions)
    val (type, name, price) = menuData.split(',')

    if(PATRON_GOLD[patronName] != null){
        println("$patronName speaks with $tavernMaster about their order.")
        if(PATRON_GOLD.getValue(patronName) >= price.toDouble()){
            val message = "$patronName buys a $name ($type) for ${price.trim()} Gold."
            println(message)
            performPurchase(price.toDouble(), patronName)
            val phrase: String = if (name == "NyetHack.Dragon's Breath"){
                "$patronName exclaims ${("Ah, delicious $name").toLowerCase().toDragonSpeak()}"
            }else{
                "$patronName says: Thanks for the $name"
            }
            println("$phrase \n$patronName Balance: ${"%.2f".format(PATRON_GOLD.getValue(patronName))}")
        }else{
            println("$patronName booted out because have no money or cant afford the drink [${"%.4s".format(price)}]")
            println("$patronName, Balance: ${"%.2f".format(PATRON_GOLD.getValue(patronName))}")
            UNIQUE_PATRONS.remove(patronName)
            PATRON_GOLD.remove(patronName)
        }
        println()
    }
}
