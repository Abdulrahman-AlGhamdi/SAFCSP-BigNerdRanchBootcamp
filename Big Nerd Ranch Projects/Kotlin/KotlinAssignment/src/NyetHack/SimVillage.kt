package NyetHack

fun main() {

    runSimulation()
    println("\n${addIntegers(4,9)}")
    val name: (String) -> String = {fullName -> fullName.toUpperCase() }
    myName("Abdulrahman Al-Ghamdi", name)

    val player = Player("Abdulrahman")
    val room = Room("Fount of Blessings")
    printIsSourceOfBlessings(player)
    printIsSourceOfBlessings(room)

    var charChar: Any
    charChar = "Abdulrahman"
    println(genericGreeting(charChar))
    charChar = charArrayOf('S','S')
    println(genericGreeting(charChar))
    charChar = listOf("Abdulrahman", "Al-Ghamdi")
    println(genericGreeting(charChar))
}

fun <T> genericGreeting(greeting: T): String{
    val currentYear = 2020
    var result = ""
    if(greeting is String)
        result = "Welcome to SimVillage, $greeting! (copyright $currentYear)"
    if(greeting is CharArray)
        result = "Welcome to SimVillage, ${greeting.joinToString("")}! (copyright $currentYear)"
    if(greeting is List<*>)
        result = "Welcome to SimVillage, ${greeting.joinToString(" ")}! (copyright $currentYear)"
    return result
}

fun <T> printIsSourceOfBlessings(source: T) {
    val isSourceOfBlessings: Boolean = if (source is Player) {
        source.isBlessed
    } else {
        (source as Room).name == "Fount of Blessings"
    }
    println("$source is a source of blessings: $isSourceOfBlessings")
}

var addIntegers = fun(num1: Int, num2: Int): Int{
    return num1 + num2
}

inline fun myName(fullName: String ,name: (String) -> String){
    val result = name(fullName.toUpperCase())
    println(result)
}

fun runSimulation(){
    val greetingFunction = configureGreetingFunction()
    println(greetingFunction("Guyal"))
    println(greetingFunction("Guyal"))

}

fun configureGreetingFunction(): (String) -> String {
    val structureType = "Hospitals"
    var numBuildings = 5
    return { playerName: String ->
        val currentYear = 2020
        numBuildings += 1
        println("Adding $numBuildings $structureType")
        "Welcome to SimVillage, $playerName! (copyright $currentYear)"
    }
}