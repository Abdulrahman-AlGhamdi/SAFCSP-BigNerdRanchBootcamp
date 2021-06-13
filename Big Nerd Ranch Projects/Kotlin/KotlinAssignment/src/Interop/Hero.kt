@file:JvmName("Hero")

package Interop

import java.io.IOException

fun main() {

    val adversary = Jhava()
    adversary.greeting = "Hello, hero"
    println(adversary.utterGreeting())
    adversary.offerFood()
    try {
        adversary.extendHandInFriendship()
    }catch (e: Exception){
        println("Begone, foul beast!")
    }

    val friendshipLevel = adversary.determineFriendshipLevel()
    println(friendshipLevel?.toLowerCase() ?: "It's complicated.")

    val adversaryHitPoints: Int = adversary.hitPoint
    println(adversaryHitPoints.dec())
    println(adversaryHitPoints.javaClass)
}

@JvmOverloads
fun runSimulation(playerName: String, greetingFunction: (String, Int) -> String) {
    val numBuildings = (1..3).random()
    println(greetingFunction(playerName, numBuildings))
}

val translator = {utterance: String -> println(utterance.toLowerCase().capitalize())}

fun makeProclamation() = "Greeting, beast!"

@JvmOverloads
fun handOverFood(leftHand: String = "Berries", rightHand: String = "Beef"){
    println("Hmmm... You hand over some delicious $leftHand and $rightHand")
}

@Throws(IOException::class)
fun acceptApology(){
    throw IOException()
}

class Spellbook{
    @JvmField
    val spells = listOf("Magic Ms. L", "Lay on Hans")

    companion object{
        const val MAX_SPELL_COUNT = 10

        @JvmStatic
        fun getSpellbookGreeting() = println("I am the Great Grimoire!")
    }
}