package NyetHack

const val MAX_EXPERIENCE = 5000

fun main() {
    val playerName = "Estragon"
    var experiencePoints = 5
    experiencePoints += 5

    //Challenge 1:
    var hasSteed = false

    //Challenge 2:
    val pubName = "Unicorn’s Horn"
    val publicanName = "Publican"
    var playerGold = 50
    val pubMenu = listOf("Water","Juice","Bear")

    //Challenge 3:
    val magicMirror = playerName.reversed()

    //Print All Vars
    println("NyetHack.Player Name: $playerName")
    println("XP: $experiencePoints / $MAX_EXPERIENCE")
    if(hasSteed)
        println("$playerName has steed")
    else
        println("$playerName doesn't have steed")
    println("NyetHack.Player Gold: $playerGold")
    println("$playerName reached $pubName to buy from the $publicanName")
    println("the $publicanName offers $pubMenu")
    println("Name Reflection in the Mirror: $magicMirror")
}